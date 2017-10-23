package io.swagger.server.api.verticle;

import io.swagger.server.api.MainApiException;
import io.swagger.server.api.MainApiHeader;
import io.swagger.server.api.model.Order;
import io.swagger.server.api.util.ResourceResponse;
import io.swagger.server.api.util.VerticleHelper;

public final class StoreApiException extends MainApiException {
    public StoreApiException(int statusCode, String statusMessage) {
        super(statusCode, statusMessage);
    }
    
    public static StoreApiException StoreApi_deleteOrder_400_createException() {
        return new StoreApiException(400, "Invalid ID supplied");
    }
    public static StoreApiException StoreApi_deleteOrder_404_createException() {
        return new StoreApiException(404, "Order not found");
    }
    public static StoreApiException StoreApi_getOrderById_400_createException() {
        return new StoreApiException(400, "Invalid ID supplied");
    }
    public static StoreApiException StoreApi_getOrderById_404_createException() {
        return new StoreApiException(404, "Order not found");
    }
    public static StoreApiException StoreApi_placeOrder_400_createException() {
        return new StoreApiException(400, "Invalid Order");
    }
    

}