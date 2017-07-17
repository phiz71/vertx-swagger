package com.github.phiz71.vertx.swagger.router.extractors;

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
        SerializableParameter serializableParam = (SerializableParameter) parameter;
        if (!params.contains(name)) {
            if (serializableParam.getRequired()) {
                throw new IllegalArgumentException("Missing required parameter: " + name);
            } else {
                return null;
            }
        }

        if ((serializableParam.getAllowEmptyValue() == null
                || !serializableParam.getAllowEmptyValue())
                && StringUtils.isEmpty(params.get(name))) {
            throw new IllegalArgumentException(
                    "Empty value is not authorized for parameter: " + name);
        }

        if ("array".equals(serializableParam.getType())) {
            if ("multi".equals(serializableParam.getCollectionFormat())) {
                return params.getAll(name);
            } else {
                List<String> resultParams = this.splitArrayParam(serializableParam,
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
            return Arrays.asList(URLDecoder.decode(paramAsString, "UTF_8").split(regex));
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