package io.swagger.server.api.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

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
                
                service.createUser(body);
                message.reply(null);
            } catch (Exception e) {
                //TODO : replace magic number (101)
                message.fail(101, e.getLocalizedMessage());
            }
        });
        
        //Consumer for createUsersWithArrayInput
        vertx.eventBus().<JsonObject> consumer(CREATEUSERSWITHARRAYINPUT_SERVICE_ID).handler(message -> {
            try {
                List<User> body = Json.mapper.readValue(message.body().getJsonArray("body").encode(),
                        Json.mapper.getTypeFactory().constructCollectionType(List.class, User.class));
                
                service.createUsersWithArrayInput(body);
                message.reply(null);
            } catch (Exception e) {
                //TODO : replace magic number (101)
                message.fail(101, e.getLocalizedMessage());
            }
        });
        
        //Consumer for createUsersWithListInput
        vertx.eventBus().<JsonObject> consumer(CREATEUSERSWITHLISTINPUT_SERVICE_ID).handler(message -> {
            try {
                List<User> body = Json.mapper.readValue(message.body().getJsonArray("body").encode(),
                        Json.mapper.getTypeFactory().constructCollectionType(List.class, User.class));
                
                service.createUsersWithListInput(body);
                message.reply(null);
            } catch (Exception e) {
                //TODO : replace magic number (101)
                message.fail(101, e.getLocalizedMessage());
            }
        });
        
        //Consumer for deleteUser
        vertx.eventBus().<JsonObject> consumer(DELETEUSER_SERVICE_ID).handler(message -> {
            try {
                String username = message.body().getString("username");
                
                service.deleteUser(username);
                message.reply(null);
            } catch (Exception e) {
                //TODO : replace magic number (101)
                message.fail(101, e.getLocalizedMessage());
            }
        });
        
        //Consumer for getUserByName
        vertx.eventBus().<JsonObject> consumer(GETUSERBYNAME_SERVICE_ID).handler(message -> {
            try {
                String username = message.body().getString("username");
                
                User result = service.getUserByName(username);
                message.reply(new JsonObject(Json.encode(result)).encodePrettily());
            } catch (Exception e) {
                //TODO : replace magic number (101)
                message.fail(101, e.getLocalizedMessage());
            }
        });
        
        //Consumer for loginUser
        vertx.eventBus().<JsonObject> consumer(LOGINUSER_SERVICE_ID).handler(message -> {
            try {
                String username = message.body().getString("username");
                String password = message.body().getString("password");
                
                String result = service.loginUser(username, password);
                message.reply(new JsonObject(Json.encode(result)).encodePrettily());
            } catch (Exception e) {
                //TODO : replace magic number (101)
                message.fail(101, e.getLocalizedMessage());
            }
        });
        
        //Consumer for logoutUser
        vertx.eventBus().<JsonObject> consumer(LOGOUTUSER_SERVICE_ID).handler(message -> {
            try {
                
                service.logoutUser();
                message.reply(null);
            } catch (Exception e) {
                //TODO : replace magic number (101)
                message.fail(101, e.getLocalizedMessage());
            }
        });
        
        //Consumer for updateUser
        vertx.eventBus().<JsonObject> consumer(UPDATEUSER_SERVICE_ID).handler(message -> {
            try {
                String username = message.body().getString("username");
                User body = Json.mapper.readValue(message.body().getJsonObject("body").encode(), User.class);
                
                service.updateUser(username, body);
                message.reply(null);
            } catch (Exception e) {
                //TODO : replace magic number (101)
                message.fail(101, e.getLocalizedMessage());
            }
        });
        
    }
}