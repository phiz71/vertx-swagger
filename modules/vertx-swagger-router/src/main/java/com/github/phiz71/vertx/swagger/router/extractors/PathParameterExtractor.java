package com.github.phiz71.vertx.swagger.router.extractors;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.regex.Pattern;

import io.swagger.models.parameters.Parameter;
import io.swagger.models.parameters.PathParameter;
import io.vertx.ext.web.RoutingContext;

public class PathParameterExtractor implements ParameterExtractor {
    @Override
    public Object extract(String name, Parameter parameter, RoutingContext context) {
        PathParameter pathParam = (PathParameter) parameter;
        String paramAsString = context.request().params().get(name);
        if ("array".equals(pathParam.getType())) {
            String regex;
            if (pathParam.getCollectionFormat() == null)
                regex = Pattern.quote(",");
            else {
                switch (pathParam.getCollectionFormat()) {
                case "ssv":
                    regex = Pattern.quote(" ");
                    break;
                case "csv":
                    regex = Pattern.quote(",");
                    break;
                case "tsv":
                    regex = Pattern.quote("\t");
                    break;
                case "pipes":
                    regex = Pattern.quote("|");
                    break;
                default:
                    regex = Pattern.quote(",");
                    break;
                }
            }
            try {
                return Arrays.asList(URLDecoder.decode(paramAsString, "UTF-8").split(regex));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }

        return paramAsString;
    }
}
