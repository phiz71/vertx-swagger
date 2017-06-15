package io.swagger.server.api.verticle;


import io.swagger.server.api.MainApiException;
import io.swagger.server.api.model.Order;
import rx.Single;

import java.util.List;
import java.util.Map;

public interface StoreApi  {
    //deleteOrder
    public Single<Void> deleteOrder(Long orderId);
    
    //getInventory
    public Single<Map<String, Integer>> getInventory();
    
    //getOrderById
    public Single<Order> getOrderById(Long orderId);
    
    //placeOrder
    public Single<Order> placeOrder(Order body);
    
}
