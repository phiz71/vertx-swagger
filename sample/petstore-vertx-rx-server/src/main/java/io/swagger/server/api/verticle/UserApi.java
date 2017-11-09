package io.swagger.server.api.verticle;

import io.swagger.server.api.model.InlineResponseDefault;
import io.swagger.server.api.MainApiException;
import io.swagger.server.api.MainApiHeader;
import io.swagger.server.api.model.ModelUser;
import java.time.OffsetDateTime;
import io.swagger.server.api.util.ResourceResponse;
import java.util.UUID;
import io.swagger.server.api.util.VerticleHelper;

import rx.Completable;
import rx.Single;
import io.vertx.rxjava.ext.auth.User;

import java.util.List;
import java.util.Map;

public interface UserApi  {
    //createUser
    Single<ResourceResponse<Void>> createUser(ModelUser body);
    
    //createUsersWithArrayInput
    Single<ResourceResponse<Void>> createUsersWithArrayInput(List<ModelUser> body);
    
    //createUsersWithListInput
    Single<ResourceResponse<Void>> createUsersWithListInput(List<ModelUser> body);
    
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
    Single<ResourceResponse<InlineResponseDefault>> uuid(UUID uuidParam);
    
}
