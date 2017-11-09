package io.swagger.server.api.verticle;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;

import io.swagger.server.api.model.Order;
import io.swagger.server.api.model.Order.StatusEnum;
import io.swagger.server.api.util.ResourceResponse;
import io.vertx.rxjava.ext.auth.User;
import rx.Single;

public class StoreApiImpl implements StoreApi {

    @Override
    public Single<ResourceResponse<Void>> deleteOrder(Long orderId) {
        ResourceResponse<Void> response = new ResourceResponse<>();
        response.addHeader(StoreApiHeader.CONTENT_TYPE_JSON);
        return Single.just(response);
    }

    @Override
    public Single<ResourceResponse<Map<String, Integer>>> getInventory(User user) {
        ResourceResponse<Map<String, Integer>> response = new ResourceResponse<>();
        response.addHeader(StoreApiHeader.CONTENT_TYPE_JSON);
        response.setResponse(new HashMap<>());
        return Single.just(response);
    }

    @Override
    public Single<ResourceResponse<Order>> getOrderById(Long orderId) {
        ResourceResponse<Order> response = new ResourceResponse<>();
        response.addHeader(StoreApiHeader.CONTENT_TYPE_JSON);
        response.setResponse(new Order(1L, 1L, 3, OffsetDateTime.of(2017,4,2,11,8,10,0,ZoneOffset.UTC), StatusEnum.APPROVED, Boolean.TRUE));
        return Single.just(response);
    }

    @Override
    public Single<ResourceResponse<Order>> placeOrder(Order body) {
        ResourceResponse<Order> response = new ResourceResponse<>();
        response.addHeader(StoreApiHeader.CONTENT_TYPE_JSON);
        response.setResponse(new Order());
        return Single.just(response);
    }

}
