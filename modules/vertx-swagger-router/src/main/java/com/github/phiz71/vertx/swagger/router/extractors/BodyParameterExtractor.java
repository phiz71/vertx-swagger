package com.github.phiz71.vertx.swagger.router.extractors;

import io.swagger.models.ArrayModel;
import io.swagger.models.parameters.BodyParameter;
import io.swagger.models.parameters.Parameter;
import io.vertx.core.json.DecodeException;
import io.vertx.ext.web.RoutingContext;

public class BodyParameterExtractor implements ParameterExtractor {
    @Override
    public Object extract(String name, Parameter parameter, RoutingContext context) {
        BodyParameter bodyParam = (BodyParameter) parameter;
        if ("".equals(context.getBodyAsString())) {
            if (bodyParam.getRequired())
                throw new IllegalArgumentException("Missing required parameter: " + name);
            else
                return null;
        }

        try {
            if(bodyParam.getSchema() instanceof ArrayModel) {
                return context.getBodyAsJsonArray();
            } else {
                return context.getBodyAsJson();
            }
        } catch (DecodeException e) {
            return context.getBodyAsString();  
        }
    }        
}
