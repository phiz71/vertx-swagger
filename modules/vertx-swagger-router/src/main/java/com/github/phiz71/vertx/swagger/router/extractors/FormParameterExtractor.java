package com.github.phiz71.vertx.swagger.router.extractors;

import io.swagger.models.parameters.FormParameter;
import io.swagger.models.parameters.Parameter;
import io.vertx.ext.web.FileUpload;
import io.vertx.ext.web.RoutingContext;

public class FormParameterExtractor extends AbstractSerializableParameterExtractor implements ParameterExtractor {
    @Override
    public Object extract(String name, Parameter parameter, RoutingContext context) {
        FormParameter formParam = (FormParameter) parameter;
        if ("file".equals(formParam.getType())) {
            for (FileUpload file : context.fileUploads()) {
                if (file.name().equals(name)) {
                    return file.uploadedFileName();
                }
            }
            if(formParam.getRequired())
                throw new IllegalArgumentException("Missing required parameter: " + name);
            return null;
        } else 
            return this.extract(name, parameter, context.request().formAttributes());
    }
}
