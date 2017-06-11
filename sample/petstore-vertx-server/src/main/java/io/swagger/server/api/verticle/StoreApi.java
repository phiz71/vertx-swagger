package io.swagger.server.api.verticle;


import io.swagger.server.api.MainApiException;
import io.swagger.server.api.model.Order;
import io.vertx.core.Future;

import java.util.List;
import java.util.Map;

public interface StoreApi  {
    //deleteOrder
    public void deleteOrder(Long orderId) throws StoreApiException;
    
    //getInventory
    public Future<Map<String, Integer>> getInventory() throws StoreApiException;
    
    //getOrderById
    public Future<Order> getOrderById(Long orderId) throws StoreApiException;
    
    //placeOrder
    public Future<Order> placeOrder(Order body) throws StoreApiException;
    
}
