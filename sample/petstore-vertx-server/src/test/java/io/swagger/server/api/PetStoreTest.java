package io.swagger.server.api;

import java.util.ArrayList;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.swagger.server.api.model.Category;
import io.swagger.server.api.model.Pet;
import io.swagger.server.api.model.Pet.StatusEnum;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;

@RunWith(VertxUnitRunner.class)
public class PetStoreTest {

    private static final int TEST_PORT = 8080;
    private static final String TEST_HOST = "localhost";
    private static Vertx vertx;
    private static HttpClient httpClient;
    private static Pet dog;

    @BeforeClass
    public static void beforeClass(TestContext context) {
        Async before = context.async();
        vertx = Vertx.vertx();
        dog = new Pet(1L, new Category(1L, "dog"), "rex", new ArrayList<>(), new ArrayList<>(), StatusEnum.AVAILABLE);

        // init Main
        vertx.deployVerticle("io.swagger.server.api.MainApiVerticle", res -> {
            if (res.succeeded()) {
                before.complete();
            } else {
                context.fail(res.cause());
            }
        });

        httpClient = Vertx.vertx().createHttpClient();

    }

    @Test(timeout = 2000)
    public void testFindByStatus(TestContext context) {
        Async async = context.async();
        httpClient.getNow(TEST_PORT, TEST_HOST, "/pet/findByStatus?status=available", response -> {
            response.bodyHandler(body -> {
                JsonArray jsonArray = new JsonArray(body.toString());
                context.assertTrue(jsonArray.size() == 1);
                try {
                    Pet resultDog = Json.mapper.readValue(jsonArray.getJsonObject(0).encode(), Pet.class);
                    context.assertEquals(dog, resultDog);
                } catch (Exception e) {
                    context.fail(e);
                }
                async.complete();
            });
            response.exceptionHandler(err -> {
                context.fail(err);
            });
        });
    }
    
    @Test(timeout = 2000)
    public void testGetPetById(TestContext context) {
        Async async = context.async();
        httpClient.getNow(TEST_PORT, TEST_HOST, "/pet/1", response -> {
            response.bodyHandler(body -> {
                JsonObject jsonObject = new JsonObject(body.toString());
                try {
                    Pet resultDog = Json.mapper.readValue(jsonObject.encode(), Pet.class);
                    context.assertEquals(dog, resultDog);
                } catch (Exception e) {
                    context.fail(e);
                }
                async.complete();
            });
            response.exceptionHandler(err -> {
                context.fail(err);
            });
        });
    }

}
