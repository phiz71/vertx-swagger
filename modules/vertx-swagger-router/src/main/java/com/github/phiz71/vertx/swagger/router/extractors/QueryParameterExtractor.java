package com.github.phiz71.vertx.swagger.router.extractors;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import io.swagger.models.parameters.Parameter;
import io.swagger.models.parameters.QueryParameter;
import io.vertx.core.MultiMap;
import io.vertx.ext.web.RoutingContext;

public class QueryParameterExtractor implements ParameterExtractor {
    @Override
    public Object extract(String name, Parameter parameter, RoutingContext context) {
        QueryParameter queryParam = (QueryParameter) parameter;
        MultiMap params = context.request().params();
        if (!params.contains(name)) {
            if(queryParam.getRequired())
                throw new IllegalArgumentException("Missing required parameter: " + name);
            else
                return null;
        }
        
        if((queryParam.getAllowEmptyValue()==null || !queryParam.getAllowEmptyValue()) && StringUtils.isEmpty(params.get(name))) 
            throw new IllegalArgumentException("Empty value is not authorized for parameter: " + name);
        
        if ("array".equals(queryParam.getType()))
            if("multi".equals(queryParam.getCollectionFormat()))
                return params.getAll(name);
            else {
                List<String> resultParams = this.splitArrayParam(queryParam, params.get(name));
                if(resultParams != null) 
                    return resultParams;
            }
        return params.get(name);
    }
}
