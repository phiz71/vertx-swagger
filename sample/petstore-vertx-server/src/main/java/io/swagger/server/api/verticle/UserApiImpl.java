package io.swagger.server.api.verticle;

import java.util.List;

import io.swagger.server.api.model.User;

public class UserApiImpl implements UserApi {

    @Override
    public void createUser(User body) {

    }

    @Override
    public void createUsersWithArrayInput(List<User> body) {

    }

    @Override
    public void createUsersWithListInput(List<User> body) {

    }

    @Override
    public void deleteUser(String username) {

    }

    @Override
    public User getUserByName(String username) {
        return null;
    }

    @Override
    public String loginUser(String username, String password) {
        return null;
    }

    @Override
    public void logoutUser() {

    }

    @Override
    public void updateUser(String username, User body) {

    }

}
