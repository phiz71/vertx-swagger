package com.github.phiz71.vertx.swagger.router.extractors;

import io.swagger.models.parameters.Parameter;
import io.vertx.ext.web.RoutingContext;

public class PathParameterExtractor implements ParameterExtractor {
    @Override
    public Object extract(String name, Parameter parameter, RoutingContext context) {
        return context.request().params().get(name);
    }
}
