package io.swagger.server.api.verticle;

import com.fasterxml.jackson.core.type.TypeReference;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.Json;
import io.vertx.ext.auth.User;
import com.github.phiz71.vertx.swagger.router.SwaggerRouter;

import io.swagger.server.api.model.InlineResponseDefault;
import java.time.Instant;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.swagger.server.api.MainApiException;
import io.swagger.server.api.MainApiHeader;
import io.swagger.server.api.model.ModelUser;
import io.swagger.server.api.util.ResourceResponse;
import io.swagger.server.api.util.VerticleHelper;

public class UserApiVerticle extends AbstractVerticle {
    private VerticleHelper verticleHelper = new VerticleHelper(this.getClass());

    private static final String CREATEUSER_SERVICE_ID = "createUser";
    private static final String CREATEUSERSWITHARRAYINPUT_SERVICE_ID = "createUsersWithArrayInput";
    private static final String CREATEUSERSWITHLISTINPUT_SERVICE_ID = "createUsersWithListInput";
    private static final String DELETEUSER_SERVICE_ID = "deleteUser";
    private static final String GETUSERBYNAME_SERVICE_ID = "getUserByName";
    private static final String LOGINUSER_SERVICE_ID = "loginUser";
    private static final String LOGOUTUSER_SERVICE_ID = "logoutUser";
    private static final String UPDATEUSER_SERVICE_ID = "updateUser";
    private static final String UUID_SERVICE_ID = "uuid";
    

    private UserApi service = createServiceImplementation();

    //Handler for createUser
    final Handler<Message<JsonObject>> createUserHandler = message -> {
        try {
            ModelUser body = new ModelUser(message.body().getJsonObject("body"));
            service.createUser(body, verticleHelper.getAsyncResultHandler(message, CREATEUSER_SERVICE_ID, false, new TypeReference<Void>(){}));

        } catch (Exception e) {
            verticleHelper.manageError(message, e, CREATEUSER_SERVICE_ID);
        }
    };
    //Handler for createUsersWithArrayInput
    final Handler<Message<JsonObject>> createUsersWithArrayInputHandler = message -> {
        try {
            JsonArray body = message.body().getJsonArray("body");
            service.createUsersWithArrayInput(body, verticleHelper.getAsyncResultHandler(message, CREATEUSERSWITHARRAYINPUT_SERVICE_ID, false, new TypeReference<Void>(){}));

        } catch (Exception e) {
            verticleHelper.manageError(message, e, CREATEUSERSWITHARRAYINPUT_SERVICE_ID);
        }
    };
    //Handler for createUsersWithListInput
    final Handler<Message<JsonObject>> createUsersWithListInputHandler = message -> {
        try {
            JsonArray body = message.body().getJsonArray("body");
            service.createUsersWithListInput(body, verticleHelper.getAsyncResultHandler(message, CREATEUSERSWITHLISTINPUT_SERVICE_ID, false, new TypeReference<Void>(){}));

        } catch (Exception e) {
            verticleHelper.manageError(message, e, CREATEUSERSWITHLISTINPUT_SERVICE_ID);
        }
    };
    //Handler for deleteUser
    final Handler<Message<JsonObject>> deleteUserHandler = message -> {
        try {
            String username = message.body().getString("username");
            service.deleteUser(username, verticleHelper.getAsyncResultHandler(message, DELETEUSER_SERVICE_ID, false, new TypeReference<Void>(){}));

        } catch (Exception e) {
            verticleHelper.manageError(message, e, DELETEUSER_SERVICE_ID);
        }
    };
    //Handler for getUserByName
    final Handler<Message<JsonObject>> getUserByNameHandler = message -> {
        try {
            String username = message.body().getString("username");
            service.getUserByName(username, verticleHelper.getAsyncResultHandler(message, GETUSERBYNAME_SERVICE_ID, true, new TypeReference<ModelUser>(){}));

        } catch (Exception e) {
            verticleHelper.manageError(message, e, GETUSERBYNAME_SERVICE_ID);
        }
    };
    //Handler for loginUser
    final Handler<Message<JsonObject>> loginUserHandler = message -> {
        try {
            String username = message.body().getString("username");
            String password = message.body().getString("password");
            service.loginUser(username, password, verticleHelper.getAsyncResultHandler(message, LOGINUSER_SERVICE_ID, false, new TypeReference<String>(){}));

        } catch (Exception e) {
            verticleHelper.manageError(message, e, LOGINUSER_SERVICE_ID);
        }
    };
    //Handler for logoutUser
    final Handler<Message<JsonObject>> logoutUserHandler = message -> {
        try {
            service.logoutUser(verticleHelper.getAsyncResultHandler(message, LOGOUTUSER_SERVICE_ID, false, new TypeReference<Void>(){}));

        } catch (Exception e) {
            verticleHelper.manageError(message, e, LOGOUTUSER_SERVICE_ID);
        }
    };
    //Handler for updateUser
    final Handler<Message<JsonObject>> updateUserHandler = message -> {
        try {
            String username = message.body().getString("username");
            ModelUser body = new ModelUser(message.body().getJsonObject("body"));
            service.updateUser(username, body, verticleHelper.getAsyncResultHandler(message, UPDATEUSER_SERVICE_ID, false, new TypeReference<Void>(){}));

        } catch (Exception e) {
            verticleHelper.manageError(message, e, UPDATEUSER_SERVICE_ID);
        }
    };
    //Handler for uuid
    final Handler<Message<JsonObject>> uuidHandler = message -> {
        try {
            String uuidParam = message.body().getString("uuidParam");
            service.uuid(uuidParam, verticleHelper.getAsyncResultHandler(message, UUID_SERVICE_ID, true, new TypeReference<InlineResponseDefault>(){}));

        } catch (Exception e) {
            verticleHelper.manageError(message, e, UUID_SERVICE_ID);
        }
    };
    

    @Override
    public void start() throws Exception {
    vertx.eventBus().<JsonObject> consumer(CREATEUSER_SERVICE_ID).handler(createUserHandler);
    vertx.eventBus().<JsonObject> consumer(CREATEUSERSWITHARRAYINPUT_SERVICE_ID).handler(createUsersWithArrayInputHandler);
    vertx.eventBus().<JsonObject> consumer(CREATEUSERSWITHLISTINPUT_SERVICE_ID).handler(createUsersWithListInputHandler);
    vertx.eventBus().<JsonObject> consumer(DELETEUSER_SERVICE_ID).handler(deleteUserHandler);
    vertx.eventBus().<JsonObject> consumer(GETUSERBYNAME_SERVICE_ID).handler(getUserByNameHandler);
    vertx.eventBus().<JsonObject> consumer(LOGINUSER_SERVICE_ID).handler(loginUserHandler);
    vertx.eventBus().<JsonObject> consumer(LOGOUTUSER_SERVICE_ID).handler(logoutUserHandler);
    vertx.eventBus().<JsonObject> consumer(UPDATEUSER_SERVICE_ID).handler(updateUserHandler);
    vertx.eventBus().<JsonObject> consumer(UUID_SERVICE_ID).handler(uuidHandler);
    
    }

    protected UserApi createServiceImplementation() {
        return new UserApiImpl();
    }
}