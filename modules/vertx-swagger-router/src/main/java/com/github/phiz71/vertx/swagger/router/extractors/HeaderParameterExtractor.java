package com.github.phiz71.vertx.swagger.router.extractors;

import io.swagger.models.parameters.HeaderParameter;
import io.swagger.models.parameters.Parameter;
import io.vertx.core.MultiMap;
import io.vertx.ext.web.RoutingContext;

public class HeaderParameterExtractor implements ParameterExtractor {

    @Override
    public Object extract(String name, Parameter parameter, RoutingContext context) {
        HeaderParameter headerParam = (HeaderParameter) parameter;
        MultiMap params = context.request().headers();
        if (!params.contains(name) && headerParam.getRequired()) {
            throw new IllegalArgumentException("Missing required parameter: " + name);
        }
        if ("array".equals(headerParam.getType()))
            return params.getAll(name);
        return params.get(name);
    }

}
