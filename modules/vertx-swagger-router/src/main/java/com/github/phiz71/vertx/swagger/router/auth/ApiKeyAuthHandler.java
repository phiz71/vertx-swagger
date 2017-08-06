package com.github.phiz71.vertx.swagger.router.auth;

import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.AuthProvider;
import io.vertx.ext.auth.User;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.Session;
import io.vertx.ext.web.handler.AuthHandler;
import io.vertx.ext.web.handler.impl.AuthHandlerImpl;

import static com.github.phiz71.vertx.swagger.router.auth.ApiKeyAuthHandler.Location.HEADER;
import static com.github.phiz71.vertx.swagger.router.auth.ApiKeyAuthHandler.Location.QUERY;

public class ApiKeyAuthHandler extends AuthHandlerImpl {

    public static final String API_KEY_NAME_PARAM = "name";
    public static final String API_KEY_VALUE_PARAM = "value";

    private final String name;
    private final ApiKeyAuthHandler.Location location;

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

    public void handle(RoutingContext context) {
        User user = context.user();
        if (user != null) {
            this.authorise(user, context);
        } else {
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

            JsonObject authInfo = new JsonObject().put(API_KEY_NAME_PARAM, this.name)
                    .put(API_KEY_VALUE_PARAM, value);
            
            this.authProvider.authenticate(authInfo, res -> {
                if (res.succeeded()) {
                    User authenticated = res.result();
                    context.setUser(authenticated);
                    Session session = context.session();
                    if (session != null) {
                        session.regenerateId();
                    }
                    this.authorise(authenticated, context);
                } else {
                    context.fail(401);
                }
            });
        }

    }
}
