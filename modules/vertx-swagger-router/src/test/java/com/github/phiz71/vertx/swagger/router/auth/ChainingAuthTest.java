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
public class ChainingAuthTest {

    private static final int TEST_PORT = 9292;
    private static final String TEST_HOST = "localhost";
    private final String A_BASIC_AUTH = "Basic QTpB";
    private final String B_BASIC_AUTH = "Basic QjpC";
    private final String C_BASIC_AUTH = "Basic QzpD";
    private final String D_BASIC_AUTH = "Basic RDpE";
    private final String A_B_BASIC_AUTH = "Basic Zm9vOmJhcg==";
    private final String C_D_BASIC_AUTH = "Basic Zm9vMjpiYXIy";
    private static Vertx vertx;
    private static EventBus eventBus;
    private static HttpClient httpClient;
    private static HttpServer httpServer;

    @BeforeClass
    public static void beforeClass(TestContext context) {
        Async before = context.async();
        vertx = Vertx.vertx();
        eventBus = vertx.eventBus();

        Map<String, AuthProvider> authProviders = new HashMap<>();
        AuthProvider A_AuthProvider = new AuthProvider() {
			
			@Override
			public void authenticate(JsonObject authInfo, Handler<AsyncResult<User>> handler) {
				String username = authInfo.getString("username");
				String password = authInfo.getString("password");
				if(("A".equals(username) && "A".equals(password)) || ("foo".equals(username) && "bar".equals(password))) {
					System.out.println("login success for A");
					handler.handle(Future.succeededFuture(new TestUser(username)));
				} else {
					System.out.println("login fails for A");
					handler.handle(Future.failedFuture("Incorrect username/password"));
				}
			}
		};
        AuthProvider B_AuthProvider = new AuthProvider() {
			
			@Override
			public void authenticate(JsonObject authInfo, Handler<AsyncResult<User>> handler) {
				String username = authInfo.getString("username");
				String password = authInfo.getString("password");
				if("B".equals(username) && "B".equals(password) || ("foo".equals(username) && "bar".equals(password))) {
					System.out.println("login success for B");
					handler.handle(Future.succeededFuture(new TestUser(username)));
				} else {
					System.out.println("login fails for B");
					handler.handle(Future.failedFuture("Incorrect username/password"));
				}
			}
		};
        AuthProvider C_AuthProvider = new AuthProvider() {
			
			@Override
			public void authenticate(JsonObject authInfo, Handler<AsyncResult<User>> handler) {
				String username = authInfo.getString("username");
				String password = authInfo.getString("password");
				if("C".equals(username) && "C".equals(password) || ("foo2".equals(username) && "bar2".equals(password))) {
					System.out.println("login success for C");
					handler.handle(Future.succeededFuture(new TestUser(username)));
				} else {
					System.out.println("login fails for C");
					handler.handle(Future.failedFuture("Incorrect username/password"));
				}
			}
		};
        AuthProvider D_AuthProvider = new AuthProvider() {
			
			@Override
			public void authenticate(JsonObject authInfo, Handler<AsyncResult<User>> handler) {
				String username = authInfo.getString("username");
				String password = authInfo.getString("password");
				if("D".equals(username) && "D".equals(password) || ("foo2".equals(username) && "bar2".equals(password))) {
					System.out.println("login success for D");
					handler.handle(Future.succeededFuture(new TestUser(username)));
				} else {
					System.out.println("login fails for D");
					handler.handle(Future.failedFuture("Incorrect username/password"));
				}
			}
		};
        AuthProviderRegistry.register("A", A_AuthProvider);
        AuthProviderRegistry.register("B", B_AuthProvider);
        AuthProviderRegistry.register("C", C_AuthProvider);
        AuthProviderRegistry.register("D", D_AuthProvider);
        
        // init Router
        FileSystem vertxFileSystem = vertx.fileSystem();
        vertxFileSystem.readFile("auth/chainAuth.json", readFile -> {
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

        // init http Server
        HttpClientOptions options = new HttpClientOptions();
        options.setDefaultPort(TEST_PORT);
        httpClient = Vertx.vertx().createHttpClient();

    }

    @Test(timeout = 2000)
    public void testNoSec(TestContext context) {
        Async async = context.async();
        
        HttpClientRequest req = httpClient.get(TEST_PORT, TEST_HOST, "/test", response -> {
        	context.assertEquals(response.statusCode(), 401);
        	async.complete();
        });
        req.end();
    }
    
    @Test(timeout = 2000)
    public void testD(TestContext context) {
        Async async = context.async();
        
        HttpClientRequest req = httpClient.get(TEST_PORT, TEST_HOST, "/test", response -> {
        	context.assertEquals(response.statusCode(), 401);
        	async.complete();
        });
        req.putHeader("Authorization", D_BASIC_AUTH);
        req.end();
    }
    
    @Test(timeout = 2000)
    public void testC(TestContext context) {
        Async async = context.async();
        
        HttpClientRequest req = httpClient.get(TEST_PORT, TEST_HOST, "/test", response -> {
        	context.assertEquals(response.statusCode(), 401);
        	async.complete();
        });
        req.putHeader("Authorization", C_BASIC_AUTH);
        req.end();
    }
    
    @Test(timeout = 2000)
    public void testCD(TestContext context) {
        Async async = context.async();
        
        HttpClientRequest req = httpClient.get(TEST_PORT, TEST_HOST, "/test", response -> {
        	context.assertEquals(response.statusCode(), 200);
        	async.complete();
        });
        req.putHeader("Authorization", C_D_BASIC_AUTH);
        req.end();
    }
    
    @Test(timeout = 2000)
    public void testB(TestContext context) {
        Async async = context.async();
        
        HttpClientRequest req = httpClient.get(TEST_PORT, TEST_HOST, "/test", response -> {
        	context.assertEquals(response.statusCode(), 401);
        	async.complete();
        });
        req.putHeader("Authorization", B_BASIC_AUTH);
        req.end();
    }
    
    @Test(timeout = 2000)
    public void testA(TestContext context) {
        Async async = context.async();
        
        HttpClientRequest req = httpClient.get(TEST_PORT, TEST_HOST, "/test", response -> {
        	context.assertEquals(response.statusCode(), 401);
        	async.complete();
        });
        req.putHeader("Authorization", A_BASIC_AUTH);
        req.end();
    }
    
    @Test(timeout = 2000)
    public void testAB(TestContext context) {
        Async async = context.async();
        
        HttpClientRequest req = httpClient.get(TEST_PORT, TEST_HOST, "/test", response -> {
        	context.assertEquals(response.statusCode(), 200);
        	async.complete();
        });
        req.putHeader("Authorization", A_B_BASIC_AUTH);
        req.end();
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
