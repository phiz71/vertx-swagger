package io.swagger.server.api.verticle;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;

import io.swagger.server.api.model.Order;
import io.swagger.server.api.model.Order.StatusEnum;
import io.vertx.rxjava.ext.auth.User;
import rx.Completable;
import rx.Single;

public class StoreApiImpl implements StoreApi {

    @Override
    public Completable deleteOrder(Long orderId) {
        return Completable.complete();
    }

    @Override
    public Single<Map<String, Integer>> getInventory(User user) {
        return Single.just(new HashMap<>());
    }

    @Override
    public Single<Order> getOrderById(Long orderId) {
        return Single.just(new Order(1L, 1L, 3, OffsetDateTime.of(2017,4,2,11,8,10,0,ZoneOffset.UTC), StatusEnum.APPROVED, Boolean.TRUE));
    }

    @Override
    public Single<Order> placeOrder(Order body) {
        return Single.just(new Order());
    }

}
