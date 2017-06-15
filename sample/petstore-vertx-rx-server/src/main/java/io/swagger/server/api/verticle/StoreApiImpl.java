package io.swagger.server.api.verticle;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Map;

import io.swagger.server.api.model.Order;
import io.swagger.server.api.model.Order.StatusEnum;
import rx.Single;

public class StoreApiImpl implements StoreApi {

    @Override
    public Single<Void> deleteOrder(Long orderId) {
        return Single.just(null);
    }

    @Override
    public Single<Map<String, Integer>> getInventory() {
        return Single.just(null);
    }

    @Override
    public Single<Order> getOrderById(Long orderId) {
        return Single.just(new Order(1L, 1L, 3, OffsetDateTime.of(2017,4,2,11,8,10,0,ZoneOffset.UTC), StatusEnum.APPROVED, Boolean.TRUE));
    }

    @Override
    public Single<Order> placeOrder(Order body) {
        return Single.just(null);
    }

}
