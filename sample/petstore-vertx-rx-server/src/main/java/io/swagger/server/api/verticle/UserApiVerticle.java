package io.swagger.server.api.verticle;

import com.fasterxml.jackson.core.type.TypeReference;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import io.swagger.server.api.model.InlineResponseDefault;
import io.swagger.server.api.MainApiException;
import java.util.UUID;
import io.swagger.server.api.model.User;

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
    
    //TODO : create Implementation
    UserApi service = new UserApiImpl();

    @Override
    public void start() throws Exception {
        
        //Consumer for createUser
        vertx.eventBus().<JsonObject> consumer(CREATEUSER_SERVICE_ID).handler(message -> {
            try {
                User body = Json.mapper.readValue(message.body().getJsonObject("body").encode(), User.class);
                service.createUser(body).subscribe(
                    () -> {
                        message.reply(null);
                    },
                    error -> {
                        manageError(message, error, "createUser");
                    });
            } catch (Exception e) {
                manageError(message, e, "createUser");
            }
        });
        
        //Consumer for createUsersWithArrayInput
        vertx.eventBus().<JsonObject> consumer(CREATEUSERSWITHARRAYINPUT_SERVICE_ID).handler(message -> {
            try {
                List<User> body = Json.mapper.readValue(message.body().getJsonArray("body").encode(), new TypeReference<List<User>>(){});
                service.createUsersWithArrayInput(body).subscribe(
                    () -> {
                        message.reply(null);
                    },
                    error -> {
                        manageError(message, error, "createUsersWithArrayInput");
                    });
            } catch (Exception e) {
                manageError(message, e, "createUsersWithArrayInput");
            }
        });
        
        //Consumer for createUsersWithListInput
        vertx.eventBus().<JsonObject> consumer(CREATEUSERSWITHLISTINPUT_SERVICE_ID).handler(message -> {
            try {
                List<User> body = Json.mapper.readValue(message.body().getJsonArray("body").encode(), new TypeReference<List<User>>(){});
                service.createUsersWithListInput(body).subscribe(
                    () -> {
                        message.reply(null);
                    },
                    error -> {
                        manageError(message, error, "createUsersWithListInput");
                    });
            } catch (Exception e) {
                manageError(message, e, "createUsersWithListInput");
            }
        });
        
        //Consumer for deleteUser
        vertx.eventBus().<JsonObject> consumer(DELETEUSER_SERVICE_ID).handler(message -> {
            try {
                String username = message.body().getString("username");
                service.deleteUser(username).subscribe(
                    () -> {
                        message.reply(null);
                    },
                    error -> {
                        manageError(message, error, "deleteUser");
                    });
            } catch (Exception e) {
                manageError(message, e, "deleteUser");
            }
        });
        
        //Consumer for getUserByName
        vertx.eventBus().<JsonObject> consumer(GETUSERBYNAME_SERVICE_ID).handler(message -> {
            try {
                String username = message.body().getString("username");
                service.getUserByName(username).subscribe(
                    result -> {
                        message.reply(new JsonObject(Json.encode(result)).encodePrettily());
                    },
                    error -> {
                        manageError(message, error, "getUserByName");
                    });
            } catch (Exception e) {
                manageError(message, e, "getUserByName");
            }
        });
        
        //Consumer for loginUser
        vertx.eventBus().<JsonObject> consumer(LOGINUSER_SERVICE_ID).handler(message -> {
            try {
                String username = message.body().getString("username");
                String password = message.body().getString("password");
                service.loginUser(username, password).subscribe(
                    result -> {
                        message.reply(new JsonObject(Json.encode(result)).encodePrettily());
                    },
                    error -> {
                        manageError(message, error, "loginUser");
                    });
            } catch (Exception e) {
                manageError(message, e, "loginUser");
            }
        });
        
        //Consumer for logoutUser
        vertx.eventBus().<JsonObject> consumer(LOGOUTUSER_SERVICE_ID).handler(message -> {
            try {
                service.logoutUser().subscribe(
                    () -> {
                        message.reply(null);
                    },
                    error -> {
                        manageError(message, error, "logoutUser");
                    });
            } catch (Exception e) {
                manageError(message, e, "logoutUser");
            }
        });
        
        //Consumer for updateUser
        vertx.eventBus().<JsonObject> consumer(UPDATEUSER_SERVICE_ID).handler(message -> {
            try {
                String username = message.body().getString("username");
                User body = Json.mapper.readValue(message.body().getJsonObject("body").encode(), User.class);
                service.updateUser(username, body).subscribe(
                    () -> {
                        message.reply(null);
                    },
                    error -> {
                        manageError(message, error, "updateUser");
                    });
            } catch (Exception e) {
                manageError(message, e, "updateUser");
            }
        });
        
        //Consumer for uuid
        vertx.eventBus().<JsonObject> consumer(UUID_SERVICE_ID).handler(message -> {
            try {
                UUID uuidParam = UUID.fromString(message.body().getString("uuidParam"));
                service.uuid(uuidParam).subscribe(
                    result -> {
                        message.reply(new JsonObject(Json.encode(result)).encodePrettily());
                    },
                    error -> {
                        manageError(message, error, "uuid");
                    });
            } catch (Exception e) {
                manageError(message, e, "uuid");
            }
        });
        
    }
    
    private void manageError(Message<JsonObject> message, Throwable cause, String serviceName) {
        int code = MainApiException.INTERNAL_SERVER_ERROR.getStatusCode();
        String statusMessage = MainApiException.INTERNAL_SERVER_ERROR.getStatusMessage();
        if (cause instanceof MainApiException) {
            code = ((MainApiException)cause).getStatusCode();
            statusMessage = ((MainApiException)cause).getStatusMessage();
        } else {
            logUnexpectedError(serviceName, cause); 
        }
            
        message.fail(code, statusMessage);
    }
    
    private void logUnexpectedError(String serviceName, Throwable cause) {
        LOGGER.error("Unexpected error in "+ serviceName, cause);
    }
}
