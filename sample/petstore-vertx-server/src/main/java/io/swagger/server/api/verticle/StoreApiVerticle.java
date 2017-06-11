package io.swagger.server.api.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import io.swagger.server.api.MainApiException;
import io.swagger.server.api.model.Order;

import java.util.List;
import java.util.Map;

public class StoreApiVerticle extends AbstractVerticle {
    final static Logger LOGGER = LoggerFactory.getLogger(StoreApiVerticle.class); 
    
    final static String DELETEORDER_SERVICE_ID = "deleteOrder";
    final static String GETINVENTORY_SERVICE_ID = "getInventory";
    final static String GETORDERBYID_SERVICE_ID = "getOrderById";
    final static String PLACEORDER_SERVICE_ID = "placeOrder";
    
    //TODO : create Implementation
    StoreApi service = new StoreApiImpl();

    @Override
    public void start() throws Exception {
        
        //Consumer for deleteOrder
        vertx.eventBus().<JsonObject> consumer(DELETEORDER_SERVICE_ID).handler(message -> {
            try {
                Long orderId = Json.mapper.readValue(message.body().getString("orderId"), Long.class);
                
                service.deleteOrder(orderId);
                message.reply(null);
            } catch (StoreApiException e) {
                message.fail(e.getStatusCode(), e.getStatusMessage());
            } catch (Exception e) {
                LOGGER.error("Error in "+DELETEORDER_SERVICE_ID, e);
                message.fail(MainApiException.INTERNAL_SERVER_ERROR.getStatusCode(), MainApiException.INTERNAL_SERVER_ERROR.getStatusMessage());
            }
        });
        
        //Consumer for getInventory
        vertx.eventBus().<JsonObject> consumer(GETINVENTORY_SERVICE_ID).handler(message -> {
            try {
                
                service.getInventory().setHandler(futureResult -> {
                    if(futureResult.succeeded()) {
                        message.reply(new JsonObject(Json.encode(futureResult.result())).encodePrettily());
                    } else {
                        LOGGER.error("Error in "+GETINVENTORY_SERVICE_ID, futureResult.cause());
                        message.fail(MainApiException.INTERNAL_SERVER_ERROR.getStatusCode(), MainApiException.INTERNAL_SERVER_ERROR.getStatusMessage());
                    }
                });
            } catch (StoreApiException e) {
                message.fail(e.getStatusCode(), e.getStatusMessage());
            } catch (Exception e) {
                LOGGER.error("Error in "+GETINVENTORY_SERVICE_ID, e);
                message.fail(MainApiException.INTERNAL_SERVER_ERROR.getStatusCode(), MainApiException.INTERNAL_SERVER_ERROR.getStatusMessage());
            }
        });
        
        //Consumer for getOrderById
        vertx.eventBus().<JsonObject> consumer(GETORDERBYID_SERVICE_ID).handler(message -> {
            try {
                Long orderId = Json.mapper.readValue(message.body().getString("OrderId"), Long.class);
                
                service.getOrderById(orderId).setHandler(futureResult -> {
                    if(futureResult.succeeded()) {
                        message.reply(new JsonObject(Json.encode(futureResult.result())).encodePrettily());
                    } else {
                        LOGGER.error("Error in "+GETORDERBYID_SERVICE_ID, futureResult.cause());
                        message.fail(MainApiException.INTERNAL_SERVER_ERROR.getStatusCode(), MainApiException.INTERNAL_SERVER_ERROR.getStatusMessage());
                    }
                });
            } catch (StoreApiException e) {
                message.fail(e.getStatusCode(), e.getStatusMessage());
            } catch (Exception e) {
                LOGGER.error("Error in "+GETORDERBYID_SERVICE_ID, e);
                message.fail(MainApiException.INTERNAL_SERVER_ERROR.getStatusCode(), MainApiException.INTERNAL_SERVER_ERROR.getStatusMessage());
            }
        });
        
        //Consumer for placeOrder
        vertx.eventBus().<JsonObject> consumer(PLACEORDER_SERVICE_ID).handler(message -> {
            try {
                Order body = Json.mapper.readValue(message.body().getJsonObject("body").encode(), Order.class);
                
                service.placeOrder(body).setHandler(futureResult -> {
                    if(futureResult.succeeded()) {
                        message.reply(new JsonObject(Json.encode(futureResult.result())).encodePrettily());
                    } else {
                        LOGGER.error("Error in "+PLACEORDER_SERVICE_ID, futureResult.cause());
                        message.fail(MainApiException.INTERNAL_SERVER_ERROR.getStatusCode(), MainApiException.INTERNAL_SERVER_ERROR.getStatusMessage());
                    }
                });
            } catch (StoreApiException e) {
                message.fail(e.getStatusCode(), e.getStatusMessage());
            } catch (Exception e) {
                LOGGER.error("Error in "+PLACEORDER_SERVICE_ID, e);
                message.fail(MainApiException.INTERNAL_SERVER_ERROR.getStatusCode(), MainApiException.INTERNAL_SERVER_ERROR.getStatusMessage());
            }
        });
        
    }
}