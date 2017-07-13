package com.github.phiz71.rxjava.vertx.swagger.router;

import com.github.phiz71.rxjava.vertx.swagger.router.SwaggerRouter;
import io.swagger.models.Swagger;
import io.swagger.parser.SwaggerParser;

import io.vertx.core.json.JsonArray;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import io.vertx.rxjava.core.Vertx;
import io.vertx.rxjava.core.buffer.Buffer;
import io.vertx.rxjava.core.eventbus.EventBus;
import io.vertx.rxjava.core.file.FileSystem;
import io.vertx.rxjava.ext.web.Router;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.nio.charset.Charset;

@RunWith(VertxUnitRunner.class)
public class SwaggerRouterTest {
    private static Vertx vertx;
    private static EventBus eventBus;
    private static JsonArray definitions;

    @BeforeClass
    public static void beforeClass(TestContext context) {
        Async async = context.async();
        vertx = Vertx.vertx();
        eventBus = vertx.eventBus();

        FileSystem vertxFileSystem = vertx.fileSystem();
        vertxFileSystem.readFile("swaggerRouterTest.json", readFile -> {
            try {
                if (readFile.succeeded()) {
                    final Buffer buffer = readFile.result();
                    definitions = new JsonArray(buffer.toString(Charset.forName("utf-8").toString()));
                } else {
                    context.fail(readFile.cause());
                }
            } catch (Throwable cause) {
                context.fail(cause);
            } finally {
                async.complete();
            }
        });
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalPathParameterName() {
        String definition = definitions.getJsonObject(0).encode();
        Swagger swagger = new SwaggerParser().parse(definition);
        SwaggerRouter.swaggerRouter(Router.router(vertx), swagger, eventBus);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyPathParameterName() {
        String definition = definitions.getJsonObject(1).encode();
        Swagger swagger = new SwaggerParser().parse(definition);
        SwaggerRouter.swaggerRouter(Router.router(vertx), swagger, eventBus);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNumericPathParameterName() {
        String definition = definitions.getJsonObject(2).encode();
        Swagger swagger = new SwaggerParser().parse(definition);
        SwaggerRouter.swaggerRouter(Router.router(vertx), swagger, eventBus);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPathParameterNameStartsWithNumber() {
        String definition = definitions.getJsonObject(3).encode();
        Swagger swagger = new SwaggerParser().parse(definition);
        SwaggerRouter.swaggerRouter(Router.router(vertx), swagger, eventBus);
    }

    @Test
    public void testAlphaNumericPathParameterName() {
        String definition = definitions.getJsonObject(4).encode();
        Swagger swagger = new SwaggerParser().parse(definition);
        SwaggerRouter.swaggerRouter(Router.router(vertx), swagger, eventBus);
    }

}