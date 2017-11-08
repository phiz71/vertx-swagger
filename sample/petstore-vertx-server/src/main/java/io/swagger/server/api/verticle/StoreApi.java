package io.swagger.server.api.verticle;

import io.swagger.server.api.MainApiException;
import io.swagger.server.api.MainApiHeader;
import io.swagger.server.api.model.Order;
import io.swagger.server.api.util.ResourceResponse;
import io.swagger.server.api.util.VerticleHelper;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.ext.auth.User;

import java.util.List;
import java.util.Map;

public interface StoreApi  {
    //deleteOrder
    void deleteOrder(Long orderId, Handler<AsyncResult<ResourceResponse<Void>>> handler);
    
    //getInventory
    void getInventory(User user, Handler<AsyncResult<ResourceResponse<Map<String, Integer>>>> handler);
    
    //getOrderById
    void getOrderById(Long orderId, Handler<AsyncResult<ResourceResponse<Order>>> handler);
    
    //placeOrder
    void placeOrder(Order body, Handler<AsyncResult<ResourceResponse<Order>>> handler);
    
}
