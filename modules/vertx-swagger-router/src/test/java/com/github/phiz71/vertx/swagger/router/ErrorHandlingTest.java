package com.github.phiz71.vertx.swagger.router;

import java.nio.charset.Charset;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.swagger.models.Swagger;
import io.swagger.parser.SwaggerParser;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.file.FileSystem;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import io.vertx.ext.web.Router;

@RunWith(VertxUnitRunner.class)
public class ErrorHandlingTest {

    private static final int TEST_PORT = 9292;
    private static final String TEST_HOST = "localhost";
    private static final String ERROR_CUSTOM_MESSAGE = "My custom message";
    private static final int EXISTING_HTTP_STATUS_CODE = 404;
    private static final int DEFAULT_HTTP_STATUS_CODE = HttpResponseStatus.INTERNAL_SERVER_ERROR.code();
    private static final int NOT_EXISTING_HTTP_STATUS_CODE = 1;
    private static final String NOT_EXISTING_HTTP_STATUS_MESSAGE = HttpResponseStatus.valueOf(NOT_EXISTING_HTTP_STATUS_CODE).reasonPhrase();
    private static final int INVALID_HTTP_STATUS_CODE = -1;
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
        eventBus.<JsonObject> consumer("GET_store_inventory").handler(message -> {
            message.fail(EXISTING_HTTP_STATUS_CODE, ERROR_CUSTOM_MESSAGE);
        });
        eventBus.<JsonObject> consumer("GET_store_order_orderId").handler(message -> {
            message.fail(EXISTING_HTTP_STATUS_CODE, null);
        });
        eventBus.<JsonObject> consumer("DELETE_store_order_orderId").handler(message -> {
            message.fail(EXISTING_HTTP_STATUS_CODE, "");
        });
        eventBus.<JsonObject> consumer("GET_pet_petId").handler(message -> {
            message.fail(NOT_EXISTING_HTTP_STATUS_CODE, ERROR_CUSTOM_MESSAGE);
        });
        eventBus.<JsonObject> consumer("POST_pet_petId").handler(message -> {
            message.fail(NOT_EXISTING_HTTP_STATUS_CODE, null);
        });
        eventBus.<JsonObject> consumer("DELETE_pet_petId").handler(message -> {
            message.fail(NOT_EXISTING_HTTP_STATUS_CODE, "");
        });
        eventBus.<JsonObject> consumer("POST_pet_petId_uploadImage").handler(message -> {
            message.fail(INVALID_HTTP_STATUS_CODE, ERROR_CUSTOM_MESSAGE);
        });
        eventBus.<JsonObject> consumer("GET_user_login").handler(message -> {
            message.fail(INVALID_HTTP_STATUS_CODE, null);
        });
        eventBus.<JsonObject> consumer("GET_pet_findByStatus").handler(message -> {
            message.fail(INVALID_HTTP_STATUS_CODE, "");
        });

        // init http Server
        HttpClientOptions options = new HttpClientOptions();
        options.setDefaultPort(TEST_PORT);
        httpClient = Vertx.vertx().createHttpClient();

    }

    @Test(timeout = 2000)
    public void testStatusCodeAndStatusMessage(TestContext context) {
        Async async = context.async();
        httpClient.getNow(TEST_PORT, TEST_HOST, "/v2/store/inventory", response -> {
            response.bodyHandler(body -> {
                context.assertEquals(response.statusCode(), EXISTING_HTTP_STATUS_CODE);
                context.assertEquals(response.statusMessage(), ERROR_CUSTOM_MESSAGE);
                async.complete();
            });
        });
    }

    @Test(timeout = 2000)
    public void testStatusCodeAndNullStatusMessage(TestContext context) {
        Async async = context.async();
        httpClient.getNow(TEST_PORT, TEST_HOST, "/v2/store/order/1", response -> {
            context.assertEquals(response.statusCode(), EXISTING_HTTP_STATUS_CODE);
            context.assertEquals(response.statusMessage(), HttpResponseStatus.valueOf(EXISTING_HTTP_STATUS_CODE).reasonPhrase());
            async.complete();
        });
    }

    @Test(timeout = 2000)
    public void testStatusCodeAndEmptyStatusMessage(TestContext context) {
        Async async = context.async();
        httpClient.delete(TEST_PORT, TEST_HOST, "/v2/store/order/1", response -> {
            context.assertEquals(response.statusCode(), EXISTING_HTTP_STATUS_CODE);
            context.assertEquals(response.statusMessage(), HttpResponseStatus.valueOf(EXISTING_HTTP_STATUS_CODE).reasonPhrase());
            async.complete();
        }).end();
    }

    @Test(timeout = 2000)
    public void testInexistingStatusCodeAndStatusMessage(TestContext context) {
        Async async = context.async();
        httpClient.getNow(TEST_PORT, TEST_HOST, "/v2/pet/5", response -> {
            response.bodyHandler(body -> {
                context.assertEquals(response.statusCode(), NOT_EXISTING_HTTP_STATUS_CODE);
                context.assertEquals(response.statusMessage(), ERROR_CUSTOM_MESSAGE);
                async.complete();
            });
        });
    }

    @Test(timeout = 2000)
    public void testInexistingStatusCodeAndNullStatusMessage(TestContext context) {
        Async async = context.async();
        httpClient.post(TEST_PORT, TEST_HOST, "/v2/pet/1", response -> response.bodyHandler(result -> {
            context.assertEquals(response.statusCode(), NOT_EXISTING_HTTP_STATUS_CODE);
            context.assertEquals(response.statusMessage(), NOT_EXISTING_HTTP_STATUS_MESSAGE);
            async.complete();
        })).putHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded").end();
    }

    @Test(timeout = 2000)
    public void testInexistingStatusCodeAndEmptyStatusMessage(TestContext context) {
        Async async = context.async();
        httpClient.delete(TEST_PORT, TEST_HOST, "/v2/pet/1", response -> response.bodyHandler(result -> {
            context.assertEquals(response.statusCode(), NOT_EXISTING_HTTP_STATUS_CODE);
            context.assertEquals(response.statusMessage(), NOT_EXISTING_HTTP_STATUS_MESSAGE);
            async.complete();
        })).end();

    }

    @Test(timeout = 2000)
    public void testInvalidStatusCodeAndStatusMessage(TestContext context) {
        Async async = context.async();
        httpClient.post(TEST_PORT, TEST_HOST, "/v2/pet/1/uploadImage", response -> response.bodyHandler(result -> {
            context.assertEquals(response.statusCode(), DEFAULT_HTTP_STATUS_CODE);
            context.assertEquals(response.statusMessage(), HttpResponseStatus.valueOf(DEFAULT_HTTP_STATUS_CODE).reasonPhrase());
            async.complete();
        })).putHeader(HttpHeaders.CONTENT_TYPE, "multipart/form-data; boundary=MyBoundary").end();
    }

    @Test(timeout = 2000)
    public void testInvalidStatusCodeAndNullStatusMessage(TestContext context) {
        Async async = context.async();
        httpClient.getNow(TEST_PORT, TEST_HOST, "/v2/user/login?username=myUser&password=mySecret", response -> {
            response.bodyHandler(body -> {
                context.assertEquals(response.statusCode(), DEFAULT_HTTP_STATUS_CODE);
                context.assertEquals(response.statusMessage(), HttpResponseStatus.valueOf(DEFAULT_HTTP_STATUS_CODE).reasonPhrase());
                async.complete();
            });
        });
    }

    @Test(timeout = 2000)
    public void testInvalidStatusCodeAndEmptyStatusMessage(TestContext context) {
        Async async = context.async();
        httpClient.getNow(TEST_PORT, TEST_HOST, "/v2/pet/findByStatus?status=available", response -> {
            response.bodyHandler(body -> {
                context.assertEquals(response.statusCode(), DEFAULT_HTTP_STATUS_CODE);
                context.assertEquals(response.statusMessage(), HttpResponseStatus.valueOf(DEFAULT_HTTP_STATUS_CODE).reasonPhrase());
                async.complete();
            });
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
