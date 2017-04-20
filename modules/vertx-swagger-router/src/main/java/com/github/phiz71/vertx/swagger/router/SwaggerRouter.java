package com.github.phiz71.vertx.swagger.router;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.phiz71.vertx.swagger.router.extractors.BodyParameterExtractor;
import com.github.phiz71.vertx.swagger.router.extractors.FormParameterExtractor;
import com.github.phiz71.vertx.swagger.router.extractors.HeaderParameterExtractor;
import com.github.phiz71.vertx.swagger.router.extractors.ParameterExtractor;
import com.github.phiz71.vertx.swagger.router.extractors.PathParameterExtractor;
import com.github.phiz71.vertx.swagger.router.extractors.QueryParameterExtractor;

import io.swagger.models.HttpMethod;
import io.swagger.models.Operation;
import io.swagger.models.Swagger;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.ReplyException;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

public class SwaggerRouter {

    private static Logger vertxLogger = LoggerFactory.getLogger(SwaggerRouter.class);

    private static final Pattern PATH_PARAMETER_NAME = Pattern.compile("\\{([A-Za-z][A-Za-z0-9_]*)\\}");
    private static final Pattern PATH_PARAMETERS = Pattern.compile("\\{(.*?)\\}");
    private static final Map<HttpMethod, RouteBuilder> ROUTE_BUILDERS = new EnumMap<>(HttpMethod.class);
    private static final Map<String, ParameterExtractor> PARAMETER_EXTRACTORS = new HashMap<>();
    static {
        ROUTE_BUILDERS.put(HttpMethod.POST, Router::post);
        ROUTE_BUILDERS.put(HttpMethod.GET, Router::get);
        ROUTE_BUILDERS.put(HttpMethod.PUT, Router::put);
        ROUTE_BUILDERS.put(HttpMethod.PATCH, Router::patch);
        ROUTE_BUILDERS.put(HttpMethod.DELETE, Router::delete);
        ROUTE_BUILDERS.put(HttpMethod.HEAD, Router::head);
        ROUTE_BUILDERS.put(HttpMethod.OPTIONS, Router::options);
        PARAMETER_EXTRACTORS.put("path", new PathParameterExtractor());
        PARAMETER_EXTRACTORS.put("query", new QueryParameterExtractor());
        PARAMETER_EXTRACTORS.put("header", new HeaderParameterExtractor());
        PARAMETER_EXTRACTORS.put("formData", new FormParameterExtractor());
        PARAMETER_EXTRACTORS.put("body", new BodyParameterExtractor());
    }

    public static Router swaggerRouter(Router baseRouter, Swagger swagger, EventBus eventBus) {
        return swaggerRouter(baseRouter, swagger, eventBus, new DefaultServiceIdResolver());
    }

    public static Router swaggerRouter(Router baseRouter, Swagger swagger, EventBus eventBus, ServiceIdResolver serviceIdResolver) {
        baseRouter.route().handler(BodyHandler.create());
        swagger.getPaths().forEach((path, pathDescription) -> pathDescription.getOperationMap().forEach((method, operation) -> {
            Route route = ROUTE_BUILDERS.get(method).buildRoute(baseRouter, convertParametersToVertx(path));
            String serviceId = serviceIdResolver.resolve(method, path, operation);
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

                eventBus.<String> send(serviceId, message, operationResponse -> {
                    if (operationResponse.succeeded()) {
                        if (operationResponse.result().body() != null) {
                            vertxLogger.debug(operationResponse.result().body());
                            context.response().end(operationResponse.result().body());
                        } else {
                            context.response().end();
                        }
                    } else {
                        manageError((ReplyException)operationResponse.cause(), context.response());
                    }
                });
            } catch (RuntimeException e) {
                vertxLogger.debug("sending Bad Request", e);
                badRequestEnd(context.response());
            }

        });

    }

    private static String convertParametersToVertx(String path) {
        Matcher pathMatcher = PATH_PARAMETERS.matcher(path);

        while (pathMatcher.find()) {
            checkParameterName(pathMatcher.group());
        }

        return pathMatcher.replaceAll(":$1");
    }

    private static void checkParameterName(String parameterPlaceholder) {
        final Matcher matcher = PATH_PARAMETER_NAME.matcher(parameterPlaceholder);

        if (!matcher.matches()) {
            final String parameterName = parameterPlaceholder.substring(1, parameterPlaceholder.length() - 1);
            throw new IllegalArgumentException("Illegal path parameter name: " + parameterName + ". Parameter names should only consist of alphabetic character, "
                    + "numeric character or underscore and follow this pattern: [A-Za-z][A-Za-z0-9_]*");
        }
    }

    private static void manageError(ReplyException cause, HttpServerResponse response) {
        response.setStatusCode(cause.failureCode()).setStatusMessage(cause.getMessage()).end();
    }

    private static void badRequestEnd(HttpServerResponse response) {
        response.setStatusCode(400).setStatusMessage("Bad Request").end();
    }

    private interface RouteBuilder {
        Route buildRoute(Router router, String path);
    }

}
