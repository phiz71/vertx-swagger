package io.swagger.server.api.util;

import io.swagger.models.Swagger;

public final class SwaggerManager {
    private static SwaggerManager instance;
    private Swagger swagger;

    private SwaggerManager() {}

    public static SwaggerManager getInstance(){
        if(instance==null)
            instance=new SwaggerManager();
        return instance;
    }

    public Swagger getSwagger() {
        return swagger;
    }

    public void setSwagger(Swagger swagger) {
        this.swagger = swagger;
    }
}
