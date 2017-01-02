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
    
    final static String DELETE_STORE_ORDER_ORDERID_SERVICE_ID = "DELETE_store_order_orderId";
    final static String GET_STORE_INVENTORY_SERVICE_ID = "GET_store_inventory";
    final static String GET_STORE_ORDER_ORDERID_SERVICE_ID = "GET_store_order_orderId";
    final static String POST_STORE_ORDER_SERVICE_ID = "POST_store_order";
    
    
    //TODO : create Implementation
    StoreApi service = new StoreApiImpl();

    @Override
    public void start() throws Exception {
        
        //Consumer for DELETE_store_order_orderId
        vertx.eventBus().<JsonObject> consumer(DELETE_STORE_ORDER_ORDERID_SERVICE_ID).handler(message -> {
            try {
                
                Long orderId = Json.mapper.readValue(message.body().getJsonObject("orderId").encode(), Long.class);
                
                
                //TODO: call implementation
                
                service.deleteOrder(orderId);
                message.reply(null);
                
            } catch (Exception e) {
                //TODO : replace magic number (101)
                message.fail(101, e.getLocalizedMessage());
            }
        });
        
        //Consumer for GET_store_inventory
        vertx.eventBus().<JsonObject> consumer(GET_STORE_INVENTORY_SERVICE_ID).handler(message -> {
            try {
                
                
                //TODO: call implementation
                
                Map<String, Integer> result = service.getInventory();
                
                message.reply(new JsonObject(Json.encode(result)));
                
            } catch (Exception e) {
                //TODO : replace magic number (101)
                message.fail(101, e.getLocalizedMessage());
            }
        });
        
        //Consumer for GET_store_order_orderId
        vertx.eventBus().<JsonObject> consumer(GET_STORE_ORDER_ORDERID_SERVICE_ID).handler(message -> {
            try {
                
                Long orderId = Json.mapper.readValue(message.body().getJsonObject("orderId").encode(), Long.class);
                
                
                //TODO: call implementation
                
                Order result = service.getOrderById(orderId);
                
                message.reply(new JsonObject(Json.encode(result)));
                
            } catch (Exception e) {
                //TODO : replace magic number (101)
                message.fail(101, e.getLocalizedMessage());
            }
        });
        
        //Consumer for POST_store_order
        vertx.eventBus().<JsonObject> consumer(POST_STORE_ORDER_SERVICE_ID).handler(message -> {
            try {
                
                Order body = Json.mapper.readValue(message.body().getJsonObject("body").encode(), Order.class);
                
                
                //TODO: call implementation
                
                Order result = service.placeOrder(body);
                
                message.reply(new JsonObject(Json.encode(result)));
                
            } catch (Exception e) {
                //TODO : replace magic number (101)
                message.fail(101, e.getLocalizedMessage());
            }
        });
        
    }
}
