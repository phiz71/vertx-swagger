package io.swagger.server.api.verticle;


import io.swagger.server.api.MainApiException;
import io.swagger.server.api.model.User;
import io.vertx.core.Future;

import java.util.List;
import java.util.Map;

public interface UserApi  {
    //createUser
    public void createUser(User body) throws UserApiException;
    
    //createUsersWithArrayInput
    public void createUsersWithArrayInput(List<User> body) throws UserApiException;
    
    //createUsersWithListInput
    public void createUsersWithListInput(List<User> body) throws UserApiException;
    
    //deleteUser
    public void deleteUser(String username) throws UserApiException;
    
    //getUserByName
    public Future<User> getUserByName(String username) throws UserApiException;
    
    //loginUser
    public Future<String> loginUser(String username,String password) throws UserApiException;
    
    //logoutUser
    public void logoutUser() throws UserApiException;
    
    //updateUser
    public void updateUser(String username,User body) throws UserApiException;
    
}
