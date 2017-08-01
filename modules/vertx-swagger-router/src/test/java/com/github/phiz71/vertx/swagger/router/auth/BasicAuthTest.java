package com.github.phiz71.vertx.swagger.router.auth;

import java.nio.charset.Charset;

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
public class BasicAuthTest {

    private static final int TEST_PORT = 9292;
    private static final String TEST_HOST = "localhost";
    private final String ALL_BASIC_AUTH = "Basic Zm9vOmJhcg==";
    private final String DUMMY_BASIC_AUTH = "Basic ZHVtbXk6ZHVtbXk=";
    private static Vertx vertx;
    private static EventBus eventBus;
    private static HttpClient httpClient;
    private static HttpServer httpServer;

    @BeforeClass
    public static void beforeClass(TestContext context) {
        Async before = context.async();
        vertx = Vertx.vertx();
        eventBus = vertx.eventBus();

        AuthProvider basicAllAuthProvider = new AuthProvider() {
			
			@Override
			public void authenticate(JsonObject authInfo, Handler<AsyncResult<User>> handler) {
				String username = authInfo.getString("username");
				String password = authInfo.getString("password");
				if("foo".equals(username) && "bar".equals(password)) {
					System.out.println("login success for basic_all");
					handler.handle(Future.succeededFuture(new TestUser(username)));
				} else {
					System.out.println("login fails for basic_all");
					handler.handle(Future.failedFuture("Incorrect username/password"));
				}
			}
		};
		AuthProvider basicDummyAuthProvider = new AuthProvider() {
			
			@Override
			public void authenticate(JsonObject authInfo, Handler<AsyncResult<User>> handler) {
				String username = authInfo.getString("username");
				String password = authInfo.getString("password");
				if("dummy".equals(username) && "dummy".equals(password)) {
					System.out.println("login success for dummy");
					handler.handle(Future.succeededFuture(new TestUser(username)));
				} else {
					System.out.println("login fails for dummy");
					handler.handle(Future.failedFuture("Incorrect username/password"));
				}
			}
		};
        AuthProviderRegistry.register("basic_all", basicAllAuthProvider);
        AuthProviderRegistry.register("basic_dummy", basicDummyAuthProvider);
        
        // init Router
        FileSystem vertxFileSystem = vertx.fileSystem();
        vertxFileSystem.readFile("auth/basicAuth.json", readFile -> {
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
    public void testBasicAuthAllKoNoCredentials(TestContext context) {
        Async async = context.async();
        httpClient.getNow(TEST_PORT, TEST_HOST, "/test", response -> {
        	context.assertEquals(response.statusCode(), 401);
            String header = response.getHeader("WWW-Authenticate");
            context.assertNotNull(header);
        	context.assertEquals("Basic realm=\"vertx-web\"", header);
            async.complete();
        });
    }

    @Test(timeout = 2000)
    public void testBasicAuthAllKoBadCredentials(TestContext context) {
        Async async = context.async();
        
        HttpClientRequest req = httpClient.get(TEST_PORT, TEST_HOST, "/test", response -> {
        	context.assertEquals(response.statusCode(), 401);
        	String header = response.getHeader("WWW-Authenticate");
            context.assertNotNull(header);
         	context.assertEquals("Basic realm=\"vertx-web\"", header);
            async.complete();
        });
        req.putHeader("Authorization", DUMMY_BASIC_AUTH);
        req.end();
    }
    
    @Test(timeout = 2000)
    public void testBasicAuthAllOk(TestContext context) {
        Async async = context.async();
        
        HttpClientRequest req = httpClient.get(TEST_PORT, TEST_HOST, "/test", response -> {
        	context.assertEquals(response.statusCode(), 200);
        	async.complete();
        });
        req.putHeader("Authorization", ALL_BASIC_AUTH);
        req.end();
        
    }
    
    @Test(timeout = 2000)
    public void testBasicAuthDummyKoNoCredentials(TestContext context) {
        Async async = context.async();
        httpClient.getNow(TEST_PORT, TEST_HOST, "/dummy", response -> {
        	context.assertEquals(response.statusCode(), 401);
            String header = response.getHeader("WWW-Authenticate");
            context.assertNotNull(header);
        	context.assertEquals("Basic realm=\"vertx-web\"", header);
            async.complete();
        });
    }

    @Test(timeout = 2000)
    public void testBasicAuthDummyKoBadCredentials(TestContext context) {
        Async async = context.async();
        
        HttpClientRequest req = httpClient.get(TEST_PORT, TEST_HOST, "/dummy", response -> {
        	context.assertEquals(response.statusCode(), 401);
        	String header = response.getHeader("WWW-Authenticate");
            context.assertNotNull(header);
         	context.assertEquals("Basic realm=\"vertx-web\"", header);
            async.complete();
        });
        req.putHeader("Authorization", ALL_BASIC_AUTH);
        req.end();
    }
    
    @Test(timeout = 2000)
    public void testBasicAuthDummyOk(TestContext context) {
        Async async = context.async();
        
        HttpClientRequest req = httpClient.get(TEST_PORT, TEST_HOST, "/dummy", response -> {
        	context.assertEquals(response.statusCode(), 200);
        	async.complete();
        });
        req.putHeader("Authorization", DUMMY_BASIC_AUTH);
        req.end();
        
    }
    
    @Test(timeout = 2000)
    public void testNoBasicAuthOK(TestContext context) {
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
