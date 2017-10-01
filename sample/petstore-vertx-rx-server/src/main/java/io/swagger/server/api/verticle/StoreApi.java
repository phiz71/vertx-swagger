package io.swagger.server.api.verticle;

import io.swagger.server.api.util.MainApiException;
import io.swagger.server.api.util.MainApiHeader;
import io.swagger.server.api.model.Order;
import io.swagger.server.api.util.ResourceResponse;

import rx.Completable;
import rx.Single;
import io.vertx.rxjava.ext.auth.User;

import java.util.List;
import java.util.Map;

public interface StoreApi  {
    //deleteOrder
    Single<ResourceResponse<Void>> deleteOrder(Long orderId);
    
    //getInventory
    Single<ResourceResponse<Map<String, Integer>>> getInventory(User user);
    
    //getOrderById
    Single<ResourceResponse<Order>> getOrderById(Long orderId);
    
    //placeOrder
    Single<ResourceResponse<Order>> placeOrder(Order body);
    
}
