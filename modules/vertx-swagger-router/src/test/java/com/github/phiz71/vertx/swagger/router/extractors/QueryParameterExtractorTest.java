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
public class QueryParameterExtractorTest {

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
        vertxFileSystem.readFile("extractors/swaggerQueryExtractor.json", readFile -> {
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
        eventBus.<JsonObject> consumer("GET_query_array_pipes").handler(message -> {
            message.reply(message.body().getJsonArray("array_query").encode());
        });
        eventBus.<JsonObject> consumer("GET_query_array_ssv").handler(message -> {
            message.reply(message.body().getJsonArray("array_query").encode());
        });
        eventBus.<JsonObject> consumer("GET_query_array_tsv").handler(message -> {
            message.reply(message.body().getJsonArray("array_query").encode());
        });
        eventBus.<JsonObject> consumer("GET_query_array_csv").handler(message -> {
            message.reply(message.body().getJsonArray("array_query").encode());
        });
        eventBus.<JsonObject> consumer("GET_query_array_multi").handler(message -> {
            message.reply(message.body().getJsonArray("array_query").encode());
        });
        eventBus.<JsonObject> consumer("GET_query_array").handler(message -> {
            message.reply(message.body().getJsonArray("array_query").encode());
        });
        eventBus.<JsonObject> consumer("GET_query_simple_required").handler(message -> {
            message.reply(message.body().getString("queryRequired"));
        });
        eventBus.<JsonObject> consumer("GET_query_simple_required_allowempty").handler(message -> {
            message.reply(message.body().getString("queryRequired"));
        });
        eventBus.<JsonObject> consumer("GET_query_simple_not_required").handler(message -> {
            message.reply(message.body().getString("queryNotRequired"));
        });
        eventBus.<JsonObject> consumer("GET_query_simple_not_required_withdefault").handler(message -> {
            message.reply(message.body().getString("queryNotRequired"));
        });
        // init http client
        httpClient = Vertx.vertx().createHttpClient();

    }

    @Test()
    public void testOkQuerySimpleRequired(TestContext context) {
        Async async = context.async();
        HttpClientRequest req = httpClient.get(TEST_PORT, TEST_HOST, "/query/simple/required?queryRequired=toto");
        req.handler(response -> {
            response.bodyHandler(body -> {
                context.assertEquals(response.statusCode(), 200);
                context.assertEquals("toto", body.toString());
                async.complete();
            });
        }).end();
    }

    @Test()
    public void testKoQuerySimpleRequiredWithEmptyValue(TestContext context) {
        Async async = context.async();
        HttpClientRequest req = httpClient.get(TEST_PORT, TEST_HOST, "/query/simple/required?queryRequired=");
        req.handler(response -> {
            response.bodyHandler(body -> {
                context.assertEquals(response.statusCode(), 400);
                async.complete();
            });
        }).end();
    }

    @Test()
    public void testOkQuerySimpleRequiredAllowEmpty(TestContext context) {
        Async async = context.async();
        HttpClientRequest req = httpClient.get(TEST_PORT, TEST_HOST, "/query/simple/required/allowempty?queryRequired=");
        req.handler(response -> {
            response.bodyHandler(body -> {
                context.assertEquals(response.statusCode(), 200);
                context.assertEquals("", body.toString());
                async.complete();
            });
        }).end();
    }
    
    @Test()
    public void testKoQuerySimpleRequired(TestContext context) {
        Async async = context.async();
        HttpClientRequest req = httpClient.get(TEST_PORT, TEST_HOST, "/query/simple/required");
        req.handler(response -> {
            response.bodyHandler(body -> {
                context.assertEquals(response.statusCode(), 400);
                async.complete();
            });
        }).end();
    }
    
    @Test()
    public void testOkQuerySimpleNotRequiredWithParam(TestContext context) {
        Async async = context.async();
        HttpClientRequest req = httpClient.get(TEST_PORT, TEST_HOST, "/query/simple/not/required?queryNotRequired=toto");
        req.handler(response -> {
            response.bodyHandler(body -> {
                context.assertEquals(response.statusCode(), 200);
                context.assertEquals("toto", body.toString());
                async.complete();
            });
        }).end();
    }

    @Test()
    public void testOkQuerySimpleNotRequiredWithoutParam(TestContext context) {
        Async async = context.async();
        HttpClientRequest req = httpClient.get(TEST_PORT, TEST_HOST, "/query/simple/not/required");
        req.handler(response -> {
            response.bodyHandler(body -> {
                context.assertEquals(response.statusCode(), 200);
                context.assertEquals("", body.toString());
                async.complete();
            });
        }).end();
    }
    
    @Test()
    public void testOkQueryArrayPipes(TestContext context) throws UnsupportedEncodingException {
        Async async = context.async();
        HttpClientRequest req = httpClient.get(TEST_PORT, TEST_HOST, "/query/array/pipes?array_query="+URLEncoder.encode("1|2|3", "UTF-8"));
        req.handler(response -> {
            response.bodyHandler(body -> {
                context.assertEquals(response.statusCode(), 200);
                context.assertEquals("[\"1\",\"2\",\"3\"]", body.toString());
                async.complete();
            });
        }).end();
    }

    @Test()
    public void testOkQueryArrayCsv(TestContext context) throws UnsupportedEncodingException {
        Async async = context.async();
        HttpClientRequest req = httpClient.get(TEST_PORT, TEST_HOST, "/query/array/csv?array_query="+URLEncoder.encode("1,2,3", "UTF-8"));
        req.handler(response -> {
            response.bodyHandler(body -> {
                context.assertEquals(response.statusCode(), 200);
                context.assertEquals("[\"1\",\"2\",\"3\"]", body.toString());
                async.complete();
            });
        }).end();
    } 
    
    @Test()
    public void testOkQueryArraySsv(TestContext context) throws UnsupportedEncodingException {
        Async async = context.async();
        HttpClientRequest req = httpClient.get(TEST_PORT, TEST_HOST, "/query/array/ssv?array_query="+URLEncoder.encode("1 2 3", "UTF-8"));
        req.handler(response -> {
            response.bodyHandler(body -> {
                context.assertEquals(response.statusCode(), 200);
                context.assertEquals("[\"1\",\"2\",\"3\"]", body.toString());
                async.complete();
            });
        }).end();
    }
    
    @Test()
    public void testOkQueryArrayTsv(TestContext context) throws UnsupportedEncodingException {
        Async async = context.async();
        HttpClientRequest req = httpClient.get(TEST_PORT, TEST_HOST, "/query/array/tsv?array_query="+URLEncoder.encode("1\t2\t3", "UTF-8"));
        req.handler(response -> {
            response.bodyHandler(body -> {
                context.assertEquals(response.statusCode(), 200);
                context.assertEquals("[\"1\",\"2\",\"3\"]", body.toString());
                async.complete();
            });
        }).end();
    }
 
    @Test()
    public void testOkQueryArrayMulti(TestContext context) throws UnsupportedEncodingException {
        Async async = context.async();
        HttpClientRequest req = httpClient.get(TEST_PORT, TEST_HOST, "/query/array/multi?array_query=1&array_query=2&array_query=3");
        req.handler(response -> {
            response.bodyHandler(body -> {
                context.assertEquals(response.statusCode(), 200);
                context.assertEquals("[\"1\",\"2\",\"3\"]", body.toString());
                async.complete();
            });
        }).end();
    }
    
    @Test()
    public void testOkQueryArray(TestContext context) throws UnsupportedEncodingException {
        Async async = context.async();
        HttpClientRequest req = httpClient.get(TEST_PORT, TEST_HOST, "/query/array?array_query="+URLEncoder.encode("1,2,3", "UTF-8"));
        req.handler(response -> {
            response.bodyHandler(body -> {
                context.assertEquals(response.statusCode(), 200);
                context.assertEquals("[\"1\",\"2\",\"3\"]", body.toString());
                async.complete();
            });
        }).end();
    }

    @Test()
    public void testOkQuerySimpleNotRequiredWithDefaultWithParam(TestContext context) {
        Async async = context.async();
        HttpClientRequest req = httpClient.get(TEST_PORT, TEST_HOST, "/query/simple/not/required/withdefault?queryNotRequired=foo");
        req.handler(response -> {
            response.bodyHandler(body -> {
                context.assertEquals(response.statusCode(), 200);
                context.assertEquals("foo", body.toString());
                async.complete();
            });
        }).end();
    }

    @Test()
    public void testOkQuerySimpleNotRequiredWithDefaultWithoutParam(TestContext context) {
        Async async = context.async();
        HttpClientRequest req = httpClient.get(TEST_PORT, TEST_HOST, "/query/simple/not/required/withdefault");
        req.handler(response -> {
            response.bodyHandler(body -> {
                context.assertEquals(response.statusCode(), 200);
                context.assertEquals("defaultValue", body.toString());
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
