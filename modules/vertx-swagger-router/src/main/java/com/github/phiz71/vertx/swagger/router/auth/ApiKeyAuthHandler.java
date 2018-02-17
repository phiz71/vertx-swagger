package com.github.phiz71.vertx.swagger.router.auth;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.AuthProvider;
import io.vertx.ext.auth.User;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.Session;
import io.vertx.ext.web.handler.AuthHandler;
import io.vertx.ext.web.handler.impl.AuthHandlerImpl;

import java.util.Optional;

import static com.github.phiz71.vertx.swagger.router.auth.ApiKeyAuthHandler.Location.HEADER;
import static com.github.phiz71.vertx.swagger.router.auth.ApiKeyAuthHandler.Location.QUERY;

public class ApiKeyAuthHandler extends AuthHandlerImpl {

    public static final String API_KEY_NAME_PARAM = "name";
    public static final String API_KEY_VALUE_PARAM = "value";

    private final String name;
    private final ApiKeyAuthHandler.Location location;

    @Override
    public void parseCredentials(RoutingContext context, Handler<AsyncResult<JsonObject>> handler) {
        HttpServerRequest request = context.request();

        String value = null;
        switch (this.location) {
            case QUERY:
                value = request.getParam(this.name);
                break;
            case HEADER:
                value = request.headers().get(this.name);
                break;
            default:
                context.fail(401);
                return;
        }

        JsonObject authInfo = new JsonObject()
                .put(API_KEY_NAME_PARAM, this.name)
                .put(API_KEY_VALUE_PARAM, value);

        handler.handle(Future.succeededFuture(authInfo));
    }

    public enum Location {
        HEADER, QUERY;
    }

    public static AuthHandler create(AuthProvider authProvider, ApiKeyAuthHandler.Location location,
            String name) {
        return new ApiKeyAuthHandler(authProvider, location, name);
    }

    private ApiKeyAuthHandler(AuthProvider authProvider, ApiKeyAuthHandler.Location location,
            String name) {
        super(authProvider);
        this.location = location;
        this.name = name;
    }


}
