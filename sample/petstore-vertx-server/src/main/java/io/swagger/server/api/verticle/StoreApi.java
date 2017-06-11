package io.swagger.server.api.verticle;

import io.swagger.server.api.MainApiException;
import io.swagger.server.api.model.Order;

import java.util.List;
import java.util.Map;

public interface StoreApi  {
    //deleteOrder
    public void deleteOrder(Long orderId) throws StoreApiException;
    
    //getInventory
    public Map<String, Integer> getInventory() throws StoreApiException;
    
    //getOrderById
    public Order getOrderById(Long orderId) throws StoreApiException;
    
    //placeOrder
    public Order placeOrder(Order body) throws StoreApiException;
    
}
