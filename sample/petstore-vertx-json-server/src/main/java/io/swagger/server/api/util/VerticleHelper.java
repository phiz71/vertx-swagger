package io.swagger.server.api.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.phiz71.vertx.swagger.router.SwaggerRouter;
import io.swagger.server.api.MainApiException;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

public class VerticleHelper {

    private Logger logger;

    public VerticleHelper(Logger logger) {
        this.logger = logger;
    }

    public VerticleHelper(Class clazz) {
        this.logger = LoggerFactory.getLogger(clazz);
    }

    public <T> Handler<AsyncResult<ResourceResponse<T>>> getAsyncResultHandler(Message<JsonObject> message, String serviceName, boolean withJsonEncode, TypeReference<T> type) {
        return result -> {
            if (result.succeeded()) {
                DeliveryOptions deliveryOptions = new DeliveryOptions();
                deliveryOptions.setHeaders(result.result().getHeaders());
                if(withJsonEncode) {
                    message.reply(result.result().toJson(), deliveryOptions);
                } else {
                    message.reply(result.result().getResponse(), deliveryOptions);
                }
            } else {
                Throwable cause = result.cause();
                manageError(message, cause, serviceName);
            }
        };
    }

    public void manageError(Message<JsonObject> message, Throwable cause, String serviceName) {
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
        logger.error("Unexpected error in "+ serviceName, cause);
    }
}