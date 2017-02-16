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
    
    final static String POST_USER_SERVICE_ID = "POST_user";
    final static String POST_USER_CREATEWITHARRAY_SERVICE_ID = "POST_user_createWithArray";
    final static String POST_USER_CREATEWITHLIST_SERVICE_ID = "POST_user_createWithList";
    final static String DELETE_USER_USERNAME_SERVICE_ID = "DELETE_user_username";
    final static String GET_USER_USERNAME_SERVICE_ID = "GET_user_username";
    final static String GET_USER_LOGIN_SERVICE_ID = "GET_user_login";
    final static String GET_USER_LOGOUT_SERVICE_ID = "GET_user_logout";
    final static String PUT_USER_USERNAME_SERVICE_ID = "PUT_user_username";
    
    
    //TODO : create Implementation
    UserApi service = new UserApiImpl();

    @Override
    public void start() throws Exception {
        
        //Consumer for POST_user
        vertx.eventBus().<JsonObject> consumer(POST_USER_SERVICE_ID).handler(message -> {
            try {
                
                User body = Json.mapper.readValue(message.body().getJsonObject("body").encode(), User.class);
                
                
                //TODO: call implementation
                
                service.createUser(body);
                message.reply(null);
                
            } catch (Exception e) {
                //TODO : replace magic number (101)
                message.fail(101, e.getLocalizedMessage());
            }
        });
        
        //Consumer for POST_user_createWithArray
        vertx.eventBus().<JsonObject> consumer(POST_USER_CREATEWITHARRAY_SERVICE_ID).handler(message -> {
            try {
                
                List<User> body = Json.mapper.readValue(message.body().getJsonArray("body").encode(), 
                        Json.mapper.getTypeFactory().constructCollectionType(List.class, User.class));
                
                
                //TODO: call implementation
                
                service.createUsersWithArrayInput(body);
                message.reply(null);
                
            } catch (Exception e) {
                //TODO : replace magic number (101)
                message.fail(101, e.getLocalizedMessage());
            }
        });
        
        //Consumer for POST_user_createWithList
        vertx.eventBus().<JsonObject> consumer(POST_USER_CREATEWITHLIST_SERVICE_ID).handler(message -> {
            try {
                
                List<User> body = Json.mapper.readValue(message.body().getJsonArray("body").encode(), 
                        Json.mapper.getTypeFactory().constructCollectionType(List.class, User.class));
                
                
                //TODO: call implementation
                
                service.createUsersWithListInput(body);
                message.reply(null);
                
            } catch (Exception e) {
                //TODO : replace magic number (101)
                message.fail(101, e.getLocalizedMessage());
            }
        });
        
        //Consumer for DELETE_user_username
        vertx.eventBus().<JsonObject> consumer(DELETE_USER_USERNAME_SERVICE_ID).handler(message -> {
            try {
                
                String username = Json.mapper.readValue(message.body().getJsonObject("username").encode(), String.class);
                
                
                //TODO: call implementation
                
                service.deleteUser(username);
                message.reply(null);
                
            } catch (Exception e) {
                //TODO : replace magic number (101)
                message.fail(101, e.getLocalizedMessage());
            }
        });
        
        //Consumer for GET_user_username
        vertx.eventBus().<JsonObject> consumer(GET_USER_USERNAME_SERVICE_ID).handler(message -> {
            try {
                
                String username = Json.mapper.readValue(message.body().getJsonObject("username").encode(), String.class);
                
                
                //TODO: call implementation
                
                User result = service.getUserByName(username);
                
                message.reply(new JsonObject(Json.encode(result)));
                
            } catch (Exception e) {
                //TODO : replace magic number (101)
                message.fail(101, e.getLocalizedMessage());
            }
        });
        
        //Consumer for GET_user_login
        vertx.eventBus().<JsonObject> consumer(GET_USER_LOGIN_SERVICE_ID).handler(message -> {
            try {
                
                String username = Json.mapper.readValue(message.body().getJsonObject("username").encode(), String.class);
                
                String password = Json.mapper.readValue(message.body().getJsonObject("password").encode(), String.class);
                
                
                //TODO: call implementation
                
                String result = service.loginUser(username, password);
                
                message.reply(new JsonObject(Json.encode(result)));
                
            } catch (Exception e) {
                //TODO : replace magic number (101)
                message.fail(101, e.getLocalizedMessage());
            }
        });
        
        //Consumer for GET_user_logout
        vertx.eventBus().<JsonObject> consumer(GET_USER_LOGOUT_SERVICE_ID).handler(message -> {
            try {
                
                
                //TODO: call implementation
                
                service.logoutUser();
                message.reply(null);
                
            } catch (Exception e) {
                //TODO : replace magic number (101)
                message.fail(101, e.getLocalizedMessage());
            }
        });
        
        //Consumer for PUT_user_username
        vertx.eventBus().<JsonObject> consumer(PUT_USER_USERNAME_SERVICE_ID).handler(message -> {
            try {
                
                String username = Json.mapper.readValue(message.body().getJsonObject("username").encode(), String.class);
                
                User body = Json.mapper.readValue(message.body().getJsonObject("body").encode(), User.class);
                
                
                //TODO: call implementation
                
                service.updateUser(username, body);
                message.reply(null);
                
            } catch (Exception e) {
                //TODO : replace magic number (101)
                message.fail(101, e.getLocalizedMessage());
            }
        });
        
    }
}
