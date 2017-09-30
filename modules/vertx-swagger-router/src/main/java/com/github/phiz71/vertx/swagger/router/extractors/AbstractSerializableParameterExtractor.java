package com.github.phiz71.vertx.swagger.router.extractors;

import io.swagger.models.parameters.AbstractSerializableParameter;
import io.swagger.models.parameters.Parameter;
import io.swagger.models.parameters.SerializableParameter;
import io.vertx.core.MultiMap;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public abstract class AbstractSerializableParameterExtractor {
    public Object extract(String name, Parameter parameter, MultiMap params) {
        AbstractSerializableParameter abstractSerializableParameter = (AbstractSerializableParameter) parameter;
        if (!params.contains(name)) {
            if (abstractSerializableParameter.getRequired()) {
                throw new IllegalArgumentException("Missing required parameter: " + name);
            } else if (abstractSerializableParameter.getDefaultValue()!=null){
                return abstractSerializableParameter.getDefaultValue();
            } else {
                return null;
            }
        }

        if ((abstractSerializableParameter.getAllowEmptyValue() == null
                || !abstractSerializableParameter.getAllowEmptyValue())
                && StringUtils.isEmpty(params.get(name))) {
            throw new IllegalArgumentException(
                    "Empty value is not authorized for parameter: " + name);
        }

        if ("array".equals(abstractSerializableParameter.getType())) {
            if ("multi".equals(abstractSerializableParameter.getCollectionFormat())) {
                return params.getAll(name);
            } else {
                List<String> resultParams = this.splitArrayParam(abstractSerializableParameter,
                        params.get(name));
                if (resultParams != null) {
                    return resultParams;
                }
            }
        }

        return params.get(name);
    }

    private List<String> splitArrayParam(SerializableParameter param, String paramAsString) {
        String regex = getArrayRegex(param);
        try {
            return Arrays.asList(URLDecoder.decode(paramAsString, "UTF-8").split(regex));
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError("UTF-8 not supported");
        }
    }

    private String getArrayRegex(SerializableParameter param) {
        String regex;
        if (param.getCollectionFormat() == null) {
            regex = Pattern.quote(",");
        } else {
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
}