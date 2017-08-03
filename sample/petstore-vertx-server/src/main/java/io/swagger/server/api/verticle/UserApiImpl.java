package io.swagger.server.api.verticle;

import java.util.List;
import java.util.UUID;

import io.swagger.server.api.model.InlineResponseDefault;
import io.swagger.server.api.model.User;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;

public class UserApiImpl implements UserApi {

    @Override
    public void createUser(User body, Handler<AsyncResult<Void>> handler) {
        // TODO Auto-generated method stub
    }

    @Override
    public void createUsersWithArrayInput(List<User> body, Handler<AsyncResult<Void>> handler) {
        // TODO Auto-generated method stub
    }

    @Override
    public void createUsersWithListInput(List<User> body, Handler<AsyncResult<Void>> handler) {
        // TODO Auto-generated method stub
    }

    @Override
    public void deleteUser(String username, Handler<AsyncResult<Void>> handler) {
        // TODO Auto-generated method stub
    }

    @Override
    public void getUserByName(String username, Handler<AsyncResult<User>> handler) {
        // TODO Auto-generated method stub
    }

    @Override
    public void loginUser(String username, String password, Handler<AsyncResult<String>> handler) {
        // TODO Auto-generated method stub
    }

    @Override
    public void logoutUser(Handler<AsyncResult<Void>> handler) {
        // TODO Auto-generated method stub
    }

    @Override
    public void updateUser(String username, User body, Handler<AsyncResult<Void>> handler) {
        // TODO Auto-generated method stub
    }

    @Override
    public void uuid(UUID uuidParam, Handler<AsyncResult<InlineResponseDefault>> handler) {
        InlineResponseDefault response = new InlineResponseDefault(uuidParam);
        handler.handle(Future.succeededFuture(response));
    }

}
