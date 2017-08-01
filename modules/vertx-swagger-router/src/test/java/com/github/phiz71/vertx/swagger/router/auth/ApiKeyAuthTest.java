package com.github.phiz71.vertx.swagger.router.auth;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.github.phiz71.vertx.swagger.router.SwaggerRouter;

import io.swagger.models.Swagger;
import io.swagger.parser.SwaggerParser;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.file.FileSystem;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.AuthProvider;
import io.vertx.ext.auth.User;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import io.vertx.ext.web.Router;

@RunWith(VertxUnitRunner.class)
public class ApiKeyAuthTest {

    private static final int TEST_PORT = 9292;
    private static final String TEST_HOST = "localhost";
    private final String ALL_API_KEY_AUTH = "bar";
    private final String DUMMY_API_KEY_AUTH = "dummy";
    private static Vertx vertx;
    private static EventBus eventBus;
    private static HttpClient httpClient;
    private static HttpServer httpServer;

    @BeforeClass
    public static void beforeClass(TestContext context) {
        Async before = context.async();
        vertx = Vertx.vertx();
        eventBus = vertx.eventBus();

        AuthProvider apiKeyAllAuthProvider = new AuthProvider() {
			
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
		AuthProvider apiKeyDummyAuthProvider = new AuthProvider() {
			
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
        AuthProviderRegistry.register("apikey_all", apiKeyAllAuthProvider);
        AuthProviderRegistry.register("apikey_dummy", apiKeyDummyAuthProvider);
        
        // init Router
        FileSystem vertxFileSystem = vertx.fileSystem();
        vertxFileSystem.readFile("auth/apiKeyAuth.json", readFile -> {
            if (readFile.succeeded()) {
                Swagger swagger = new SwaggerParser().parse(readFile.result().toString(Charset.forName("utf-8")));
                Router swaggerRouter = SwaggerRouter.swaggerRouter(Router.router(vertx), swagger, eventBus);
                httpServer = vertx.createHttpServer().requestHandler(swaggerRouter::accept).listen(TEST_PORT, TEST_HOST, listen -> {
                    if (listen.succeeded()) {
                        before.complete();
                    } else {
                        context.fail(listen.cause());
                    }
                });
            } else {
                context.fail(readFile.cause());
            }
        });

        // init consumers
        eventBus.<JsonObject> consumer("GET_test").handler(message -> {
        	message.reply("Hi from GET_test");
        });
        eventBus.<JsonObject> consumer("GET_dummy").handler(message -> {
        	message.reply("Hi from GET_dummy");
        });
        eventBus.<JsonObject> consumer("GET_unsecured").handler(message -> {
        	message.reply("Hi from GET_unsecured");
        });

        // init http Server
        HttpClientOptions options = new HttpClientOptions();
        options.setDefaultPort(TEST_PORT);
        httpClient = Vertx.vertx().createHttpClient();

    }

    @Test(timeout = 2000)
    public void testApiKeyAuthAllKoNoCredentials(TestContext context) {
        Async async = context.async();
        httpClient.getNow(TEST_PORT, TEST_HOST, "/test", response -> {
        	context.assertEquals(response.statusCode(), 401);
            async.complete();
        });
    }

    @Test(timeout = 2000)
    public void testApiKeyAuthAllKoBadCredentials(TestContext context) {
        Async async = context.async();
        
        HttpClientRequest req = httpClient.get(TEST_PORT, TEST_HOST, "/test", response -> {
        	context.assertEquals(response.statusCode(), 401);
            async.complete();
        });
        req.putHeader("apikey-all", DUMMY_API_KEY_AUTH);
        req.end();
    }
    
    @Test(timeout = 2000)
    public void testApiKeyAuthAllOk(TestContext context) {
        Async async = context.async();
        
        HttpClientRequest req = httpClient.get(TEST_PORT, TEST_HOST, "/test", response -> {
        	context.assertEquals(response.statusCode(), 200);
        	async.complete();
        });
        req.putHeader("apikey-all", ALL_API_KEY_AUTH);
        req.end();
        
    }
    
    @Test(timeout = 2000)
    public void testApiKeyAuthDummyKoNoCredentials(TestContext context) {
        Async async = context.async();
        httpClient.getNow(TEST_PORT, TEST_HOST, "/dummy", response -> {
        	context.assertEquals(response.statusCode(), 401);
            async.complete();
        });
    }

    @Test(timeout = 2000)
    public void testApiKeyAuthDummyKoBadCredentials(TestContext context) {
        Async async = context.async();
        
        HttpClientRequest req = httpClient.get(TEST_PORT, TEST_HOST, "/dummy", response -> {
        	context.assertEquals(response.statusCode(), 401);
            async.complete();
        });
        req.putHeader("apikey-dummy", ALL_API_KEY_AUTH);
        req.end();
    }
    
    @Test(timeout = 2000)
    public void testApiKeyAuthDummyOk(TestContext context) {
        Async async = context.async();
        
        HttpClientRequest req = httpClient.get(TEST_PORT, TEST_HOST, "/dummy", response -> {
        	context.assertEquals(response.statusCode(), 200);
        	async.complete();
        });
        req.putHeader("apikey-dummy", DUMMY_API_KEY_AUTH);
        req.end();
        
    }
    
    @Test(timeout = 2000)
    public void testNoApiKeyAuthOK(TestContext context) {
        Async async = context.async();
        httpClient.getNow(TEST_PORT, TEST_HOST, "/unsecured", response -> {
        	context.assertEquals(response.statusCode(), 200);
            async.complete();
        });
    }
    
    @AfterClass
    public static void afterClass(TestContext context) {
        Async after = context.async();
        httpServer.close(completionHandler -> {
            if (completionHandler.succeeded()) {
                FileSystem vertxFileSystem = vertx.fileSystem();
                vertxFileSystem.deleteRecursive(".vertx", true, vertxDir -> {
                    if (vertxDir.succeeded()) {
                        after.complete();
                    } else {
                        context.fail(vertxDir.cause());
                    }
                });
            }
        });
    }

}
