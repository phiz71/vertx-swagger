package io.swagger.server.api.verticle;

import io.swagger.server.api.model.User;

import java.util.List;
import java.util.Map;

public interface UserApi  {
    //POST_user
    public void createUser(User body);
    
    //POST_user_createWithArray
    public void createUsersWithArrayInput(List<User> body);
    
    //POST_user_createWithList
    public void createUsersWithListInput(List<User> body);
    
    //DELETE_user_username
    public void deleteUser(String username);
    
    //GET_user_username
    public User getUserByName(String username);
    
    //GET_user_login
    public String loginUser(String username,String password);
    
    //GET_user_logout
    public void logoutUser();
    
    //PUT_user_username
    public void updateUser(String username,User body);
    
}
