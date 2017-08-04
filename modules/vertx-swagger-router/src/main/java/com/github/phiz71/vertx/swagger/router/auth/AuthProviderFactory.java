package com.github.phiz71.vertx.swagger.router.auth;

import io.vertx.ext.auth.AuthProvider;

import java.util.Map;

public interface AuthProviderFactory {
    AuthProvider getAuthProviderByName(String securityDefinitionName);

    Map<String, AuthProvider> getAuthProviders();
}