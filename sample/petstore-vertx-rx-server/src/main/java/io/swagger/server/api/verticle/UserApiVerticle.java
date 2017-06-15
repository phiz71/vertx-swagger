package io.swagger.server.api.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;


import io.swagger.server.api.MainApiException;
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
    
    //TODO : create Implementation
    UserApi service = new UserApiImpl();

    @Override
    public void start() throws Exception {
        
        //Consumer for createUser
        vertx.eventBus().<JsonObject> consumer(CREATEUSER_SERVICE_ID).handler(message -> {
            try {
                User body = Json.mapper.readValue(message.body().getJsonObject("body").encode(), User.class);
                service.createUser(body).subscribe(
                    onSuccess -> {
                        message.reply(null);
                    },
                    onError -> {
                        LOGGER.error("Error in " + CREATEUSER_SERVICE_ID, onError);
                        
                        int code = MainApiException.INTERNAL_SERVER_ERROR.getStatusCode();
                        String statusMessage = MainApiException.INTERNAL_SERVER_ERROR.getStatusMessage();
                        if (onError instanceof MainApiException) {
                            code = ((MainApiException)onError).getStatusCode();
                            statusMessage = ((MainApiException)onError).getStatusMessage();
                        }

                        message.fail(code, statusMessage);
                    });

            } catch (Exception e) {
                LOGGER.error("Error in "+CREATEUSER_SERVICE_ID, e);
                message.fail(MainApiException.INTERNAL_SERVER_ERROR.getStatusCode(), MainApiException.INTERNAL_SERVER_ERROR.getStatusMessage());
            }
        });
        
        //Consumer for createUsersWithArrayInput
        vertx.eventBus().<JsonObject> consumer(CREATEUSERSWITHARRAYINPUT_SERVICE_ID).handler(message -> {
            try {
                List<User> body = Json.mapper.readValue(message.body().getJsonArray("body").encode(), Json.mapper.getTypeFactory().constructCollectionType(List.class, User.class));
                service.createUsersWithArrayInput(body).subscribe(
                    onSuccess -> {
                        message.reply(null);
                    },
                    onError -> {
                        LOGGER.error("Error in " + CREATEUSERSWITHARRAYINPUT_SERVICE_ID, onError);
                        
                        int code = MainApiException.INTERNAL_SERVER_ERROR.getStatusCode();
                        String statusMessage = MainApiException.INTERNAL_SERVER_ERROR.getStatusMessage();
                        if (onError instanceof MainApiException) {
                            code = ((MainApiException)onError).getStatusCode();
                            statusMessage = ((MainApiException)onError).getStatusMessage();
                        }

                        message.fail(code, statusMessage);
                    });

            } catch (Exception e) {
                LOGGER.error("Error in "+CREATEUSERSWITHARRAYINPUT_SERVICE_ID, e);
                message.fail(MainApiException.INTERNAL_SERVER_ERROR.getStatusCode(), MainApiException.INTERNAL_SERVER_ERROR.getStatusMessage());
            }
        });
        
        //Consumer for createUsersWithListInput
        vertx.eventBus().<JsonObject> consumer(CREATEUSERSWITHLISTINPUT_SERVICE_ID).handler(message -> {
            try {
                List<User> body = Json.mapper.readValue(message.body().getJsonArray("body").encode(), Json.mapper.getTypeFactory().constructCollectionType(List.class, User.class));
                service.createUsersWithListInput(body).subscribe(
                    onSuccess -> {
                        message.reply(null);
                    },
                    onError -> {
                        LOGGER.error("Error in " + CREATEUSERSWITHLISTINPUT_SERVICE_ID, onError);
                        
                        int code = MainApiException.INTERNAL_SERVER_ERROR.getStatusCode();
                        String statusMessage = MainApiException.INTERNAL_SERVER_ERROR.getStatusMessage();
                        if (onError instanceof MainApiException) {
                            code = ((MainApiException)onError).getStatusCode();
                            statusMessage = ((MainApiException)onError).getStatusMessage();
                        }

                        message.fail(code, statusMessage);
                    });

            } catch (Exception e) {
                LOGGER.error("Error in "+CREATEUSERSWITHLISTINPUT_SERVICE_ID, e);
                message.fail(MainApiException.INTERNAL_SERVER_ERROR.getStatusCode(), MainApiException.INTERNAL_SERVER_ERROR.getStatusMessage());
            }
        });
        
        //Consumer for deleteUser
        vertx.eventBus().<JsonObject> consumer(DELETEUSER_SERVICE_ID).handler(message -> {
            try {
                String username = message.body().getString("username");
                service.deleteUser(username).subscribe(
                    onSuccess -> {
                        message.reply(null);
                    },
                    onError -> {
                        LOGGER.error("Error in " + DELETEUSER_SERVICE_ID, onError);
                        
                        int code = MainApiException.INTERNAL_SERVER_ERROR.getStatusCode();
                        String statusMessage = MainApiException.INTERNAL_SERVER_ERROR.getStatusMessage();
                        if (onError instanceof MainApiException) {
                            code = ((MainApiException)onError).getStatusCode();
                            statusMessage = ((MainApiException)onError).getStatusMessage();
                        }

                        message.fail(code, statusMessage);
                    });

            } catch (Exception e) {
                LOGGER.error("Error in "+DELETEUSER_SERVICE_ID, e);
                message.fail(MainApiException.INTERNAL_SERVER_ERROR.getStatusCode(), MainApiException.INTERNAL_SERVER_ERROR.getStatusMessage());
            }
        });
        
        //Consumer for getUserByName
        vertx.eventBus().<JsonObject> consumer(GETUSERBYNAME_SERVICE_ID).handler(message -> {
            try {
                String username = message.body().getString("username");
                service.getUserByName(username).subscribe(
                    onSuccess -> {
                        message.reply(new JsonObject(Json.encode(onSuccess)).encodePrettily());
                    },
                    onError -> {
                        LOGGER.error("Error in " + GETUSERBYNAME_SERVICE_ID, onError);
                        
                        int code = MainApiException.INTERNAL_SERVER_ERROR.getStatusCode();
                        String statusMessage = MainApiException.INTERNAL_SERVER_ERROR.getStatusMessage();
                        if (onError instanceof MainApiException) {
                            code = ((MainApiException)onError).getStatusCode();
                            statusMessage = ((MainApiException)onError).getStatusMessage();
                        }

                        message.fail(code, statusMessage);
                    });

            } catch (Exception e) {
                LOGGER.error("Error in "+GETUSERBYNAME_SERVICE_ID, e);
                message.fail(MainApiException.INTERNAL_SERVER_ERROR.getStatusCode(), MainApiException.INTERNAL_SERVER_ERROR.getStatusMessage());
            }
        });
        
        //Consumer for loginUser
        vertx.eventBus().<JsonObject> consumer(LOGINUSER_SERVICE_ID).handler(message -> {
            try {
                String username = message.body().getString("username");
                String password = message.body().getString("password");
                service.loginUser(username, password).subscribe(
                    onSuccess -> {
                        message.reply(new JsonObject(Json.encode(onSuccess)).encodePrettily());
                    },
                    onError -> {
                        LOGGER.error("Error in " + LOGINUSER_SERVICE_ID, onError);
                        
                        int code = MainApiException.INTERNAL_SERVER_ERROR.getStatusCode();
                        String statusMessage = MainApiException.INTERNAL_SERVER_ERROR.getStatusMessage();
                        if (onError instanceof MainApiException) {
                            code = ((MainApiException)onError).getStatusCode();
                            statusMessage = ((MainApiException)onError).getStatusMessage();
                        }

                        message.fail(code, statusMessage);
                    });

            } catch (Exception e) {
                LOGGER.error("Error in "+LOGINUSER_SERVICE_ID, e);
                message.fail(MainApiException.INTERNAL_SERVER_ERROR.getStatusCode(), MainApiException.INTERNAL_SERVER_ERROR.getStatusMessage());
            }
        });
        
        //Consumer for logoutUser
        vertx.eventBus().<JsonObject> consumer(LOGOUTUSER_SERVICE_ID).handler(message -> {
            try {
                service.logoutUser().subscribe(
                    onSuccess -> {
                        message.reply(null);
                    },
                    onError -> {
                        LOGGER.error("Error in " + LOGOUTUSER_SERVICE_ID, onError);
                        
                        int code = MainApiException.INTERNAL_SERVER_ERROR.getStatusCode();
                        String statusMessage = MainApiException.INTERNAL_SERVER_ERROR.getStatusMessage();
                        if (onError instanceof MainApiException) {
                            code = ((MainApiException)onError).getStatusCode();
                            statusMessage = ((MainApiException)onError).getStatusMessage();
                        }

                        message.fail(code, statusMessage);
                    });

            } catch (Exception e) {
                LOGGER.error("Error in "+LOGOUTUSER_SERVICE_ID, e);
                message.fail(MainApiException.INTERNAL_SERVER_ERROR.getStatusCode(), MainApiException.INTERNAL_SERVER_ERROR.getStatusMessage());
            }
        });
        
        //Consumer for updateUser
        vertx.eventBus().<JsonObject> consumer(UPDATEUSER_SERVICE_ID).handler(message -> {
            try {
                String username = message.body().getString("username");
                User body = Json.mapper.readValue(message.body().getJsonObject("body").encode(), User.class);
                service.updateUser(username, body).subscribe(
                    onSuccess -> {
                        message.reply(null);
                    },
                    onError -> {
                        LOGGER.error("Error in " + UPDATEUSER_SERVICE_ID, onError);
                        
                        int code = MainApiException.INTERNAL_SERVER_ERROR.getStatusCode();
                        String statusMessage = MainApiException.INTERNAL_SERVER_ERROR.getStatusMessage();
                        if (onError instanceof MainApiException) {
                            code = ((MainApiException)onError).getStatusCode();
                            statusMessage = ((MainApiException)onError).getStatusMessage();
                        }

                        message.fail(code, statusMessage);
                    });

            } catch (Exception e) {
                LOGGER.error("Error in "+UPDATEUSER_SERVICE_ID, e);
                message.fail(MainApiException.INTERNAL_SERVER_ERROR.getStatusCode(), MainApiException.INTERNAL_SERVER_ERROR.getStatusMessage());
            }
        });
        
    }
}