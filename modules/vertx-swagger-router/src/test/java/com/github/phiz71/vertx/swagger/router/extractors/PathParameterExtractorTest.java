package com.github.phiz71.vertx.swagger.router.extractors;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.github.phiz71.vertx.swagger.router.SwaggerRouter;

import io.swagger.models.Swagger;
import io.swagger.parser.SwaggerParser;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.file.FileSystem;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import io.vertx.ext.web.Router;

@RunWith(VertxUnitRunner.class)
public class PathParameterExtractorTest {

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
        vertxFileSystem.readFile("extractors/swaggerPathExtractor.json", readFile -> {
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
        eventBus.<JsonObject> consumer("GET_path_array_pipes_array_id").handler(message -> {
            message.reply(message.body().getJsonArray("array_id").encode());
        });
        eventBus.<JsonObject> consumer("GET_path_array_ssv_array_id").handler(message -> {
            message.reply(message.body().getJsonArray("array_id").encode());
        });
        eventBus.<JsonObject> consumer("GET_path_array_tsv_array_id").handler(message -> {
            message.reply(message.body().getJsonArray("array_id").encode());
        });
        eventBus.<JsonObject> consumer("GET_path_array_csv_array_id").handler(message -> {
            message.reply(message.body().getJsonArray("array_id").encode());
        });
        eventBus.<JsonObject> consumer("GET_path_array_array_id").handler(message -> {
            message.reply(message.body().getJsonArray("array_id").encode());
        });
        eventBus.<JsonObject> consumer("GET_path_simple_id").handler(message -> {
            message.reply(message.body().getString("id"));
        });

        // init http client
        httpClient = Vertx.vertx().createHttpClient();

    }

    @Test()
    public void testOkPathSimple(TestContext context) {
        Async async = context.async();
        HttpClientRequest req = httpClient.get(TEST_PORT, TEST_HOST, "/path/simple/1");
        req.handler(response -> {
            response.bodyHandler(body -> {
                context.assertEquals(response.statusCode(), 200);
                context.assertEquals("1", body.toString());
                async.complete();
            });
        }).end();
    }

    @Test()
    public void testOkPathArrayPipes(TestContext context) throws UnsupportedEncodingException {
        Async async = context.async();
        HttpClientRequest req = httpClient.get(TEST_PORT, TEST_HOST, "/path/array/pipes/"+URLEncoder.encode("1|2|3", "UTF-8"));
        req.handler(response -> {
            response.bodyHandler(body -> {
                context.assertEquals(response.statusCode(), 200);
                context.assertEquals("[\"1\",\"2\",\"3\"]", body.toString());
                async.complete();
            });
        }).end();
    }

    @Test()
    public void testOkPathArrayCsv(TestContext context) throws UnsupportedEncodingException {
        Async async = context.async();
        HttpClientRequest req = httpClient.get(TEST_PORT, TEST_HOST, "/path/array/csv/"+URLEncoder.encode("1,2,3", "UTF-8"));
        req.handler(response -> {
            response.bodyHandler(body -> {
                context.assertEquals(response.statusCode(), 200);
                context.assertEquals("[\"1\",\"2\",\"3\"]", body.toString());
                async.complete();
            });
        }).end();
    } 
    
    @Test(timeout=1000000000)
    public void testOkPathArraySsv(TestContext context) throws UnsupportedEncodingException {
        Async async = context.async();
        HttpClientRequest req = httpClient.get(TEST_PORT, TEST_HOST, "/path/array/ssv/"+URLEncoder.encode("1 2 3", "UTF-8"));
        req.handler(response -> {
            response.bodyHandler(body -> {
                context.assertEquals(response.statusCode(), 200);
                context.assertEquals("[\"1\",\"2\",\"3\"]", body.toString());
                async.complete();
            });
        }).end();
    }
    
    @Test()
    public void testOkPathArrayTsv(TestContext context) throws UnsupportedEncodingException {
        Async async = context.async();
        HttpClientRequest req = httpClient.get(TEST_PORT, TEST_HOST, "/path/array/tsv/"+URLEncoder.encode("1\t2\t3", "UTF-8"));
        req.handler(response -> {
            response.bodyHandler(body -> {
                context.assertEquals(response.statusCode(), 200);
                context.assertEquals("[\"1\",\"2\",\"3\"]", body.toString());
                async.complete();
            });
        }).end();
    }
    
    @Test()
    public void testOkPathArray(TestContext context) throws UnsupportedEncodingException {
        Async async = context.async();
        HttpClientRequest req = httpClient.get(TEST_PORT, TEST_HOST, "/path/array/"+URLEncoder.encode("1,2,3", "UTF-8"));
        req.handler(response -> {
            response.bodyHandler(body -> {
                context.assertEquals(response.statusCode(), 200);
                context.assertEquals("[\"1\",\"2\",\"3\"]", body.toString());
                async.complete();
            });
        }).end();
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
