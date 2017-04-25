package com.github.phiz71.vertx.swagger.router.extractors;

import io.swagger.models.parameters.BodyParameter;
import io.swagger.models.parameters.Parameter;
import io.vertx.ext.web.RoutingContext;

public class BodyParameterExtractor implements ParameterExtractor {
    @Override
    public Object extract(String name, Parameter parameter, RoutingContext context) {
        BodyParameter bodyParam = (BodyParameter) parameter;
        if ("".equals(context.getBodyAsString()) && bodyParam.getRequired()) {
            throw new IllegalArgumentException("Missing required parameter: " + name);
        }
        return context.getBodyAsString();
    }
}
