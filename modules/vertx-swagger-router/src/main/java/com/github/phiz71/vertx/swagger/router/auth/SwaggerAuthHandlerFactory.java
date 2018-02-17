package com.github.phiz71.vertx.swagger.router.auth;

import com.github.phiz71.vertx.swagger.router.SwaggerRouter;
import com.github.phiz71.vertx.swagger.router.auth.ApiKeyAuthHandler.Location;
import io.swagger.models.auth.ApiKeyAuthDefinition;
import io.swagger.models.auth.SecuritySchemeDefinition;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.auth.AuthProvider;
import io.vertx.ext.auth.User;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.AuthHandler;
import io.vertx.ext.web.handler.BasicAuthHandler;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.github.phiz71.vertx.swagger.router.auth.AuthProviderRegistry.getAuthProviderFactory;

public class SwaggerAuthHandlerFactory {

    private static Logger vertxLogger = LoggerFactory.getLogger(SwaggerAuthHandlerFactory.class);

    private final Map<String, AuthHandler> authHandlers = new ConcurrentHashMap<>();
    private final Map<String, SecuritySchemeDefinition> securitySchemes;

    public static SwaggerAuthHandlerFactory create(Map<String, SecuritySchemeDefinition> securitySchemes) {
        return new SwaggerAuthHandlerFactory(securitySchemes);
    }

    private SwaggerAuthHandlerFactory(Map<String, SecuritySchemeDefinition> securitySchemes) {
        this.securitySchemes = securitySchemes;
    }

    private AuthHandler getAuthHandler(String name) {
        AuthHandler authHandler = this.authHandlers.get(name);
        if (authHandler != null) {
            return authHandler;
        }

        AuthProvider authProvider = getAuthProviderFactory().getAuthProviderByName(name);
        if (authProvider == null) {
            return null;
        }

        SecuritySchemeDefinition securityScheme = this.securitySchemes.get(name);
        if(securityScheme != null) {
	        switch (securityScheme.getType()) {
	            case "apiKey":
	                ApiKeyAuthDefinition apiKeyAuthDefinition = (ApiKeyAuthDefinition) securityScheme;
	                Location apiKeyLocation = Location.valueOf(apiKeyAuthDefinition.getIn().name());
	                authHandler = ApiKeyAuthHandler.create(authProvider, apiKeyLocation, apiKeyAuthDefinition.getName());
	                break;
	            case "basic":
	                authHandler = BasicAuthHandler.create(authProvider);
	                break;
	            case "oauth2":
	                vertxLogger.warn("OAuth2 authentication has not been implemented yet!");
	                break;
	            default:
	                vertxLogger.warn("SecurityScheme is not authorized : " + securityScheme.getType());
	                break;
	        }
	        
	
	        if (authHandler != null) {
	            this.authHandlers.put(name, authHandler);
	        }
        } else {
            vertxLogger.warn("No securityScheme definition in swagger file for auth provider: " + name);
        }

        return authHandler;
    }

    public AuthHandler createAuthHandler(final List<Map<String, List<String>>> security) {
        return new AuthHandler() {
            private Set<String> authorities = new HashSet<>();

            public AuthHandler addAuthority(String authority) {
                this.authorities.add(authority);
                return this;
            }

            public AuthHandler addAuthorities(Set<String> authorities) {
                this.authorities.addAll(authorities);
                return this;
            }

            @Override
            public void parseCredentials(RoutingContext routingContext, Handler<AsyncResult<JsonObject>> handler) {

            }

            @Override
            public void authorize(User user, Handler<AsyncResult<Void>> handler) {

            }

            private void handle(InterceptableRoutingContext context, int orLevelIndex, int andLevelIndex) {
                // reset context
                context.clearUser();
                context.setNextCallback(null);
                context.setFailedCallback(null);

                // prepare routing next of failure behaviours
                if (andLevelIndex < security.get(orLevelIndex).size() - 1) {
                    context.setNextCallback(() -> handle(context, orLevelIndex, andLevelIndex + 1));
                }
                if (orLevelIndex < security.size() - 1) {
                    context.setFailedCallback(() -> handle(context, orLevelIndex + 1, 0));
                }

                // navigate to the auth provider at given orIndex and andIndex and call it.
                // if it fails, the context will call the next auth handler at the OR level, or fail or no more are available.
                // if it succeeds, the context will call the next auth handler at the AND level, or call the next route handler if no more are available.
                Map<String, List<String>> orSecurityIdentifiers = security.get(orLevelIndex);
                if (!orSecurityIdentifiers.isEmpty()) {
                    List<String> andSecurityIdentifiers = new ArrayList<>(orSecurityIdentifiers.keySet());
                    String securityIdentifier = andSecurityIdentifiers.get(andLevelIndex);
                    AuthHandler handler = SwaggerAuthHandlerFactory.this.getAuthHandler(securityIdentifier);
                    if (handler != null) {
                        context.put(SwaggerRouter.AUTH_PROVIDER_NAME_HEADER_KEY, securityIdentifier);
                        handler.addAuthorities(this.authorities).handle(context);
                    } else {
                        context.fail(401);
                    }
                } else {
                    context.fail(401);
                }
            }

            @Override
            public void handle(RoutingContext context) {
                handle(new InterceptableRoutingContext(context), 0, 0);
            }
        };
    }
}
