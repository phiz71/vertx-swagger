package com.github.phiz71.vertx.swagger.router.extractors;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.github.phiz71.vertx.swagger.router.SwaggerRouter;
import com.google.common.escape.Escaper;
import com.google.common.net.UrlEscapers;

import io.swagger.models.Swagger;
import io.swagger.parser.SwaggerParser;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.file.FileSystem;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import io.vertx.ext.web.Router;

@RunWith(VertxUnitRunner.class)
public class FormParameterExtractorTest {

    private static final int TEST_PORT = 9292;
    private static final String TEST_HOST = "localhost";
    private static final String TEST_FILENAME = "testUpload.json";
    private static Vertx vertx;
    private static EventBus eventBus;
    private static HttpClient httpClient;
    private static HttpServer httpServer;
    private static Escaper esc = UrlEscapers.urlFormParameterEscaper();

    @BeforeClass
    public static void beforeClass(TestContext context) {
        Async before = context.async();
        vertx = Vertx.vertx();
        eventBus = vertx.eventBus();

        // init Router
        FileSystem vertxFileSystem = vertx.fileSystem();
        vertxFileSystem.readFile("extractors/swaggerFormExtractor.json", readFile -> {
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
        eventBus.<JsonObject> consumer("POST_formdata_array_pipes").handler(message -> {
            message.reply(message.body().getJsonArray("array_formdata").encode());
        });
        eventBus.<JsonObject> consumer("POST_formdata_array_ssv").handler(message -> {
            message.reply(message.body().getJsonArray("array_formdata").encode());
        });
        eventBus.<JsonObject> consumer("POST_formdata_array_tsv").handler(message -> {
            message.reply(message.body().getJsonArray("array_formdata").encode());
        });
        eventBus.<JsonObject> consumer("POST_formdata_array_csv").handler(message -> {
            message.reply(message.body().getJsonArray("array_formdata").encode());
        });
        eventBus.<JsonObject> consumer("POST_formdata_array_multi").handler(message -> {
            message.reply(message.body().getJsonArray("array_formdata").encode());
        });
        eventBus.<JsonObject> consumer("POST_formdata_array").handler(message -> {
            message.reply(message.body().getJsonArray("array_formdata").encode());
        });
        eventBus.<JsonObject> consumer("POST_formdata_simple_required").handler(message -> {
            message.reply(message.body().getString("formDataRequired"));
        });
        eventBus.<JsonObject> consumer("POST_formdata_simple_required_allowempty").handler(message -> {
            message.reply(message.body().getString("formDataRequired"));
        });
        eventBus.<JsonObject> consumer("POST_formdata_simple_not_required").handler(message -> {
            message.reply(message.body().getString("formDataNotRequired"));
        });
        eventBus.<JsonObject> consumer("POST_formdata_simple_file").handler(message -> {
            vertxFileSystem.readFile(message.body().getString("formDataRequired"), readFile -> {
                if (readFile.succeeded()) {
                    message.reply(readFile.result().toString());
                } else {
                    context.fail(readFile.cause());
                }
            });
        });
        // init http client
        httpClient = Vertx.vertx().createHttpClient();

    }

    @Test()
    public void testOkFormDataSimpleRequired(TestContext context) {
        Async async = context.async();
        HttpClientRequest req = httpClient.post(TEST_PORT, TEST_HOST, "/formdata/simple/required");
        req.handler(response -> {
            response.bodyHandler(body -> {
                context.assertEquals(response.statusCode(), 200);
                context.assertEquals("toto", body.toString());
                async.complete();
            });
        });

        // Construct form
        StringBuffer payload = new StringBuffer().append("formDataRequired=").append(esc.escape("toto"));
        req.putHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded");
        req.end(payload.toString());
    }

    @Test()
    public void testKoFormDataSimpleRequiredWithEmptyValue(TestContext context) {
        Async async = context.async();
        HttpClientRequest req = httpClient.post(TEST_PORT, TEST_HOST, "/formdata/simple/required");
        req.handler(response -> {
            response.bodyHandler(body -> {
                context.assertEquals(response.statusCode(), 400);
                async.complete();
            });
        });
        // Construct form
        StringBuffer payload = new StringBuffer().append("formDataRequired=");
        req.putHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded");
        req.end(payload.toString());
    }

    @Test()
    public void testKoFormDataSimpleRequired(TestContext context) {
        Async async = context.async();
        HttpClientRequest req = httpClient.post(TEST_PORT, TEST_HOST, "/formdata/simple/required");
        req.handler(response -> {
            response.bodyHandler(body -> {
                context.assertEquals(response.statusCode(), 400);
                async.complete();
            });
        });
        req.putHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded");
        req.end();
    }

    @Test()
    public void testOkFormDataSimpleRequiredAllowEmpty(TestContext context) {
        Async async = context.async();
        HttpClientRequest req = httpClient.post(TEST_PORT, TEST_HOST, "/formdata/simple/required/allowempty");
        req.handler(response -> {
            response.bodyHandler(body -> {
                context.assertEquals(response.statusCode(), 200);
                context.assertEquals("", body.toString());
                async.complete();
            });
        });
        // Construct form
        StringBuffer payload = new StringBuffer().append("formDataRequired=");
        req.putHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded");
        req.end(payload.toString());
    }

    @Test()
    public void testOkFormDataSimpleNotRequiredWithParam(TestContext context) {
        Async async = context.async();
        HttpClientRequest req = httpClient.post(TEST_PORT, TEST_HOST, "/formdata/simple/not/required");
        req.handler(response -> {
            response.bodyHandler(body -> {
                context.assertEquals(response.statusCode(), 200);
                context.assertEquals("toto", body.toString());
                async.complete();
            });
        });

        // Construct form
        StringBuffer payload = new StringBuffer().append("formDataNotRequired=").append(esc.escape("toto"));
        req.putHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded");
        req.end(payload.toString());
    }

    @Test()
    public void testOkFormDataSimpleNotRequiredWithoutParam(TestContext context) {
        Async async = context.async();
        HttpClientRequest req = httpClient.post(TEST_PORT, TEST_HOST, "/formdata/simple/not/required");
        req.handler(response -> {
            response.bodyHandler(body -> {
                context.assertEquals(response.statusCode(), 200);
                context.assertEquals("", body.toString());
                async.complete();
            });
        });
        req.putHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded");
        req.end();
    }

    @Test()
    public void testOkFormDataArrayPipes(TestContext context) {
        Async async = context.async();
        HttpClientRequest req = httpClient.post(TEST_PORT, TEST_HOST, "/formdata/array/pipes");
        req.handler(response -> {
            response.bodyHandler(body -> {
                context.assertEquals(response.statusCode(), 200);
                context.assertEquals("[\"1\",\"2\",\"3\"]", body.toString());
                async.complete();
            });
        });

        // Construct form
        StringBuffer payload = new StringBuffer().append("array_formdata=").append(esc.escape("1|2|3"));
        req.putHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded");
        req.end(payload.toString());
    }

    @Test()
    public void testOkFormDataArrayCsv(TestContext context) {
        Async async = context.async();
        HttpClientRequest req = httpClient.post(TEST_PORT, TEST_HOST, "/formdata/array/csv");
        req.handler(response -> {
            response.bodyHandler(body -> {
                context.assertEquals(response.statusCode(), 200);
                context.assertEquals("[\"1\",\"2\",\"3\"]", body.toString());
                async.complete();
            });
        });

        // Construct form
        StringBuffer payload = new StringBuffer().append("array_formdata=").append(esc.escape("1,2,3"));
        req.putHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded");
        req.end(payload.toString());
    }

    @Test()
    public void testOkFormDataArraySsv(TestContext context) {
        Async async = context.async();
        HttpClientRequest req = httpClient.post(TEST_PORT, TEST_HOST, "/formdata/array/ssv");
        req.handler(response -> {
            response.bodyHandler(body -> {
                context.assertEquals(response.statusCode(), 200);
                context.assertEquals("[\"1\",\"2\",\"3\"]", body.toString());
                async.complete();
            });
        });

        // Construct form
        StringBuffer payload = new StringBuffer().append("array_formdata=").append(esc.escape("1 2 3"));
        req.putHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded");
        req.end(payload.toString());
    }

    @Test()
    public void testOkFormDataArrayTsv(TestContext context) {
        Async async = context.async();
        HttpClientRequest req = httpClient.post(TEST_PORT, TEST_HOST, "/formdata/array/tsv");
        req.handler(response -> {
            response.bodyHandler(body -> {
                context.assertEquals(response.statusCode(), 200);
                context.assertEquals("[\"1\",\"2\",\"3\"]", body.toString());
                async.complete();
            });
        });

        // Construct form
        StringBuffer payload = new StringBuffer().append("array_formdata=").append(esc.escape("1\t2\t3"));
        req.putHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded");
        req.end(payload.toString());
    }

    @Test()
    public void testOkQueryArrayMulti(TestContext context) throws UnsupportedEncodingException {
        Async async = context.async();
        HttpClientRequest req = httpClient.post(TEST_PORT, TEST_HOST, "/formdata/array/multi?array_formdata=1&array_formdata=2&array_formdata=3");
        req.handler(response -> {
            response.bodyHandler(body -> {
                context.assertEquals(response.statusCode(), 200);
                context.assertEquals("[\"1\",\"2\",\"3\"]", body.toString());
                async.complete();
            });
        });

        // Construct form
        StringBuffer payload = new StringBuffer().append("array_formdata=").append(esc.escape("1")).append("&array_formdata=").append(esc.escape("2")).append("&array_formdata=")
                .append(esc.escape("3"));
        req.putHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded");
        req.end(payload.toString());
    }

    @Test()
    public void testOkFormDataArray(TestContext context) throws UnsupportedEncodingException {
        Async async = context.async();
        HttpClientRequest req = httpClient.post(TEST_PORT, TEST_HOST, "/formdata/array");
        req.handler(response -> {
            response.bodyHandler(body -> {
                context.assertEquals(response.statusCode(), 200);
                context.assertEquals("[\"1\",\"2\",\"3\"]", body.toString());
                async.complete();
            });
        });

        // Construct form
        StringBuffer payload = new StringBuffer().append("array_formdata=").append(esc.escape("1,2,3"));
        req.putHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded");
        req.end(payload.toString());
    }

    @Test()
    public void testOkFormDataSimpleFile(TestContext context) {
        Async async = context.async();
        HttpClientRequest req = httpClient.post(TEST_PORT, TEST_HOST, "/formdata/simple/file");
        req.handler(response -> {
            response.bodyHandler(body -> {
                context.assertEquals(response.statusCode(), 200);
                context.assertEquals("{\"test\":\"This is a test file.\"}", body.toString());
                async.complete();
            });
        });
        // Construct multipart data
        req.putHeader(HttpHeaders.CONTENT_TYPE, "multipart/form-data; boundary=MyBoundary");
        Buffer buffer = Buffer.factory.buffer();
        FileSystem vertxFileSystem = vertx.fileSystem();
        vertxFileSystem.readFile(TEST_FILENAME, readFile -> {
            if (readFile.succeeded()) {
                buffer.appendString("\r\n");
                buffer.appendString("--MyBoundary\r\n");
                buffer.appendString("Content-Disposition: form-data; name=\"formDataRequired\"; filename=\"" + TEST_FILENAME + "\"\r\n");
                buffer.appendString("Content-Type: text/plain\r\n");
                buffer.appendString("\r\n");
                buffer.appendString(readFile.result().toString(Charset.forName("utf-8")));
                buffer.appendString("\r\n");
                buffer.appendString("--MyBoundary--");
                req.end(buffer);
            } else {
                context.fail(readFile.cause());
            }
        });
    }

    @Test()
    public void testKoFormDataSimpleFileWithoutFile(TestContext context) {
        Async async = context.async();
        HttpClientRequest req = httpClient.post(TEST_PORT, TEST_HOST, "/formdata/simple/file");
        req.handler(response -> {
            response.bodyHandler(body -> {
                context.assertEquals(response.statusCode(), 400);
                async.complete();
            });
        });
        // Construct multipart data
        req.putHeader(HttpHeaders.CONTENT_TYPE, "multipart/form-data; boundary=MyBoundary");
        req.end();
    }

    @AfterClass
    public static void afterClass(TestContext context) {
        Async after = context.async();
        httpServer.close(completionHandler -> {
            if (completionHandler.succeeded()) {
                FileSystem vertxFileSystem = vertx.fileSystem();
                vertxFileSystem.deleteRecursive("file-uploads", true, deletedDir -> {
                    if (deletedDir.succeeded()) {
                        vertxFileSystem.deleteRecursive(".vertx", true, vertxDir -> {
                            if (vertxDir.succeeded()) {
                                after.complete();
                            } else {
                                context.fail(vertxDir.cause());
                            }
                        });
                    } else {
                        context.fail(deletedDir.cause());
                    }
                });
            }
        });

    }

}
