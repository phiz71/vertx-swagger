package com.github.phiz71.vertx.swagger.router.extractors;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.github.phiz71.vertx.swagger.router.SwaggerRouter;
import com.github.phiz71.vertx.swagger.router.model.BodyType;

import io.swagger.models.Swagger;
import io.swagger.parser.SwaggerParser;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.file.FileSystem;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import io.vertx.ext.web.Router;

@RunWith(VertxUnitRunner.class)
public class BodyParameterExtractorTest {

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
        vertxFileSystem.readFile("extractors/swaggerBodyExtractor.json", readFile -> {
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
        eventBus.<JsonObject> consumer("POST_body_required").handler(message -> {
            if("application/xml".equals(message.headers().get(HttpHeaders.CONTENT_TYPE))) {
                message.reply(message.body().getString("bodyRequired"));
            } else {
                try {
                    BodyType body = Json.mapper.readValue(message.body().getJsonObject("bodyRequired").encode(), BodyType.class);
                    message.reply(Json.encode(body));
                } catch (Exception e) {
                    e.printStackTrace();
                    message.fail(500, e.getMessage());
                }
            }
        });
        eventBus.<JsonObject> consumer("POST_body_not_required").handler(message -> {
            if("application/xml".equals(message.headers().get(HttpHeaders.CONTENT_TYPE))) {
                message.reply(message.body().getString("bodyNotRequired"));
            } else {
                try {
                    BodyType body = null;
                    if (message.body().getJsonObject("bodyNotRequired") != null) {
                        body = Json.mapper.readValue(message.body().getJsonObject("bodyNotRequired").encode(), BodyType.class);
                    }
                    message.reply(Json.encode(body));
                } catch (Exception e) {
                    e.printStackTrace();
                    message.fail(500, e.getMessage());
                }
            }
        });
        eventBus.<JsonObject> consumer("POST_body_array").handler(message -> {
            if("application/xml".equals(message.headers().get(HttpHeaders.CONTENT_TYPE))) {
                message.reply(message.body().getString("arrayBody"));
            } else {
                try {
                    List<BodyType> bodys = Json.mapper.readValue(message.body().getJsonArray("arrayBody").encode(),
                            Json.mapper.getTypeFactory().constructCollectionType(List.class, BodyType.class));
                    message.reply(Json.encode(bodys));
                } catch (Exception e) {
                    e.printStackTrace();
                    message.fail(500, e.getMessage());
                }
            }
        });
        // init http client
        httpClient = Vertx.vertx().createHttpClient();

    }

    @Test
    public void testOkAddBodyRequired(TestContext context) {
        Async async = context.async();
        BodyType bodyReq = new BodyType(1L, "body 1");
        HttpClientRequest req = httpClient.post(TEST_PORT, TEST_HOST, "/body/required");
        req.handler(response -> {
            context.assertEquals(response.statusCode(), 200);
            response.bodyHandler(body -> {
                context.assertEquals(Json.encode(bodyReq), body.toString());
                async.complete();
            });

        }).end(Json.encode(bodyReq));
    }

    @Test
    public void testOkAddBodyRequiredWithXML(TestContext context) {
        Async async = context.async();
        String bodyReqXml = "<BodyType><id>1</id><name>body 1</name></BodyType>";
        HttpClientRequest req = httpClient.post(TEST_PORT, TEST_HOST, "/body/required");
        req.handler(response -> {
            context.assertEquals(response.statusCode(), 200);
            response.bodyHandler(body -> {
                context.assertEquals(bodyReqXml, body.toString());
                async.complete();
            });

        })
        .putHeader(HttpHeaders.CONTENT_TYPE, "application/xml")
        .end(bodyReqXml);
    }

    @Test
    public void testKoAddBodyRequired(TestContext context) {
        Async async = context.async();
        HttpClientRequest req = httpClient.post(TEST_PORT, TEST_HOST, "/body/required");
        req.handler(response -> {
            context.assertEquals(response.statusCode(), 400);
            async.complete();
        }).end();
    }

    @Test()
    public void testOkAddBodyNotRequiredWithBody(TestContext context) {
        Async async = context.async();
        BodyType bodyReq = new BodyType(1L, "body 1");
        HttpClientRequest req = httpClient.post(TEST_PORT, TEST_HOST, "/body/not/required");
        req.handler(response -> {
            context.assertEquals(response.statusCode(), 200);
            response.bodyHandler(bodyHandler -> {
                context.assertEquals(Json.encode(bodyReq), bodyHandler.toString());
                async.complete();
            });
        }).end(Json.encode(bodyReq));
    }

    @Test
    public void testOkAddBodyNotRequiredWithEmptyBody(TestContext context) {
        Async async = context.async();
        HttpClientRequest req = httpClient.post(TEST_PORT, TEST_HOST, "/body/not/required");
        req.handler(response -> {
            context.assertEquals(response.statusCode(), 200);
            response.bodyHandler(bodyHandler -> {
                context.assertEquals(Json.encode(new JsonObject()), bodyHandler.toString());
                async.complete();
            });
        }).end(Json.encode(new JsonObject()));
    }

    @Test
    public void testOkAddBodyNotRequiredWithoutBody(TestContext context) {
        Async async = context.async();
        HttpClientRequest req = httpClient.post(TEST_PORT, TEST_HOST, "/body/not/required");
        req.handler(response -> {
            context.assertEquals(response.statusCode(), 200);
            response.bodyHandler(bodyHandler -> {
                context.assertEquals("null", bodyHandler.toString());
                async.complete();
            });
        }).end();
    }

    @Test
    public void testOkAddArrayBody(TestContext context) {
        Async async = context.async();
        BodyType body1 = new BodyType(1L, "body 1");
        BodyType body2 = new BodyType(2L, "body 2");
        JsonArray bodys = new JsonArray(Arrays.asList(body1, body2));
        HttpClientRequest req = httpClient.post(TEST_PORT, TEST_HOST, "/body/array");
        req.handler(response -> {
            context.assertEquals(response.statusCode(), 200);
            response.bodyHandler(body -> {
                context.assertEquals(Json.encode(bodys), body.toString());
                async.complete();
            });

        }).end(Json.encode(bodys));
    }

    
    @Test
    public void testOkAddArrayBodyWithXML(TestContext context) {
        Async async = context.async();
        String bodyReqXml = "<BodyType><id>1</id><name>body 1</name></BodyType><BodyType><id>2</id><name>body 2</name></BodyType>";
        HttpClientRequest req = httpClient.post(TEST_PORT, TEST_HOST, "/body/array");
        req.handler(response -> {
            context.assertEquals(response.statusCode(), 200);
            response.bodyHandler(body -> {
                context.assertEquals(bodyReqXml, body.toString());
                async.complete();
            });

        })
        .putHeader(HttpHeaders.CONTENT_TYPE, "application/xml")
        .end(bodyReqXml);
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
