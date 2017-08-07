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
    public Completable createUser(ModelUser body);
    
    //createUsersWithArrayInput
    public Completable createUsersWithArrayInput(List<ModelUser> body);
    
    //createUsersWithListInput
    public Completable createUsersWithListInput(List<ModelUser> body);
    
    //deleteUser
    public Completable deleteUser(String username);
    
    //getUserByName
    public Single<ModelUser> getUserByName(String username);
    
    //loginUser
    public Single<String> loginUser(String username, String password);
    
    //logoutUser
    public Completable logoutUser();
    
    //updateUser
    public Completable updateUser(String username, ModelUser body);
    
    //uuid
    public Single<InlineResponseDefault> uuid(UUID uuidParam);
    
}
