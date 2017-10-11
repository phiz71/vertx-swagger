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

import io.swagger.server.api.model.InlineResponseDefault;
import io.swagger.server.api.MainApiException;
import io.swagger.server.api.MainApiHeader;
import io.swagger.server.api.model.ModelUser;
import java.time.OffsetDateTime;
import io.swagger.server.api.util.ResourceResponse;
import java.util.UUID;

import java.util.List;
import java.util.Map;

public class UserApiVerticle extends AbstractVerticle {
    final static Logger LOGGER = LoggerFactory.getLogger(UserApiVerticle.class); 
    
    final static String CREATEUSER_SERVICE_ID = "createUser";
    final static String CREATEUSERSWITHARRAYINPUT_SERVICE_ID = "createUsersWithArrayInput";
    final static String CREATEUSERSWITHLISTINPUT_SERVICE_ID = "createUsersWithListInput";
    final static String DELETEUSER_SERVICE_ID = "deleteUser";
    final static String GETUSERBYNAME_SERVICE_ID = "getUserByName";
    final static String LOGINUSER_SERVICE_ID = "loginUser";
    final static String LOGOUTUSER_SERVICE_ID = "logoutUser";
    final static String UPDATEUSER_SERVICE_ID = "updateUser";
    final static String UUID_SERVICE_ID = "uuid";
    

    protected UserApi service = createServiceImplementation();

    @Override
    public void start() throws Exception {
        
        //Consumer for createUser
        vertx.eventBus().<JsonObject> consumer(CREATEUSER_SERVICE_ID).handler(message -> {
            try {
                ModelUser body = Json.mapper.readValue(message.body().getJsonObject("body").encode(), ModelUser.class);
                service.createUser(body).subscribe(
                    result -> {
                        DeliveryOptions deliveryOptions = new DeliveryOptions();
                        deliveryOptions.setHeaders(result.getHeaders());
                        message.reply(null, deliveryOptions);
                    },
                    error -> {
                        manageError(message, error, CREATEUSER_SERVICE_ID);
                    });
            } catch (Exception e) {
                manageError(message, e, CREATEUSER_SERVICE_ID);
            }
        });
        
        //Consumer for createUsersWithArrayInput
        vertx.eventBus().<JsonObject> consumer(CREATEUSERSWITHARRAYINPUT_SERVICE_ID).handler(message -> {
            try {
                List<ModelUser> body = Json.mapper.readValue(message.body().getJsonArray("body").encode(), new TypeReference<List<ModelUser>>(){});
                service.createUsersWithArrayInput(body).subscribe(
                    result -> {
                        DeliveryOptions deliveryOptions = new DeliveryOptions();
                        deliveryOptions.setHeaders(result.getHeaders());
                        message.reply(null, deliveryOptions);
                    },
                    error -> {
                        manageError(message, error, CREATEUSERSWITHARRAYINPUT_SERVICE_ID);
                    });
            } catch (Exception e) {
                manageError(message, e, CREATEUSERSWITHARRAYINPUT_SERVICE_ID);
            }
        });
        
        //Consumer for createUsersWithListInput
        vertx.eventBus().<JsonObject> consumer(CREATEUSERSWITHLISTINPUT_SERVICE_ID).handler(message -> {
            try {
                List<ModelUser> body = Json.mapper.readValue(message.body().getJsonArray("body").encode(), new TypeReference<List<ModelUser>>(){});
                service.createUsersWithListInput(body).subscribe(
                    result -> {
                        DeliveryOptions deliveryOptions = new DeliveryOptions();
                        deliveryOptions.setHeaders(result.getHeaders());
                        message.reply(null, deliveryOptions);
                    },
                    error -> {
                        manageError(message, error, CREATEUSERSWITHLISTINPUT_SERVICE_ID);
                    });
            } catch (Exception e) {
                manageError(message, e, CREATEUSERSWITHLISTINPUT_SERVICE_ID);
            }
        });
        
        //Consumer for deleteUser
        vertx.eventBus().<JsonObject> consumer(DELETEUSER_SERVICE_ID).handler(message -> {
            try {
                String username = message.body().getString("username");
                service.deleteUser(username).subscribe(
                    result -> {
                        DeliveryOptions deliveryOptions = new DeliveryOptions();
                        deliveryOptions.setHeaders(result.getHeaders());
                        message.reply(null, deliveryOptions);
                    },
                    error -> {
                        manageError(message, error, DELETEUSER_SERVICE_ID);
                    });
            } catch (Exception e) {
                manageError(message, e, DELETEUSER_SERVICE_ID);
            }
        });
        
        //Consumer for getUserByName
        vertx.eventBus().<JsonObject> consumer(GETUSERBYNAME_SERVICE_ID).handler(message -> {
            try {
                String username = message.body().getString("username");
                service.getUserByName(username).subscribe(
                    result -> {
                        DeliveryOptions deliveryOptions = new DeliveryOptions();
                        deliveryOptions.setHeaders(result.getHeaders());
                        message.reply(new JsonObject(Json.encode(result.getResponse())).encodePrettily(), deliveryOptions);
                    },
                    error -> {
                        manageError(message, error, GETUSERBYNAME_SERVICE_ID);
                    });
            } catch (Exception e) {
                manageError(message, e, GETUSERBYNAME_SERVICE_ID);
            }
        });
        
        //Consumer for loginUser
        vertx.eventBus().<JsonObject> consumer(LOGINUSER_SERVICE_ID).handler(message -> {
            try {
                String username = message.body().getString("username");
                String password = message.body().getString("password");
                service.loginUser(username, password).subscribe(
                    result -> {
                        DeliveryOptions deliveryOptions = new DeliveryOptions();
                        deliveryOptions.setHeaders(result.getHeaders());
                        message.reply(result.getResponse(), deliveryOptions);
                    },
                    error -> {
                        manageError(message, error, LOGINUSER_SERVICE_ID);
                    });
            } catch (Exception e) {
                manageError(message, e, LOGINUSER_SERVICE_ID);
            }
        });
        
        //Consumer for logoutUser
        vertx.eventBus().<JsonObject> consumer(LOGOUTUSER_SERVICE_ID).handler(message -> {
            try {
                service.logoutUser().subscribe(
                    result -> {
                        DeliveryOptions deliveryOptions = new DeliveryOptions();
                        deliveryOptions.setHeaders(result.getHeaders());
                        message.reply(null, deliveryOptions);
                    },
                    error -> {
                        manageError(message, error, LOGOUTUSER_SERVICE_ID);
                    });
            } catch (Exception e) {
                manageError(message, e, LOGOUTUSER_SERVICE_ID);
            }
        });
        
        //Consumer for updateUser
        vertx.eventBus().<JsonObject> consumer(UPDATEUSER_SERVICE_ID).handler(message -> {
            try {
                String username = message.body().getString("username");
                ModelUser body = Json.mapper.readValue(message.body().getJsonObject("body").encode(), ModelUser.class);
                service.updateUser(username, body).subscribe(
                    result -> {
                        DeliveryOptions deliveryOptions = new DeliveryOptions();
                        deliveryOptions.setHeaders(result.getHeaders());
                        message.reply(null, deliveryOptions);
                    },
                    error -> {
                        manageError(message, error, UPDATEUSER_SERVICE_ID);
                    });
            } catch (Exception e) {
                manageError(message, e, UPDATEUSER_SERVICE_ID);
            }
        });
        
        //Consumer for uuid
        vertx.eventBus().<JsonObject> consumer(UUID_SERVICE_ID).handler(message -> {
            try {
                UUID uuidParam = UUID.fromString(message.body().getString("uuidParam"));
                service.uuid(uuidParam).subscribe(
                    result -> {
                        DeliveryOptions deliveryOptions = new DeliveryOptions();
                        deliveryOptions.setHeaders(result.getHeaders());
                        message.reply(new JsonObject(Json.encode(result.getResponse())).encodePrettily(), deliveryOptions);
                    },
                    error -> {
                        manageError(message, error, UUID_SERVICE_ID);
                    });
            } catch (Exception e) {
                manageError(message, e, UUID_SERVICE_ID);
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

    protected UserApi createServiceImplementation() {
        return new UserApiImpl();
    }
}
