package io.vertx.rxjava.ext.swagger.router;

import io.swagger.models.Swagger;

import io.vertx.core.eventbus.DeliveryOptions;
import com.github.phiz71.vertx.swagger.router.DefaultServiceIdResolver;
import com.github.phiz71.vertx.swagger.router.ServiceIdResolver;
import io.vertx.rxjava.core.eventbus.EventBus;
import io.vertx.rxjava.ext.web.Router;
import io.vertx.rxjava.ext.web.RoutingContext;

import java.util.function.Function;

public class SwaggerRouter {

    public static Router swaggerRouter(Router baseRouter, Swagger swagger, EventBus eventBus) {
        return swaggerRouter(baseRouter, swagger, eventBus, new DefaultServiceIdResolver(), null);
    }

    public static Router swaggerRouter(Router baseRouter, Swagger swagger, EventBus eventBus, ServiceIdResolver serviceIdResolver) {
        return swaggerRouter(baseRouter, swagger, eventBus, serviceIdResolver, null);
    }

    public static Router swaggerRouter(Router baseRouter, Swagger swagger, EventBus eventBus, ServiceIdResolver serviceIdResolver, Function<RoutingContext, DeliveryOptions> configureMessage) {
        final io.vertx.ext.web.Router baseRouterDelegate = (io.vertx.ext.web.Router) baseRouter.getDelegate();
        final io.vertx.core.eventbus.EventBus eventBusDelegate = (io.vertx.core.eventbus.EventBus) eventBus.getDelegate();

        Function<io.vertx.ext.web.RoutingContext, DeliveryOptions> configureMessageDelegate = rc -> configureMessage.apply(new RoutingContext(rc));

        return new Router(com.github.phiz71.vertx.swagger.router.SwaggerRouter.swaggerRouter(baseRouterDelegate, swagger, eventBusDelegate, serviceIdResolver, configureMessageDelegate));
    }

}
