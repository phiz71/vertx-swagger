package io.vertx.ext.swagger.router.extractors;

import io.swagger.models.parameters.Parameter;
import io.swagger.models.parameters.PathParameter;
import io.vertx.core.MultiMap;
import io.vertx.ext.web.RoutingContext;

public class PathParameterExtractor implements ParameterExtractor {
    @Override
    public Object extract(String name, Parameter parameter, RoutingContext context) {
        PathParameter pathParam = (PathParameter) parameter;
        MultiMap params = context.request().params();
        if (!params.contains(name) && pathParam.getRequired()) {
            throw new IllegalArgumentException("Missing required parameter: " + name);
        }
        return params.get(name);
    }
}
