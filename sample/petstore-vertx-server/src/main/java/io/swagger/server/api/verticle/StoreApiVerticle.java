package io.swagger.server.api.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

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
                
            } catch (Exception e) {
                //TODO : replace magic number (101)
                message.fail(101, e.getLocalizedMessage());
            }
        });
        
        //Consumer for getInventory
        vertx.eventBus().<JsonObject> consumer(GETINVENTORY_SERVICE_ID).handler(message -> {
            try {
                
             
                Map<String, Integer> result = service.getInventory();
                
                message.reply(new JsonObject(Json.encode(result)).encodePrettily());
                
            } catch (Exception e) {
                //TODO : replace magic number (101)
                message.fail(101, e.getLocalizedMessage());
            }
        });
        
        //Consumer for getOrderById
        vertx.eventBus().<JsonObject> consumer(GETORDERBYID_SERVICE_ID).handler(message -> {
            try {
                
                                Long orderId = Json.mapper.readValue(message.body().getString("orderId"), Long.class);
                
             
                Order result = service.getOrderById(orderId);
                
                message.reply(new JsonObject(Json.encode(result)).encodePrettily());
                
            } catch (Exception e) {
                //TODO : replace magic number (101)
                message.fail(101, e.getLocalizedMessage());
            }
        });
        
        //Consumer for placeOrder
        vertx.eventBus().<JsonObject> consumer(PLACEORDER_SERVICE_ID).handler(message -> {
            try {
                
                                Order body = Json.mapper.readValue(message.body().getJsonObject("body").encode(), Order.class);
                
             
                Order result = service.placeOrder(body);
                
                message.reply(new JsonObject(Json.encode(result)).encodePrettily());
                
            } catch (Exception e) {
                //TODO : replace magic number (101)
                message.fail(101, e.getLocalizedMessage());
            }
        });
        
    }
}
