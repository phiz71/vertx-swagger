package io.swagger.server.api.verticle;

import io.swagger.server.api.model.Order;
import io.swagger.server.api.util.ResourceResponse;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.User;

import java.time.Instant;

public class StoreApiImpl implements StoreApi {

    @Override
    public void deleteOrder(Long orderId, Handler<AsyncResult<ResourceResponse<Void>>> handler) {
        // TODO Auto-generated method stub

    }

    @Override
    public void getInventory(User user, Handler<AsyncResult<ResourceResponse<JsonObject>>> handler) {
        // TODO Auto-generated method stub
        handler.handle(Future.succeededFuture(null));
    }

    @Override
    public void getOrderById(Long orderId, Handler<AsyncResult<ResourceResponse<Order>>> handler) {
        ResourceResponse<Order> response = new ResourceResponse<>();
        response.addHeader(StoreApiHeader.CONTENT_TYPE_JSON);
        response.setResponse(new Order(1L, 1L, 3, Instant.parse("2017-04-02T11:08:10Z"), Order.OrderStatus.APPROVED, Boolean.TRUE));
        handler.handle(Future.succeededFuture(response));
    }

    @Override
    public void placeOrder(Order body, Handler<AsyncResult<ResourceResponse<Order>>> handler) {
        // TODO Auto-generated method stub
    }

}
