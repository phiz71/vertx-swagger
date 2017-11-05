package io.swagger.server.api.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.phiz71.vertx.swagger.router.SwaggerRouter;
import io.swagger.server.api.MainApiException;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import rx.functions.Action1;

public class VerticleHelper {

    private Logger logger;

    public VerticleHelper(Logger logger) {
        this.logger = logger;
    }

    public VerticleHelper(Class clazz) {
        this.logger = LoggerFactory.getLogger(clazz);
    }

    public Action1<Throwable> getErrorAction(Message<JsonObject> message, String serviceName) {
        return error -> {
            manageError(message, error, serviceName);
        };
    }

    public <T> Action1<ResourceResponse<T>> getRxResultHandler(Message<JsonObject> message, boolean withJsonEncode, TypeReference<T> type) {
        return result -> {
            DeliveryOptions deliveryOptions = new DeliveryOptions();
            deliveryOptions.setHeaders(result.getHeaders());
            if (withJsonEncode) {
                message.reply(result.toJson(), deliveryOptions);
            } else {
                message.reply(result.getResponse(), deliveryOptions);
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