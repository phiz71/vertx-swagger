package io.swagger.server.api.verticle;

import io.swagger.server.api.MainApiException;
import io.swagger.server.api.model.Order;

import rx.Completable;
import rx.Single;
import io.vertx.rxjava.ext.auth.User;

import java.util.List;
import java.util.Map;

public interface StoreApi  {
    //deleteOrder
    public Completable deleteOrder(Long orderId);
    
    //getInventory
    public Single<Map<String, Integer>> getInventory(User user);
    
    //getOrderById
    public Single<Order> getOrderById(Long orderId);
    
    //placeOrder
    public Single<Order> placeOrder(Order body);
    
}
