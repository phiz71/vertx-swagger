package io.swagger.server.api.verticle;

import io.swagger.server.api.model.InlineResponseDefault;
import java.time.Instant;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.swagger.server.api.MainApiException;
import io.swagger.server.api.MainApiHeader;
import io.swagger.server.api.model.ModelUser;
import io.swagger.server.api.util.ResourceResponse;
import io.swagger.server.api.util.VerticleHelper;

import rx.Completable;
import rx.Single;
import io.vertx.rxjava.ext.auth.User;

public interface UserApi  {
    //createUser
    Single<ResourceResponse<Void>> createUser(ModelUser body);
    
    //createUsersWithArrayInput
    Single<ResourceResponse<Void>> createUsersWithArrayInput(JsonArray body);
    
    //createUsersWithListInput
    Single<ResourceResponse<Void>> createUsersWithListInput(JsonArray body);
    
    //deleteUser
    Single<ResourceResponse<Void>> deleteUser(String username);
    
    //getUserByName
    Single<ResourceResponse<ModelUser>> getUserByName(String username);
    
    //loginUser
    Single<ResourceResponse<String>> loginUser(String username, String password);
    
    //logoutUser
    Single<ResourceResponse<Void>> logoutUser();
    
    //updateUser
    Single<ResourceResponse<Void>> updateUser(String username, ModelUser body);
    
    //uuid
    Single<ResourceResponse<InlineResponseDefault>> uuid(String uuidParam);
    
}
