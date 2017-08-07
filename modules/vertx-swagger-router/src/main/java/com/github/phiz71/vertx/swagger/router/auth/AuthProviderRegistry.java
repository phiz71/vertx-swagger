package com.github.phiz71.vertx.swagger.router.auth;

import io.vertx.ext.auth.AuthProvider;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AuthProviderRegistry implements AuthProviderFactory {

    private static AuthProviderRegistry instance = new AuthProviderRegistry();

    private Map<String, AuthProvider> authProviders = new ConcurrentHashMap<>();

    public static AuthProviderFactory getAuthProviderFactory() {
        return instance;
    }

    public static void register(AuthProviderFactory factory) {
        instance.authProviders.putAll(factory.getAuthProviders());
    }

    public static void register(String name, AuthProvider authProvider) {
        instance.authProviders.put(name, authProvider);
    }

    public static void clearRegistry() {
        instance.authProviders.clear();
    }

    @Override
    public AuthProvider getAuthProviderByName(String name) {
        return authProviders.get(name);
    }

    @Override
    public Map<String, AuthProvider> getAuthProviders() {
        return Collections.unmodifiableMap(authProviders);
    }
}