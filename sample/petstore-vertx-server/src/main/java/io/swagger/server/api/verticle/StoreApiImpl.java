package io.swagger.server.api.verticle;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Map;

import io.swagger.server.api.model.Order;
import io.swagger.server.api.model.Order.StatusEnum;
import io.vertx.core.Future;

public class StoreApiImpl implements StoreApi {

    @Override
    public void deleteOrder(Long orderId) {
        // TODO Auto-generated method stub

    }

    @Override
    public Future<Map<String, Integer>> getInventory() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Future<Order> getOrderById(Long orderId) {
        Future<Order> futureResult = Future.future();
        futureResult.complete(new Order(1L, 1L, 3, OffsetDateTime.of(2017,4,2,11,8,10,0,ZoneOffset.UTC), StatusEnum.APPROVED, Boolean.TRUE));
        return futureResult;
    }

    @Override
    public Future<Order> placeOrder(Order body) {
        // TODO Auto-generated method stub
        return null;
    }

}
