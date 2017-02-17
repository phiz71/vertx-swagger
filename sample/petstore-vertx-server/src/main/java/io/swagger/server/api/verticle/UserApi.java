package io.swagger.server.api.verticle;

import io.swagger.server.api.model.User;

import java.util.List;
import java.util.Map;

public interface UserApi  {
    //createUser
    public void createUser(User body);
    
    //createUsersWithArrayInput
    public void createUsersWithArrayInput(List<User> body);
    
    //createUsersWithListInput
    public void createUsersWithListInput(List<User> body);
    
    //deleteUser
    public void deleteUser(String username);
    
    //getUserByName
    public User getUserByName(String username);
    
    //loginUser
    public String loginUser(String username,String password);
    
    //logoutUser
    public void logoutUser();
    
    //updateUser
    public void updateUser(String username,User body);
    
}
