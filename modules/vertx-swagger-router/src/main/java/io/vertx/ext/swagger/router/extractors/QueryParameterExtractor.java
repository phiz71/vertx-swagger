package io.vertx.ext.swagger.router.extractors;

import io.swagger.models.parameters.Parameter;
import io.swagger.models.parameters.QueryParameter;
import io.vertx.core.MultiMap;
import io.vertx.ext.web.RoutingContext;

public class QueryParameterExtractor implements ParameterExtractor {
    @Override
    public Object extract(String name, Parameter parameter, RoutingContext context) {
        QueryParameter queryParam = (QueryParameter) parameter;
        MultiMap params = context.request().params();
        if (!params.contains(name) && queryParam.getRequired()) {
            throw new IllegalArgumentException("Missing required parameter: " + name);
        }
        if ("array".equals(queryParam.getType()))
            return params.getAll(name);
        return params.get(name);
    }
}
