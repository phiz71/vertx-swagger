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
                
                service.deleteOrder(orderId, result -> {
                    if (result.succeeded())
                        message.reply(null);
                    else {
                        Throwable cause = result.cause();

                        int code = MainApiException.INTERNAL_SERVER_ERROR.getStatusCode();
                        String statusMessage = MainApiException.INTERNAL_SERVER_ERROR.getStatusMessage();
                        if (cause instanceof MainApiException) {
                            code = ((MainApiException)cause).getStatusCode();
                            statusMessage = ((MainApiException)cause).getStatusMessage();
                        }

                        message.fail(code, statusMessage);
                    }
                });
            } catch (Exception e) {
                LOGGER.error("Error in "+DELETEORDER_SERVICE_ID, e);
                message.fail(MainApiException.INTERNAL_SERVER_ERROR.getStatusCode(), MainApiException.INTERNAL_SERVER_ERROR.getStatusMessage());
            }
        });
        
        //Consumer for getInventory
        vertx.eventBus().<JsonObject> consumer(GETINVENTORY_SERVICE_ID).handler(message -> {
            try {
                
                service.getInventory(result -> {
                    if (result.succeeded())
                        message.reply(new JsonObject(Json.encode(result.result())).encodePrettily());
                    else {
                        Throwable cause = result.cause();

                        int code = MainApiException.INTERNAL_SERVER_ERROR.getStatusCode();
                        String statusMessage = MainApiException.INTERNAL_SERVER_ERROR.getStatusMessage();
                        if (cause instanceof MainApiException) {
                            code = ((MainApiException)cause).getStatusCode();
                            statusMessage = ((MainApiException)cause).getStatusMessage();
                        }

                        message.fail(code, statusMessage);
                    }
                });
            } catch (Exception e) {
                LOGGER.error("Error in "+GETINVENTORY_SERVICE_ID, e);
                message.fail(MainApiException.INTERNAL_SERVER_ERROR.getStatusCode(), MainApiException.INTERNAL_SERVER_ERROR.getStatusMessage());
            }
        });
        
        //Consumer for getOrderById
        vertx.eventBus().<JsonObject> consumer(GETORDERBYID_SERVICE_ID).handler(message -> {
            try {
                Long orderId = Json.mapper.readValue(message.body().getString("OrderId"), Long.class);
                
                service.getOrderById(orderId, result -> {
                    if (result.succeeded())
                        message.reply(new JsonObject(Json.encode(result.result())).encodePrettily());
                    else {
                        Throwable cause = result.cause();

                        int code = MainApiException.INTERNAL_SERVER_ERROR.getStatusCode();
                        String statusMessage = MainApiException.INTERNAL_SERVER_ERROR.getStatusMessage();
                        if (cause instanceof MainApiException) {
                            code = ((MainApiException)cause).getStatusCode();
                            statusMessage = ((MainApiException)cause).getStatusMessage();
                        }

                        message.fail(code, statusMessage);
                    }
                });
            } catch (Exception e) {
                LOGGER.error("Error in "+GETORDERBYID_SERVICE_ID, e);
                message.fail(MainApiException.INTERNAL_SERVER_ERROR.getStatusCode(), MainApiException.INTERNAL_SERVER_ERROR.getStatusMessage());
            }
        });
        
        //Consumer for placeOrder
        vertx.eventBus().<JsonObject> consumer(PLACEORDER_SERVICE_ID).handler(message -> {
            try {
                Order body = Json.mapper.readValue(message.body().getJsonObject("body").encode(), Order.class);
                
                service.placeOrder(body, result -> {
                    if (result.succeeded())
                        message.reply(new JsonObject(Json.encode(result.result())).encodePrettily());
                    else {
                        Throwable cause = result.cause();

                        int code = MainApiException.INTERNAL_SERVER_ERROR.getStatusCode();
                        String statusMessage = MainApiException.INTERNAL_SERVER_ERROR.getStatusMessage();
                        if (cause instanceof MainApiException) {
                            code = ((MainApiException)cause).getStatusCode();
                            statusMessage = ((MainApiException)cause).getStatusMessage();
                        }

                        message.fail(code, statusMessage);
                    }
                });
            } catch (Exception e) {
                LOGGER.error("Error in "+PLACEORDER_SERVICE_ID, e);
                message.fail(MainApiException.INTERNAL_SERVER_ERROR.getStatusCode(), MainApiException.INTERNAL_SERVER_ERROR.getStatusMessage());
            }
        });
        
    }
}