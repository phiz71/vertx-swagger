package io.swagger.server.api.verticle;

import com.fasterxml.jackson.core.type.TypeReference;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.Message;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.auth.User;
import com.github.phiz71.vertx.swagger.router.SwaggerRouter;

import io.swagger.server.api.util.MainApiException;
import io.swagger.server.api.util.MainApiHeader;
import io.swagger.server.api.model.Order;
import io.swagger.server.api.util.ResourceResponse;

import java.util.List;
import java.util.Map;

public class StoreApiVerticle extends AbstractVerticle {
    final static Logger LOGGER = LoggerFactory.getLogger(StoreApiVerticle.class); 
    
    final static String DELETEORDER_SERVICE_ID = "deleteOrder";
    final static String GETINVENTORY_SERVICE_ID = "getInventory";
    final static String GETORDERBYID_SERVICE_ID = "getOrderById";
    final static String PLACEORDER_SERVICE_ID = "placeOrder";
    

    protected StoreApi service = createServiceImplementation();

    @Override
    public void start() throws Exception {
        
        //Consumer for deleteOrder
        vertx.eventBus().<JsonObject> consumer(DELETEORDER_SERVICE_ID).handler(message -> {
            try {
                Long orderId = Json.mapper.readValue(message.body().getString("orderId"), Long.class);
                service.deleteOrder(orderId).subscribe(
                    result -> {
                        DeliveryOptions deliveryOptions = new DeliveryOptions();
                        deliveryOptions.setHeaders(result.getHeaders());
                        message.reply(null, deliveryOptions);
                    },
                    error -> {
                        manageError(message, error, DELETEORDER_SERVICE_ID);
                    });
            } catch (Exception e) {
                manageError(message, e, DELETEORDER_SERVICE_ID);
            }
        });
        
        //Consumer for getInventory
        vertx.eventBus().<JsonObject> consumer(GETINVENTORY_SERVICE_ID).handler(message -> {
            try {
                User user = SwaggerRouter.extractAuthUserFromMessage(message);
                service.getInventory(io.vertx.rxjava.ext.auth.User.newInstance(user)).subscribe(
                    result -> {
                        DeliveryOptions deliveryOptions = new DeliveryOptions();
                        deliveryOptions.setHeaders(result.getHeaders());
                        message.reply(new JsonObject(Json.encode(result.getResponse())).encodePrettily(), deliveryOptions);
                    },
                    error -> {
                        manageError(message, error, GETINVENTORY_SERVICE_ID);
                    });
            } catch (Exception e) {
                manageError(message, e, GETINVENTORY_SERVICE_ID);
            }
        });
        
        //Consumer for getOrderById
        vertx.eventBus().<JsonObject> consumer(GETORDERBYID_SERVICE_ID).handler(message -> {
            try {
                Long orderId = Json.mapper.readValue(message.body().getString("OrderId"), Long.class);
                service.getOrderById(orderId).subscribe(
                    result -> {
                        DeliveryOptions deliveryOptions = new DeliveryOptions();
                        deliveryOptions.setHeaders(result.getHeaders());
                        message.reply(new JsonObject(Json.encode(result.getResponse())).encodePrettily(), deliveryOptions);
                    },
                    error -> {
                        manageError(message, error, GETORDERBYID_SERVICE_ID);
                    });
            } catch (Exception e) {
                manageError(message, e, GETORDERBYID_SERVICE_ID);
            }
        });
        
        //Consumer for placeOrder
        vertx.eventBus().<JsonObject> consumer(PLACEORDER_SERVICE_ID).handler(message -> {
            try {
                Order body = Json.mapper.readValue(message.body().getJsonObject("body").encode(), Order.class);
                service.placeOrder(body).subscribe(
                    result -> {
                        DeliveryOptions deliveryOptions = new DeliveryOptions();
                        deliveryOptions.setHeaders(result.getHeaders());
                        message.reply(new JsonObject(Json.encode(result.getResponse())).encodePrettily(), deliveryOptions);
                    },
                    error -> {
                        manageError(message, error, PLACEORDER_SERVICE_ID);
                    });
            } catch (Exception e) {
                manageError(message, e, PLACEORDER_SERVICE_ID);
            }
        });
        
    }
    
    private void manageError(Message<JsonObject> message, Throwable cause, String serviceName) {
        int code = MainApiException.INTERNAL_SERVER_ERROR.getStatusCode();
        String statusMessage = MainApiException.INTERNAL_SERVER_ERROR.getStatusMessage();
        DeliveryOptions deliveryOptions = new DeliveryOptions();
        if (cause instanceof MainApiException) {
            code = ((MainApiException)cause).getStatusCode();
            statusMessage = ((MainApiException)cause).getStatusMessage();
            deliveryOptions.setHeaders(((MainApiException)cause).getHeaders());
        } else {
            logUnexpectedError(serviceName, cause); 
        }
        deliveryOptions.addHeader(SwaggerRouter.CUSTOM_STATUS_CODE_HEADER_KEY, String.valueOf(code));
        deliveryOptions.addHeader(SwaggerRouter.CUSTOM_STATUS_MESSAGE_HEADER_KEY, statusMessage);

        message.reply(null, deliveryOptions);
    }
    
    private void logUnexpectedError(String serviceName, Throwable cause) {
        LOGGER.error("Unexpected error in "+ serviceName, cause);
    }

    protected StoreApi createServiceImplementation() {
        return new StoreApiImpl();
    }
}
