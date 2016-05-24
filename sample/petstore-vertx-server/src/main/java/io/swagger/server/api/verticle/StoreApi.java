package io.swagger.server.api.verticle;

import io.swagger.server.api.model.Order;

import java.util.List;
import java.util.Map;

public interface StoreApi  {
    //DELETE_store_order_orderId
    public void deleteOrder(Long orderId);
    
    //GET_store_inventory
    public Map<String, Integer> getInventory();
    
    //GET_store_order_orderId
    public Order getOrderById(Long orderId);
    
    //POST_store_order
    public Order placeOrder(Order body);
    
}
