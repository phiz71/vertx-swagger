package com.github.phiz71.vertx.swagger.router.extractors;

import java.util.List;

import io.swagger.models.parameters.Parameter;
import io.swagger.models.parameters.PathParameter;
import io.vertx.ext.web.RoutingContext;

public class PathParameterExtractor implements ParameterExtractor {
    @Override
    public Object extract(String name, Parameter parameter, RoutingContext context) {
        PathParameter pathParam = (PathParameter) parameter;
        String paramAsString = context.request().params().get(name);
        if ("array".equals(pathParam.getType())) {
            List<String> params = this.splitArrayParam(pathParam, paramAsString);
            if(params != null) 
                return params;
        } 
        return paramAsString;
    }
}
