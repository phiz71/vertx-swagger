package com.github.phiz71.vertx.swagger.router.extractors;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import io.swagger.models.parameters.Parameter;
import io.swagger.models.parameters.SerializableParameter;
import io.vertx.ext.web.RoutingContext;

public interface ParameterExtractor {
    Object extract(String name, Parameter parameter, RoutingContext context);
    
    default String getArrayRegex(SerializableParameter param) {
        String regex;
        if (param.getCollectionFormat() == null)
            regex = Pattern.quote(",");
        else {
            switch (param.getCollectionFormat()) {
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
        return regex;
    }
    
    default List<String> splitArrayParam(SerializableParameter param, String paramAsString) {
        String regex = getArrayRegex(param);
        try {
            return Arrays.asList(URLDecoder.decode(paramAsString, "UTF-8").split(regex));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
