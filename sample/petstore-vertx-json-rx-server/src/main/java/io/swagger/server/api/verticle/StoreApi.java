package io.swagger.server.api.verticle;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.swagger.server.api.MainApiException;
import io.swagger.server.api.MainApiHeader;
import io.swagger.server.api.model.Order;
import io.swagger.server.api.util.ResourceResponse;
import io.swagger.server.api.util.VerticleHelper;

import rx.Completable;
import rx.Single;
import io.vertx.rxjava.ext.auth.User;

public interface StoreApi  {
    //deleteOrder
    Single<ResourceResponse<Void>> deleteOrder(Long orderId);
    
    //getInventory
    Single<ResourceResponse<JsonObject>> getInventory(User user);
    
    //getOrderById
    Single<ResourceResponse<Order>> getOrderById(Long orderId);
    
    //placeOrder
    Single<ResourceResponse<Order>> placeOrder(Order body);
    
}
