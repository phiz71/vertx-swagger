package io.swagger.server.api.verticle;

import io.swagger.server.api.model.InlineResponseDefault;
import io.swagger.server.api.model.ModelUser;
import io.swagger.server.api.util.ResourceResponse;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;

import java.util.List;
import java.util.UUID;

public class UserApiImpl implements UserApi {

    @Override
  public void createUser(ModelUser body, Handler<AsyncResult<ResourceResponse<Void>>> handler) {
    // TODO Auto-generated method stub
  }

  @Override
  public void createUsersWithArrayInput(List<ModelUser> body,
      Handler<AsyncResult<ResourceResponse<Void>>> handler) {
    // TODO Auto-generated method stub
  }

  @Override
  public void createUsersWithListInput(List<ModelUser> body,
      Handler<AsyncResult<ResourceResponse<Void>>> handler) {
    // TODO Auto-generated method stub
  }

  @Override
  public void deleteUser(String username, Handler<AsyncResult<ResourceResponse<Void>>> handler) {
    // TODO Auto-generated method stub
  }

  @Override
  public void getUserByName(String username,
      Handler<AsyncResult<ResourceResponse<ModelUser>>> handler) {
    // TODO Auto-generated method stub
  }

  @Override
  public void loginUser(String username, String password, Handler<AsyncResult<ResourceResponse<String>>> handler) {
      if( "foo".equalsIgnoreCase(username) && "bar".equalsIgnoreCase(password)) {
          ResourceResponse<String> response = new ResourceResponse<>();
          response.addHeader(UserApiHeader.CONTENT_TYPE_JSON);
          response.setResponse("OK");
          response.addHeader(UserApiHeader.UserApi_loginUser_200_createXRateLimit(1));
          handler.handle(Future.succeededFuture(response));
      } else {
          UserApiException e = UserApiException.UserApi_loginUser_400_createException();
          e.addHeader(UserApiHeader.UserApi_loginUser_400_createWwWAuthenticate("Basic"));
          handler.handle(Future.failedFuture(e));
      }
  }

  @Override
  public void logoutUser(Handler<AsyncResult<ResourceResponse<Void>>> handler) {
      ResourceResponse<Void> response = new ResourceResponse<>();
      handler.handle(Future.succeededFuture(response));
  }

  @Override
  public void updateUser(String username, ModelUser body,
      Handler<AsyncResult<ResourceResponse<Void>>> handler) {
    // TODO Auto-generated method stub
  }

    @Override
    public void uuid(UUID uuidParam, Handler<AsyncResult<ResourceResponse<InlineResponseDefault>>> handler) {
        InlineResponseDefault inlineResponse = new InlineResponseDefault(uuidParam);
        ResourceResponse<InlineResponseDefault> response = new ResourceResponse<>();
        response.setResponse(inlineResponse);
        response
          .addHeader(UserApiHeader.CONTENT_TYPE_JSON);
        handler.handle(Future.succeededFuture(response));
    }

}
