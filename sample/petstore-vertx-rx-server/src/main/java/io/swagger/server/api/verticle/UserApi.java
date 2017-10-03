package io.swagger.server.api.verticle;

import io.swagger.server.api.model.InlineResponseDefault;
import io.swagger.server.api.MainApiException;
import io.swagger.server.api.model.ModelUser;
import java.util.UUID;

import rx.Completable;
import rx.Single;
import io.vertx.rxjava.ext.auth.User;

import java.util.List;
import java.util.Map;

public interface UserApi  {
    //createUser
    Completable createUser(ModelUser body);
    
    //createUsersWithArrayInput
    Completable createUsersWithArrayInput(List<ModelUser> body);
    
    //createUsersWithListInput
    Completable createUsersWithListInput(List<ModelUser> body);
    
    //deleteUser
    Completable deleteUser(String username);
    
    //getUserByName
    Single<ModelUser> getUserByName(String username);
    
    //loginUser
    Single<String> loginUser(String username, String password);
    
    //logoutUser
    Completable logoutUser();
    
    //updateUser
    Completable updateUser(String username, ModelUser body);
    
    //uuid
    Single<InlineResponseDefault> uuid(UUID uuidParam);
    
}
