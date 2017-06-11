package io.swagger.server.api.verticle;

import java.util.List;

import io.swagger.server.api.model.User;
import io.vertx.core.Future;

public class UserApiImpl implements UserApi {

    @Override
    public void createUser(User body) {
        // TODO Auto-generated method stub

    }

    @Override
    public void createUsersWithArrayInput(List<User> body) {
        // TODO Auto-generated method stub

    }

    @Override
    public void createUsersWithListInput(List<User> body) {
        // TODO Auto-generated method stub

    }

    @Override
    public void deleteUser(String username) {
        // TODO Auto-generated method stub

    }

    @Override
    public Future<User> getUserByName(String username) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Future<String> loginUser(String username, String password) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void logoutUser() {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateUser(String username, User body) {
        // TODO Auto-generated method stub

    }

}
