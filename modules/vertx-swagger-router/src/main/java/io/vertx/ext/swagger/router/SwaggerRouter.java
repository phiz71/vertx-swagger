package io.vertx.ext.swagger.router;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.swagger.models.HttpMethod;
import io.swagger.models.Operation;
import io.swagger.models.Swagger;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.swagger.router.extractors.BodyParameterExtractor;
import io.vertx.ext.swagger.router.extractors.FormParameterExtractor;
import io.vertx.ext.swagger.router.extractors.HeaderParameterExtractor;
import io.vertx.ext.swagger.router.extractors.ParameterExtractor;
import io.vertx.ext.swagger.router.extractors.PathParameterExtractor;
import io.vertx.ext.swagger.router.extractors.QueryParameterExtractor;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

public class SwaggerRouter {

    private static Logger VERTX_LOGGER = LoggerFactory.getLogger(SwaggerRouter.class);

    private static Pattern PATH_PARAMETERS = Pattern.compile("\\{(.*)\\}");
    private static Map<HttpMethod, RouteBuilder> ROUTE_BUILDERS = new EnumMap<HttpMethod, RouteBuilder>(HttpMethod.class) {
        {
            put(HttpMethod.POST, Router::post);
            put(HttpMethod.GET, Router::get);
            put(HttpMethod.PUT, Router::put);
            put(HttpMethod.PATCH, Router::patch);
            put(HttpMethod.DELETE, Router::delete);
            put(HttpMethod.HEAD, Router::head);
            put(HttpMethod.OPTIONS, Router::options);
        }
    };

    private static Map<String, ParameterExtractor> PARAMETER_EXTRACTORS = new HashMap<String, ParameterExtractor>() {
        {
            put("path", new PathParameterExtractor());
            put("query", new QueryParameterExtractor());
            put("header", new HeaderParameterExtractor());
            put("formData", new FormParameterExtractor());
            put("body", new BodyParameterExtractor());  
        }
    };

    public static Router swaggerRouter(Router baseRouter, Swagger swagger, EventBus eventBus) {
        baseRouter.route().handler(BodyHandler.create());
        swagger.getPaths().forEach((path, pathDescription) -> pathDescription.getOperationMap().forEach((method, operation) -> {
            Route route = ROUTE_BUILDERS.get(method).buildRoute(baseRouter, convertParametersToVertx(path));
            String serviceId = computeServiceId(method, path);
            configureRoute(route, serviceId, operation, eventBus);
        }));

        return baseRouter;
    }

    private static void configureRoute(Route route, String serviceId, Operation operation, EventBus eventBus) {
        Optional.ofNullable(operation.getConsumes()).ifPresent(consumes -> consumes.forEach(route::consumes));
        Optional.ofNullable(operation.getProduces()).ifPresent(produces -> produces.forEach(route::produces));

        route.handler(context -> {
            try {
                JsonObject message = new JsonObject();
                operation.getParameters().forEach(parameter -> {
                    String name = parameter.getName();
                    Object value = PARAMETER_EXTRACTORS.get(parameter.getIn()).extract(name, parameter, context);
                    message.put(name, value);
                });
                eventBus.<JsonObject> send(serviceId, message, operationResponse -> {
                    if (operationResponse.succeeded()) {
                        if(operationResponse.result().body() != null)
                            context.response().end(operationResponse.result().body().encode());
                        else
                            context.response().end();
                    } else {
                        internalServerErrorEnd(context.response());
                    }
                });
            } catch (RuntimeException e) {
                VERTX_LOGGER.debug("sending Bad Request", e);
                badRequestEnd(context.response());
            }
            
        });

    }

    private static String convertParametersToVertx(String path) {
        Matcher pathMatcher = PATH_PARAMETERS.matcher(path);
        return pathMatcher.replaceAll(":$1");
    }

    private static String computeServiceId(HttpMethod httpMethod, String pathname) {
        return httpMethod.name() + pathname.replaceAll("-", "_").replaceAll("/", "_").replaceAll("[{}]", "");
    }

    private static void internalServerErrorEnd(HttpServerResponse response) {
        response.setStatusCode(500).setStatusMessage("Internal Server Error").end();
    }

    private static void badRequestEnd(HttpServerResponse response) {
        response.setStatusCode(400).setStatusMessage("Bad Request").end();
    }

    private interface RouteBuilder {
        Route buildRoute(Router router, String path);
    }

}
