package com.github.phiz71.vertx.swagger.router;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeoutException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.fasterxml.jackson.databind.type.TypeFactory;
import com.github.phiz71.vertx.swagger.router.SwaggerRouter;
import com.github.phiz71.vertx.swagger.router.model.Category;
import com.github.phiz71.vertx.swagger.router.model.Pet;
import com.github.phiz71.vertx.swagger.router.model.User;
import com.github.phiz71.vertx.swagger.router.model.Pet.StatusEnum;
import com.google.common.escape.Escaper;
import com.google.common.net.UrlEscapers;

import io.swagger.models.Swagger;
import io.swagger.parser.SwaggerParser;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.file.FileSystem;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import io.vertx.ext.web.Router;

@RunWith(VertxUnitRunner.class)
public class BuildRouterTest {

    private static final int TEST_PORT = 9292;
    private static final String TEST_HOST = "localhost";
    private static final String TEST_FILENAME = "testUpload.json";
    private static Vertx vertx;
    private static EventBus eventBus;
    private static HttpClient httpClient;
    private static Pet cat, dog, bird;

    @BeforeClass
    public static void beforeClass(TestContext context) {
        Async before = context.async();
        vertx = Vertx.vertx();
        eventBus = vertx.eventBus();
        cat = new Pet(1L, new Category(1L, "CAT"), "kitty", new ArrayList<>(), new ArrayList<>(), StatusEnum.AVAILABLE);
        dog = new Pet(2L, new Category(2L, "DOG"), "rex", new ArrayList<>(), new ArrayList<>(), StatusEnum.PENDING);
        bird = new Pet(3L, new Category(3L, "BIRD"), "twetty", new ArrayList<>(), new ArrayList<>(), StatusEnum.SOLD);

        // init Router
        FileSystem vertxFileSystem = vertx.fileSystem();
        vertxFileSystem.readFile("swagger.json", readFile -> {
            if (readFile.succeeded()) {
                Swagger swagger = new SwaggerParser().parse(readFile.result().toString(Charset.forName("utf-8")));
                Router swaggerRouter = SwaggerRouter.swaggerRouter(Router.router(vertx), swagger, eventBus);
                vertx.createHttpServer().requestHandler(swaggerRouter::accept).listen(TEST_PORT, TEST_HOST, listen -> {
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
        eventBus.<JsonObject> consumer("GET_pet_petId").handler(message -> {
            String petId = message.body().getString("petId");
            message.reply(new JsonObject().put("petId_received", petId).encode());
        });
        eventBus.<JsonObject> consumer("POST_pet_petId").handler(message -> {
            String petId = message.body().getString("petId");
            String name = message.body().getString("name");
            String status = message.body().getString("status");
            message.reply(new JsonObject().put("petId_received", petId).put("name_received", name).put("status_received", status).encode());
        });
        eventBus.<JsonObject> consumer("DELETE_pet_petId").handler(message -> {
            String petId = message.body().getString("petId");
            String apiKey = message.body().getString("api_key");
            message.reply(new JsonObject().put("petId_received", petId).put("header_received", apiKey).encode());
        });
        eventBus.<JsonObject> consumer("POST_pet_petId_uploadImage").handler(message -> {
            JsonObject result = new JsonObject();
            String petId = message.body().getString("petId");
            String additionalMetadata = message.body().getString("additionalMetadata");
            String fileName = message.body().getString("file");
            vertxFileSystem.readFile(fileName, readFile -> {
                if (readFile.succeeded()) {
                    result.put("fileContent_received", readFile.result().toString());
                    result.put("petId_received", petId);
                    result.put("additionalMetadata_received", additionalMetadata);
                    message.reply(result.encode());
                } else {
                    context.fail(readFile.cause());
                }
            });
        });
        eventBus.<JsonObject> consumer("GET_user_login").handler(message -> {
            String username = message.body().getString("username");
            message.reply(new JsonObject().put("username_received", username).encode());
        });
        eventBus.<JsonObject> consumer("GET_pet_findByStatus").handler(message -> {
            JsonArray status = message.body().getJsonArray("status");
            JsonArray result = new JsonArray();
            for (int i = 0; i < status.size(); i++) {
                if (cat.getStatus().toString().equals(status.getString(i))) {
                    result.add(new JsonObject(Json.encode(cat)));
                }
                if (dog.getStatus().toString().equals(status.getString(i))) {
                    result.add(new JsonObject(Json.encode(dog)));
                }
                if (bird.getStatus().toString().equals(status.getString(i))) {
                    result.add(new JsonObject(Json.encode(bird)));
                }
            }
            message.reply(result.encode());
        });
        eventBus.<JsonObject> consumer("POST_user_createWithArray").handler(message -> {
            try {
                List<User> users = Json.mapper.readValue(message.body().getString("body"), TypeFactory.defaultInstance().constructCollectionType(List.class, User.class));
                JsonObject result = new JsonObject();
                for (int i = 0; i < users.size(); i++) {
                    result.put("user " + (i + 1), users.get(i).toString());
                }
                message.reply(result.encode());
            } catch (Exception e) {
                message.fail(500, e.getLocalizedMessage());
            }
        });
        eventBus.<JsonObject> consumer("GET_user_logout").handler(message -> {
            message.reply(null);
        });
        eventBus.<JsonObject> consumer("GET_user_username").handler(message -> {
            DeliveryOptions options = new DeliveryOptions();
            options.addHeader("Header_KEY", "Header_VALUE");
            message.reply("", options);
        });

        // init http Server
        HttpClientOptions options = new HttpClientOptions();
        options.setDefaultPort(TEST_PORT);
        httpClient = Vertx.vertx().createHttpClient();

    }

    @Test(timeout = 2000)
    public void testResourceNotfound(TestContext context) {
        Async async = context.async();
        httpClient.getNow(TEST_PORT, TEST_HOST, "/dummy", response -> {
            context.assertEquals(response.statusCode(), 404);
            async.complete();
        });

    }

    @Test(timeout = 2000)
    public void testMessageIsConsume(TestContext context) {
        Async async = context.async();
        httpClient.getNow(TEST_PORT, TEST_HOST, "/store/inventory", response -> {
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
        httpClient.getNow(TEST_PORT, TEST_HOST, "/user/logout", response -> response.exceptionHandler(err -> {
            async.complete();
        }));
    }

    @Test(timeout = 2000)
    public void testWithPathParameter(TestContext context) {
        Async async = context.async();
        httpClient.getNow(TEST_PORT, TEST_HOST, "/pet/5", response -> {
            response.bodyHandler(body -> {
                JsonObject jsonBody = new JsonObject(body.toString(Charset.forName("utf-8")));
                context.assertTrue(jsonBody.containsKey("petId_received"));
                context.assertEquals("5", jsonBody.getString("petId_received"));
                async.complete();
            });
        });
    }

    @Test(timeout = 2000)
    public void testWithQuerySimpleParameter(TestContext context) {
        Async async = context.async();
        httpClient.getNow(TEST_PORT, TEST_HOST, "/user/login?username=myUser&password=mySecret", response -> {
            response.bodyHandler(body -> {
                JsonObject jsonBody = new JsonObject(body.toString(Charset.forName("utf-8")));
                context.assertTrue(jsonBody.containsKey("username_received"));
                context.assertEquals("myUser", jsonBody.getString("username_received"));
                async.complete();
            });
        });
    }

    @Test(timeout = 2000)
    public void testWithQueryArrayParameter(TestContext context) {
        Async async = context.async();
        httpClient.getNow(TEST_PORT, TEST_HOST, "/pet/findByStatus?status=available", response -> {
            response.bodyHandler(body -> {
                JsonArray jsonArray = new JsonArray(body.toString(Charset.forName("utf-8")));
                context.assertTrue(jsonArray.size() == 1);
                try {
                    Pet resultCat = Json.mapper.readValue(jsonArray.getJsonObject(0).encode(), Pet.class);
                    context.assertEquals(cat, resultCat);
                } catch (Exception e) {
                    context.fail(e);
                }
                async.complete();
            });
        });
    }

    @Test(timeout = 2000)
    public void testWithQueryManyArrayParameter(TestContext context) {
        Async async = context.async();
        httpClient.getNow(TEST_PORT, TEST_HOST, "/pet/findByStatus?status=available&status=pending", response -> {
            response.bodyHandler(body -> {
                JsonArray jsonArray = new JsonArray(body.toString(Charset.forName("utf-8")));
                context.assertTrue(jsonArray.size() == 2);
                try {
                    Pet resultCat = Json.mapper.readValue(jsonArray.getJsonObject(0).encode(), Pet.class);
                    Pet resultDog = Json.mapper.readValue(jsonArray.getJsonObject(1).encode(), Pet.class);
                    context.assertEquals(cat, resultCat);
                    context.assertEquals(dog, resultDog);
                } catch (Exception e) {
                    context.fail(e);
                }

                async.complete();
            });
        });
    }

    @Test(timeout = 2000)
    public void testWithBodyParameterNoBody(TestContext context) {
        Async async = context.async();
        HttpClientRequest req = httpClient.post(TEST_PORT, TEST_HOST, "/user/createWithArray");
        req.handler(response -> {
            context.assertEquals(response.statusCode(), 400);
            async.complete();
        }).end();
    }

    @Test(timeout = 2000)
    public void testWithBodyParameter(TestContext context) {
        Async async = context.async();
        User user1 = new User(1L, "user 1", "first 1", "last 1", "email 1", "secret 1", "phone 1", 1);
        User user2 = new User(2L, "user 2", "first 2", "last 2", "email 2", "secret 2", "phone 2", 2);
        JsonArray users = new JsonArray(Arrays.asList(user1, user2));
        HttpClientRequest req = httpClient.post(TEST_PORT, TEST_HOST, "/user/createWithArray");
        req.setChunked(true);
        req.handler(response -> response.bodyHandler(result -> {
            JsonObject jsonBody = new JsonObject(result.toString(Charset.forName("utf-8")));
            context.assertTrue(jsonBody.containsKey("user 1"));
            context.assertEquals(user1.toString(), jsonBody.getString("user 1"));
            context.assertTrue(jsonBody.containsKey("user 2"));
            context.assertEquals(user2.toString(), jsonBody.getString("user 2"));
            async.complete();
        })).end(users.encode());
    }

    @Test(timeout = 2000)
    public void testNullBodyResponse(TestContext context) {
        Async async = context.async();
        httpClient.getNow(TEST_PORT, TEST_HOST, "/user/logout", response -> {
            context.assertEquals(response.statusCode(), 200);
            async.complete();
        });
    }

    @Test(timeout = 2000)
    public void testWithFormParameterMultiPart(TestContext context) {
        Async async = context.async();
        HttpClientRequest req = httpClient.post(TEST_PORT, TEST_HOST, "/pet/1/uploadImage");
        req.setChunked(false);
        req.handler(response -> response.bodyHandler(result -> {
            JsonObject jsonBody = new JsonObject(result.toString(Charset.forName("utf-8")));
            context.assertTrue(jsonBody.containsKey("petId_received"));
            context.assertEquals("1", jsonBody.getString("petId_received"));
            context.assertTrue(jsonBody.containsKey("additionalMetadata_received"));
            context.assertEquals("Exceptionnal file !!", jsonBody.getString("additionalMetadata_received"));
            context.assertTrue(jsonBody.containsKey("fileContent_received"));
            context.assertEquals("{\"test\":\"This is a test file.\"}", jsonBody.getString("fileContent_received"));
            async.complete();
        }));

        // Construct multipart data
        req.putHeader(HttpHeaders.CONTENT_TYPE, "multipart/form-data; boundary=MyBoundary");
        Buffer buffer = Buffer.factory.buffer();
        buffer.appendString("\r\n");
        buffer.appendString("--MyBoundary\r\n");
        buffer.appendString("Content-Disposition: form-data; name=\"additionalMetadata\" \r\n");
        buffer.appendString("\r\n");
        buffer.appendString("Exceptionnal file !!");
        FileSystem vertxFileSystem = vertx.fileSystem();
        vertxFileSystem.readFile(TEST_FILENAME, readFile -> {
            if (readFile.succeeded()) {
                buffer.appendString("\r\n");
                buffer.appendString("--MyBoundary\r\n");
                buffer.appendString("Content-Disposition: form-data; name=\"file\"; filename=\"" + TEST_FILENAME + "\"\r\n");
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

    @Test(timeout = 2000)
    public void testWithFormParameterUrlEncoded(TestContext context) {
        Async async = context.async();
        HttpClientRequest req = httpClient.post(TEST_PORT, TEST_HOST, "/pet/1");

        req.handler(response -> response.bodyHandler(result -> {
            JsonObject jsonBody = new JsonObject(result.toString(Charset.forName("utf-8")));
            context.assertTrue(jsonBody.containsKey("petId_received"));
            context.assertEquals("1", jsonBody.getString("petId_received"));
            context.assertTrue(jsonBody.containsKey("name_received"));
            context.assertEquals("MyName", jsonBody.getString("name_received"));
            context.assertTrue(jsonBody.containsKey("status_received"));
            context.assertEquals("MyStatus", jsonBody.getString("status_received"));
            async.complete();
        }));

        // Construct form
        Escaper esc = UrlEscapers.urlFormParameterEscaper();

        StringBuffer payload = new StringBuffer().append("name=").append(esc.escape("MyName")).append("&status=").append(esc.escape("MyStatus"));

        req.putHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded");
        req.end(payload.toString());

    }

    @Test(timeout = 2000)
    public void testWithHeaderParameter(TestContext context) {
        Async async = context.async();
        HttpClientRequest req = httpClient.delete(TEST_PORT, TEST_HOST, "/pet/1");
        req.putHeader("api_key", "MyAPIKey");
        req.handler(response -> response.bodyHandler(result -> {
            JsonObject jsonBody = new JsonObject(result.toString(Charset.forName("utf-8")));
            context.assertTrue(jsonBody.containsKey("petId_received"));
            context.assertEquals("1", jsonBody.getString("petId_received"));
            context.assertTrue(jsonBody.containsKey("header_received"));
            context.assertEquals("MyAPIKey", jsonBody.getString("header_received"));
            async.complete();
        }));
        req.end();

    }
    
    @Test(timeout = 2000)
    public void testWithReturnedHeaderParameter(TestContext context) {
        Async async = context.async();
        httpClient.getNow(TEST_PORT, TEST_HOST, "/user/username", response -> { 
            String header = response.getHeader("Header_KEY");
            context.assertNotNull(header);
            context.assertEquals("Header_VALUE", header);
            async.complete();
        });
    }
    
    @AfterClass
    public static void afterClass(TestContext context) {
        Async after = context.async();
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

}
