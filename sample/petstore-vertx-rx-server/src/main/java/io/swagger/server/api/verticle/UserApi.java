package io.swagger.server.api.verticle;


import io.swagger.server.api.MainApiException;
import io.swagger.server.api.model.User;
import rx.Single;

import java.util.List;
import java.util.Map;

public interface UserApi  {
    //createUser
    public Single<Void> createUser(User body);
    
    //createUsersWithArrayInput
    public Single<Void> createUsersWithArrayInput(List<User> body);
    
    //createUsersWithListInput
    public Single<Void> createUsersWithListInput(List<User> body);
    
    //deleteUser
    public Single<Void> deleteUser(String username);
    
    //getUserByName
    public Single<User> getUserByName(String username);
    
    //loginUser
    public Single<String> loginUser(String username,String password);
    
    //logoutUser
    public Single<Void> logoutUser();
    
    //updateUser
    public Single<Void> updateUser(String username,User body);
    
}
