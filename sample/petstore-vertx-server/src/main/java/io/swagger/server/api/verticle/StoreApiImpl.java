package io.swagger.server.api.verticle;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Map;

import io.swagger.server.api.model.Order;
import io.swagger.server.api.model.Order.StatusEnum;
import io.swagger.server.api.util.ResourceResponse;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.ext.auth.User;

public class StoreApiImpl implements StoreApi {

    @Override
    public void deleteOrder(Long orderId, Handler<AsyncResult<ResourceResponse<Void>>> handler) {
        // TODO Auto-generated method stub

    }

    @Override
    public void getInventory(User user, Handler<AsyncResult<ResourceResponse<Map<String, Integer>>>> handler) {
        // TODO Auto-generated method stub
        handler.handle(Future.succeededFuture(null));
    }

    @Override
    public void getOrderById(Long orderId, Handler<AsyncResult<ResourceResponse<Order>>> handler) {
        ResourceResponse<Order> response = new ResourceResponse<>();
        response.addHeader(StoreApiHeader.CONTENT_TYPE_JSON);
        response.setResponse(new Order(1L, 1L, 3, OffsetDateTime.of(2017, 4, 2, 11, 8, 10, 0, ZoneOffset.UTC), StatusEnum.APPROVED, Boolean.TRUE));
        handler.handle(Future.succeededFuture(response));
    }

    @Override
    public void placeOrder(Order body, Handler<AsyncResult<ResourceResponse<Order>>> handler) {
        // TODO Auto-generated method stub
    }

}
