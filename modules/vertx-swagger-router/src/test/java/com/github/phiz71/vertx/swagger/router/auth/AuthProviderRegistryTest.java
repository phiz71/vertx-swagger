package com.github.phiz71.vertx.swagger.router.auth;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.AuthProvider;
import io.vertx.ext.auth.User;
import io.vertx.ext.web.Cookie;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.Session;
import io.vertx.ext.web.impl.RouterImpl;
import io.vertx.ext.web.impl.RoutingContextImpl;
import io.vertx.ext.web.impl.Utils;
import io.vertx.ext.web.sstore.impl.SessionImpl;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AuthProviderRegistryTest {

    private static AuthProvider apiKeyAllAuthProvider, apiKeyDummyAuthProvider;
    
    @BeforeClass
    public static void init() {
        AuthProviderRegistry.clearRegistry();
        apiKeyAllAuthProvider = new AuthProvider() {
            @Override
            public void authenticate(JsonObject authInfo, Handler<AsyncResult<User>> handler) {
                String name= authInfo.getString("name");
                String value= authInfo.getString("value");
                if("apikey-all".equals(name) && "bar".equals(value)) {
                    System.out.println("login success for api_key_all");
                    handler.handle(Future.succeededFuture(new TestUser(name+" : "+value)));
                } else {
                    System.out.println("login fails for api_key_all");
                    handler.handle(Future.failedFuture("Incorrect name/value"));
                }
            }
        };
        
        apiKeyDummyAuthProvider = new AuthProvider() {
            @Override
            public void authenticate(JsonObject authInfo, Handler<AsyncResult<User>> handler) {
                String name = authInfo.getString("name");
                String value = authInfo.getString("value");
                if("apikey-dummy".equals(name) && "dummy".equals(value)) {
                    System.out.println("login success for dummy");
                    handler.handle(Future.succeededFuture(new TestUser(name+" : "+value)));
                } else {
                    System.out.println("login fails for dummy");
                    handler.handle(Future.failedFuture("Incorrect name/value"));
                }
            }
        };
        
    }

    @Test
    public void testRegister() {
        AuthProviderRegistry.register("apiKey", apiKeyAllAuthProvider);
        AuthProviderRegistry.register("dummy", apiKeyDummyAuthProvider);
        
        Assert.assertEquals(2, AuthProviderRegistry.getAuthProviderFactory().getAuthProviders().size());
        
        Assert.assertEquals(apiKeyAllAuthProvider, AuthProviderRegistry.getAuthProviderFactory().getAuthProviderByName("apiKey"));
        
        AuthProviderRegistry.clearRegistry();
        Assert.assertEquals(0, AuthProviderRegistry.getAuthProviderFactory().getAuthProviders().size());
        
        TestAuthProviderFactory anotherFactory = new TestAuthProviderFactory();
        anotherFactory.register("apiKey", apiKeyAllAuthProvider);
        anotherFactory.register("dummy", apiKeyDummyAuthProvider);
        
        AuthProviderRegistry.register(anotherFactory);
        Assert.assertEquals(2, AuthProviderRegistry.getAuthProviderFactory().getAuthProviders().size());
        Assert.assertEquals(apiKeyAllAuthProvider, AuthProviderRegistry.getAuthProviderFactory().getAuthProviderByName("apiKey"));
        
    }
    
    @Test
    public void testWithAnotherRegister() {
        TestAuthProviderFactory anotherFactory = new TestAuthProviderFactory();
        anotherFactory.register("apiKey", apiKeyAllAuthProvider);
        anotherFactory.register("dummy", apiKeyDummyAuthProvider);
        
        AuthProviderRegistry.register(anotherFactory);
        Assert.assertEquals(2, AuthProviderRegistry.getAuthProviderFactory().getAuthProviders().size());
        Assert.assertEquals(apiKeyAllAuthProvider, AuthProviderRegistry.getAuthProviderFactory().getAuthProviderByName("apiKey"));
        
    }

}

class TestAuthProviderFactory implements AuthProviderFactory {

    private Map<String, AuthProvider> myAuthProviders = new ConcurrentHashMap<>();
        
    public TestAuthProviderFactory() {
        super();
        this.myAuthProviders = new HashMap<>();
    }

    public void register(String name, AuthProvider authProvider) {
        this.myAuthProviders.put(name, authProvider);
    }
    
    @Override
    public AuthProvider getAuthProviderByName(String securityDefinitionName) {
        // TODO Auto-generated method stub
        return myAuthProviders.get(securityDefinitionName);
    }

    @Override
    public Map<String, AuthProvider> getAuthProviders() {
        // TODO Auto-generated method stub
        return myAuthProviders;
    }
    
}