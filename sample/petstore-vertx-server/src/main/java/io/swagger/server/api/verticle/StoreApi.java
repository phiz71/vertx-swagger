package io.swagger.server.api.verticle;

import io.swagger.server.api.MainApiException;
import io.swagger.server.api.model.Order;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.ext.auth.User;

import java.util.List;
import java.util.Map;

public interface StoreApi  {
    //deleteOrder
    void deleteOrder(Long orderId, Handler<AsyncResult<Void>> handler);
    
    //getInventory
    void getInventory(User user, Handler<AsyncResult<Map<String, Integer>>> handler);
    
    //getOrderById
    void getOrderById(Long orderId, Handler<AsyncResult<Order>> handler);
    
    //placeOrder
    void placeOrder(Order body, Handler<AsyncResult<Order>> handler);
    
}
