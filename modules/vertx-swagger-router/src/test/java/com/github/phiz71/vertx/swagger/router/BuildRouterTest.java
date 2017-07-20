package com.github.phiz71.vertx.swagger.router;

import java.nio.charset.Charset;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.swagger.models.Swagger;
import io.swagger.parser.SwaggerParser;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.file.FileSystem;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

@RunWith(VertxUnitRunner.class)
public class BuildRouterTest {

    private static final int TEST_PORT = 9292;
    private static final String TEST_HOST = "localhost";
    private static Vertx vertx;
    private static EventBus eventBus;
    private static HttpClient httpClient;
    private static HttpServer httpServer;

    @BeforeClass
    public static void beforeClass(TestContext context) {
        Async before = context.async();
        vertx = Vertx.vertx();
        eventBus = vertx.eventBus();

        // init Router
        FileSystem vertxFileSystem = vertx.fileSystem();
        vertxFileSystem.readFile("swagger.json", readFile -> {
            if (readFile.succeeded()) {
                Swagger swagger = new SwaggerParser().parse(readFile.result().toString(Charset.forName("utf-8")));
                Router swaggerRouter = SwaggerRouter.swaggerRouter(Router.router(vertx), swagger, eventBus, new DefaultServiceIdResolver(), new Function<RoutingContext, DeliveryOptions>() {
                    @Override
                    public DeliveryOptions apply(RoutingContext t) {
                        // TODO Auto-generated method stub
                        return new DeliveryOptions().addHeader("MainHeader", "MainValue");
                    }
                });
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
        eventBus.<JsonObject> consumer("GET_store_inventory").handler(message -> {
            message.reply(new JsonObject().put("sold", 2L).encode());
        });
        eventBus.<JsonObject> consumer("test.dummy").handler(message -> {
            context.fail("should not be called");
        });

        eventBus.<JsonObject> consumer("GET_user_logout").handler(message -> {
            message.reply(null);
        });
        eventBus.<JsonObject> consumer("GET_user_username").handler(message -> {
            String username = message.body().getString("username");
            DeliveryOptions options = new DeliveryOptions();
            switch (username) {
            case "statusCode":
                options.addHeader(SwaggerRouter.CUSTOM_STATUS_CODE_HEADER_KEY, "206");
                break;
            case "statusMessage":
                options.addHeader(SwaggerRouter.CUSTOM_STATUS_MESSAGE_HEADER_KEY, "My Custom Message");
                break;
            case "statusCodeAndMessage":
                options.addHeader(SwaggerRouter.CUSTOM_STATUS_CODE_HEADER_KEY, "206");
                options.addHeader(SwaggerRouter.CUSTOM_STATUS_MESSAGE_HEADER_KEY, "My Custom Message");
                break;
            case "simpleHeader":
                options.addHeader("Header_KEY", "Header_VALUE");
                break;
            case "simpleHeaderAndStatusCode":
                options.addHeader("Header_KEY", "Header_VALUE");
                options.addHeader(SwaggerRouter.CUSTOM_STATUS_CODE_HEADER_KEY, "206");
                break;
            }

            message.reply("", options);
        });

        eventBus.<JsonObject> consumer("GET_pet_petId").handler(message -> {
            DeliveryOptions options = new DeliveryOptions();
            message.reply(null, options.setHeaders(message.headers()));
        });
        
        // init http Server
        HttpClientOptions options = new HttpClientOptions();
        options.setDefaultPort(TEST_PORT);
        httpClient = Vertx.vertx().createHttpClient();

    }

    @Test(timeout = 2000)
    public void testResourceNotfound(TestContext context) {
        Async async = context.async();
        httpClient.getNow(TEST_PORT, TEST_HOST, "/v2/dummy", response -> {
            context.assertEquals(response.statusCode(), 404);
            async.complete();
        });

    }

    @Test(timeout = 2000)
    public void testMessageIsConsume(TestContext context) {
        Async async = context.async();
        httpClient.getNow(TEST_PORT, TEST_HOST, "/v2/store/inventory", response -> {
            response.bodyHandler(body -> {
                JsonObject jsonBody = new JsonObject(body.toString(Charset.forName("utf-8")));
                context.assertTrue(jsonBody.containsKey("sold"));
                context.assertEquals(2L, jsonBody.getLong("sold"));
                async.complete();
            });
        });
    }

    @Test(timeout = 2000, expected = TimeoutException.class)
    public void testMessageIsNotConsume(TestContext context) {
        Async async = context.async();
        httpClient.getNow(TEST_PORT, TEST_HOST, "/v2/user/logout", response -> response.exceptionHandler(err -> {
            async.complete();
        }));
    }

    @Test(timeout = 2000)
    public void testNullBodyResponse(TestContext context) {
        Async async = context.async();
        httpClient.getNow(TEST_PORT, TEST_HOST, "/v2/user/logout", response -> {
            context.assertEquals(response.statusCode(), 200);
            async.complete();
        });
    }

    @Test(timeout = 2000)
    public void testWithCustomStatusCode(TestContext context) {
        Async async = context.async();
        httpClient.getNow(TEST_PORT, TEST_HOST, "/v2/user/statusCode", response -> {
            context.assertEquals(206, response.statusCode());
            String header = response.getHeader(SwaggerRouter.CUSTOM_STATUS_CODE_HEADER_KEY);
            context.assertNull(header);
            async.complete();
        });
    }

    @Test(timeout = 2000)
    public void testWithCustomStatusMessage(TestContext context) {
        Async async = context.async();
        httpClient.getNow(TEST_PORT, TEST_HOST, "/v2/user/statusMessage", response -> {
            context.assertEquals("My Custom Message", response.statusMessage());
            async.complete();
        });
    }

    @Test(timeout = 2000)
    public void testWithCustomStatusCodeAndMessage(TestContext context) {
        Async async = context.async();
        httpClient.getNow(TEST_PORT, TEST_HOST, "/v2/user/statusCodeAndMessage", response -> {
            context.assertEquals(206, response.statusCode());
            context.assertEquals("My Custom Message", response.statusMessage());
            async.complete();
        });
    }

    @Test(timeout = 2000)
    public void testWithSimpleReturnedHeader(TestContext context) {
        Async async = context.async();
        httpClient.getNow(TEST_PORT, TEST_HOST, "/v2/user/simpleHeader", response -> {
            String header = response.getHeader("Header_KEY");
            context.assertNotNull(header);
            context.assertEquals("Header_VALUE", header);
            async.complete();
        });
    }

    @Test(timeout = 2000)
    public void testWithSimpleReturnedHeaderAndCustomStatusCode(TestContext context) {
        Async async = context.async();
        httpClient.getNow(TEST_PORT, TEST_HOST, "/v2/user/simpleHeaderAndStatusCode", response -> {
            context.assertEquals(206, response.statusCode());
            String header = response.getHeader("Header_KEY");
            context.assertNotNull(header);
            context.assertEquals("Header_VALUE", header);
            async.complete();
        });
    }

    
    @Test(timeout = 2000)
    public void testMainHeaderOnly(TestContext context) {
        Async async = context.async();
        httpClient.getNow(TEST_PORT, TEST_HOST, "/v2/pet/1", response -> {
            context.assertEquals(response.statusCode(), 200);
            String header = response.getHeader("MainHeader");
            context.assertNotNull(header);
            context.assertEquals("MainValue", header);
            async.complete();
        });
    }

    @Test(timeout = 2000)
    public void testMainHeaderWithAnotherHeader(TestContext context) {
        Async async = context.async();
        HttpClientRequest req = httpClient.get(TEST_PORT, TEST_HOST, "/v2/pet/1", response -> {
            context.assertEquals(response.statusCode(), 200);
            String header = response.getHeader("MainHeader");
            context.assertNotNull(header);
            context.assertEquals("MainValue", header);
            String customerHeader = response.getHeader("MyCustomerHeader");
            context.assertNotNull(customerHeader);
            context.assertEquals("MyCustomerValue", customerHeader);
            async.complete();
        });
        req.putHeader("MyCustomerHeader", "MyCustomerValue");
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
