package io.swagger.server.api.verticle;

import io.swagger.server.api.model.Order;
import io.swagger.server.api.util.ResourceResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.rxjava.ext.auth.User;
import rx.Single;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;

public class StoreApiImpl implements StoreApi {

    @Override
    public Single<ResourceResponse<Void>> deleteOrder(Long orderId) {
        ResourceResponse<Void> response = new ResourceResponse<>();
        response.addHeader(StoreApiHeader.CONTENT_TYPE_JSON);
        return Single.just(response);
    }

    @Override
    public Single<ResourceResponse<JsonObject>> getInventory(User user) {
        ResourceResponse<JsonObject> response = new ResourceResponse<>();
        response.addHeader(StoreApiHeader.CONTENT_TYPE_JSON);
        response.setResponse(new JsonObject());
        return Single.just(response);
    }

    @Override
    public Single<ResourceResponse<Order>> getOrderById(Long orderId) {
        ResourceResponse<Order> response = new ResourceResponse<>();
        response.addHeader(StoreApiHeader.CONTENT_TYPE_JSON);
        response.setResponse(new Order(1L, 1L, 3, Instant.parse("2017-04-02T11:08:10Z"), Order.OrderStatus.APPROVED, Boolean.TRUE));
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
