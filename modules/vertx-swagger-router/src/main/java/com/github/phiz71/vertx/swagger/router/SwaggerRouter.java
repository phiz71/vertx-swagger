package com.github.phiz71.vertx.swagger.router;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.github.phiz71.vertx.swagger.router.extractors.BodyParameterExtractor;
import com.github.phiz71.vertx.swagger.router.extractors.FormParameterExtractor;
import com.github.phiz71.vertx.swagger.router.extractors.HeaderParameterExtractor;
import com.github.phiz71.vertx.swagger.router.extractors.ParameterExtractor;
import com.github.phiz71.vertx.swagger.router.extractors.PathParameterExtractor;
import com.github.phiz71.vertx.swagger.router.extractors.QueryParameterExtractor;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.swagger.models.HttpMethod;
import io.swagger.models.Operation;
import io.swagger.models.Swagger;
import io.vertx.core.MultiMap;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.ReplyException;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

public class SwaggerRouter {

    private static Logger vertxLogger = LoggerFactory.getLogger(SwaggerRouter.class);

    public static final String CUSTOM_STATUS_CODE_HEADER_KEY="CUSTOM_STATUS_CODE";
    public static final String CUSTOM_STATUS_MESSAGE_HEADER_KEY="CUSTOM_STATUS_MESSAGE";
    
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
        return swaggerRouter(baseRouter, swagger, eventBus, new DefaultServiceIdResolver(), null);
    }

    public static Router swaggerRouter(Router baseRouter, Swagger swagger, EventBus eventBus, ServiceIdResolver serviceIdResolver) {
        return swaggerRouter(baseRouter, swagger, eventBus, serviceIdResolver, null);
    }

    public static Router swaggerRouter(Router baseRouter, Swagger swagger, EventBus eventBus, ServiceIdResolver serviceIdResolver, Function<RoutingContext, DeliveryOptions> configureMessage) {
        baseRouter.route().handler(BodyHandler.create());
        final String basePath = getBasePath(swagger);
        swagger.getPaths().forEach((path, pathDescription) -> pathDescription.getOperationMap().forEach((method, operation) -> {
            Route route = ROUTE_BUILDERS.get(method).buildRoute(baseRouter, convertParametersToVertx(basePath + path));
            String serviceId = serviceIdResolver.resolve(method, path, operation);
            configureRoute(route, serviceId, operation, eventBus, configureMessage);
        }));

        return baseRouter;
    }

    private static String getBasePath(Swagger swagger) {
        String result = swagger.getBasePath();
        if (result == null)
            result = "";
        return result;
    }

    private static void configureRoute(Route route, String serviceId, Operation operation, EventBus eventBus, Function<RoutingContext, DeliveryOptions> configureMessage) {
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

                // callback to configure message e.g. provide message header values
                DeliveryOptions deliveryOptions = configureMessage != null ? configureMessage.apply(context) : new DeliveryOptions();
                context.request().headers().forEach(entry -> deliveryOptions.addHeader(entry.getKey(), entry.getValue()));

                
                eventBus.<String> send(serviceId, message, deliveryOptions, operationResponse -> {
                    if (operationResponse.succeeded()) {
                        manageHeaders(context.response(), operationResponse.result().headers());

                        if (operationResponse.result().body() != null) {
                            context.response().end(operationResponse.result().body());
                        } else {
                            context.response().end();
                        }
                      
                    } else {
                        vertxLogger.error("Internal Server Error", operationResponse.cause());
                        manageError((ReplyException)operationResponse.cause(), context.response());
                    }
                });
            } catch (Exception e) {
                vertxLogger.error("sending Bad Request", e);
                badRequestEnd(context.response());
            }

        });

    }

    private static void manageHeaders(HttpServerResponse httpServerResponse, MultiMap messageHeaders) {
        if(messageHeaders.contains(CUSTOM_STATUS_CODE_HEADER_KEY)) {
            Integer customStatusCode = Integer.valueOf(messageHeaders.get(CUSTOM_STATUS_CODE_HEADER_KEY));
            httpServerResponse.setStatusCode(customStatusCode);
            messageHeaders.remove(CUSTOM_STATUS_CODE_HEADER_KEY);
        }
        if(messageHeaders.contains(CUSTOM_STATUS_MESSAGE_HEADER_KEY)) {
            String customStatusMessage = messageHeaders.get(CUSTOM_STATUS_MESSAGE_HEADER_KEY);
            httpServerResponse.setStatusMessage(customStatusMessage);
            messageHeaders.remove(CUSTOM_STATUS_MESSAGE_HEADER_KEY);
        }
        httpServerResponse.headers().addAll(messageHeaders);
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

    private static void manageError( ReplyException cause, HttpServerResponse response) {
        if(isExistingHttStatusCode(cause.failureCode())) {
            response.setStatusCode(cause.failureCode());
            if(StringUtils.isNotEmpty(cause.getMessage())) {
                response.setStatusMessage(cause.getMessage());
            }
        } else {
            response.setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code());
        }
        response.end();
    }

    private static boolean isExistingHttStatusCode(int failureCode) {
        try {
            HttpResponseStatus.valueOf(failureCode);
        } catch (IllegalArgumentException e) {
            vertxLogger.info(failureCode+" is not a valid HttpStatusCode", e);
            return false;
        }
        return true;
    }

    private static void badRequestEnd(HttpServerResponse response) {
        response.setStatusCode(400).setStatusMessage("Bad Request").end();
    }

    private interface RouteBuilder {
        Route buildRoute(Router router, String path);
    }

}
