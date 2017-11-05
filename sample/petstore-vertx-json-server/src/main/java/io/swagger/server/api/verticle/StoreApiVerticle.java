package io.swagger.server.api.verticle;

import com.fasterxml.jackson.core.type.TypeReference;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.Json;
import io.vertx.ext.auth.User;
import com.github.phiz71.vertx.swagger.router.SwaggerRouter;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.swagger.server.api.MainApiException;
import io.swagger.server.api.MainApiHeader;
import io.swagger.server.api.model.Order;
import io.swagger.server.api.util.ResourceResponse;
import io.swagger.server.api.util.VerticleHelper;

public class StoreApiVerticle extends AbstractVerticle {
    private VerticleHelper verticleHelper = new VerticleHelper(this.getClass());

    private static final String DELETEORDER_SERVICE_ID = "deleteOrder";
    private static final String GETINVENTORY_SERVICE_ID = "getInventory";
    private static final String GETORDERBYID_SERVICE_ID = "getOrderById";
    private static final String PLACEORDER_SERVICE_ID = "placeOrder";
    

    private StoreApi service = createServiceImplementation();

    //Handler for deleteOrder
    final Handler<Message<JsonObject>> deleteOrderHandler = message -> {
        try {
            Long orderId = Json.mapper.readValue(message.body().getString("orderId"), Long.class);
            service.deleteOrder(orderId, verticleHelper.getAsyncResultHandler(message, DELETEORDER_SERVICE_ID, false, new TypeReference<Void>(){}));

        } catch (Exception e) {
            verticleHelper.manageError(message, e, DELETEORDER_SERVICE_ID);
        }
    };
    //Handler for getInventory
    final Handler<Message<JsonObject>> getInventoryHandler = message -> {
        try {
            User user = SwaggerRouter.extractAuthUserFromMessage(message);
            service.getInventory(user, verticleHelper.getAsyncResultHandler(message, GETINVENTORY_SERVICE_ID, true, new TypeReference<JsonObject>(){}));

        } catch (Exception e) {
            verticleHelper.manageError(message, e, GETINVENTORY_SERVICE_ID);
        }
    };
    //Handler for getOrderById
    final Handler<Message<JsonObject>> getOrderByIdHandler = message -> {
        try {
            Long orderId = Json.mapper.readValue(message.body().getString("OrderId"), Long.class);
            service.getOrderById(orderId, verticleHelper.getAsyncResultHandler(message, GETORDERBYID_SERVICE_ID, true, new TypeReference<Order>(){}));

        } catch (Exception e) {
            verticleHelper.manageError(message, e, GETORDERBYID_SERVICE_ID);
        }
    };
    //Handler for placeOrder
    final Handler<Message<JsonObject>> placeOrderHandler = message -> {
        try {
            Order body = new Order(message.body().getJsonObject("body"));
            service.placeOrder(body, verticleHelper.getAsyncResultHandler(message, PLACEORDER_SERVICE_ID, true, new TypeReference<Order>(){}));

        } catch (Exception e) {
            verticleHelper.manageError(message, e, PLACEORDER_SERVICE_ID);
        }
    };
    

    @Override
    public void start() throws Exception {
    vertx.eventBus().<JsonObject> consumer(DELETEORDER_SERVICE_ID).handler(deleteOrderHandler);
    vertx.eventBus().<JsonObject> consumer(GETINVENTORY_SERVICE_ID).handler(getInventoryHandler);
    vertx.eventBus().<JsonObject> consumer(GETORDERBYID_SERVICE_ID).handler(getOrderByIdHandler);
    vertx.eventBus().<JsonObject> consumer(PLACEORDER_SERVICE_ID).handler(placeOrderHandler);
    
    }

    protected StoreApi createServiceImplementation() {
        return new StoreApiImpl();
    }
}