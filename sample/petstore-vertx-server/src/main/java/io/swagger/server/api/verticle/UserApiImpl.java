package io.swagger.server.api.verticle;

import io.swagger.server.api.model.InlineResponseDefault;
import io.swagger.server.api.model.ModelUser;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;

import java.util.List;
import java.util.UUID;

public class UserApiImpl implements UserApi {

    @Override
    public void createUser(ModelUser body, Handler<AsyncResult<Void>> handler) {
        // TODO Auto-generated method stub
    }

    @Override
    public void createUsersWithArrayInput(List<ModelUser> body, Handler<AsyncResult<Void>> handler) {
        // TODO Auto-generated method stub
    }

    @Override
    public void createUsersWithListInput(List<ModelUser> body, Handler<AsyncResult<Void>> handler) {
        // TODO Auto-generated method stub
    }

    @Override
    public void deleteUser(String username, Handler<AsyncResult<Void>> handler) {
        // TODO Auto-generated method stub
    }

    @Override
    public void getUserByName(String username, Handler<AsyncResult<ModelUser>> handler) {
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
    public void updateUser(String username, ModelUser body, Handler<AsyncResult<Void>> handler) {
        // TODO Auto-generated method stub
    }

    @Override
    public void uuid(UUID uuidParam, Handler<AsyncResult<InlineResponseDefault>> handler) {
        InlineResponseDefault response = new InlineResponseDefault(uuidParam);
        handler.handle(Future.succeededFuture(response));
    }

}
