package com.github.phiz71.vertx.swagger.router.extractors;

import io.swagger.models.parameters.Parameter;
import io.vertx.ext.web.RoutingContext;

public class PathParameterExtractor extends AbstractSerializableParameterExtractor implements ParameterExtractor {
    @Override
    public Object extract(String name, Parameter parameter, RoutingContext context) {
        return this.extract(name, parameter, context.request().params());
    }
}
