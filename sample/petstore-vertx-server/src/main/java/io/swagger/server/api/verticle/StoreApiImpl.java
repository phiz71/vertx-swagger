package io.swagger.server.api.verticle;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Map;

import io.swagger.server.api.model.Order;
import io.swagger.server.api.model.Order.StatusEnum;

public class StoreApiImpl implements StoreApi {

    @Override
    public void deleteOrder(Long orderId) {

    }

    @Override
    public Map<String, Integer> getInventory() {
        return null;
    }

    @Override
    public Order getOrderById(Long orderId) {
        return new Order(1L, 1L, 3, OffsetDateTime.of(2017, 4, 2, 11, 8, 10, 0, ZoneOffset.UTC), StatusEnum.APPROVED, Boolean.TRUE);
    }

    @Override
    public Order placeOrder(Order body) {
        return null;
    }

}
