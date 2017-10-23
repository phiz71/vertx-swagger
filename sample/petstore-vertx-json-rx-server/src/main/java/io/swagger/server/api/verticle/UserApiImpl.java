package io.swagger.server.api.verticle;

import io.swagger.server.api.model.InlineResponseDefault;
import io.swagger.server.api.model.ModelUser;
import io.swagger.server.api.util.ResourceResponse;
import io.vertx.core.json.JsonArray;
import rx.Single;

import java.util.List;
import java.util.UUID;

public class UserApiImpl implements UserApi {

    @Override
    public Single<ResourceResponse<Void>> createUser(ModelUser body) {
        return returnVoid();
    }

    @Override
    public Single<ResourceResponse<Void>> createUsersWithArrayInput(JsonArray body) {
        return returnVoid();
    }

    @Override
    public Single<ResourceResponse<Void>> createUsersWithListInput(JsonArray body) {
        return returnVoid();
    }

    @Override
    public Single<ResourceResponse<Void>> deleteUser(String username) {
        return returnVoid();
    }

    @Override
    public Single<ResourceResponse<ModelUser>> getUserByName(String username) {
        ResourceResponse<ModelUser> response = new ResourceResponse<>();
        response.addHeader(UserApiHeader.CONTENT_TYPE_JSON);
        response.setResponse(new ModelUser());
        return Single.just(response);
    }

    @Override
    public Single<ResourceResponse<String>> loginUser(String username, String password) {

        if( "foo".equalsIgnoreCase(username) && "bar".equalsIgnoreCase(password)) {
            ResourceResponse<String> response = new ResourceResponse<>();
            response.addHeader(UserApiHeader.CONTENT_TYPE_JSON);
            response.setResponse("OK");
            response.addHeader(UserApiHeader.UserApi_loginUser_200_createXRateLimit(1));
            return Single.just(response);
        } else {
            UserApiException e = UserApiException.UserApi_loginUser_400_createException();
            e.addHeader(UserApiHeader.UserApi_loginUser_400_createWwWAuthenticate("Basic"));
            return Single.error(e);
        }

    }

    @Override
    public Single<ResourceResponse<Void>> logoutUser() {
        return returnVoid();
    }

    @Override
    public Single<ResourceResponse<Void>> updateUser(String username, ModelUser body) {
        return returnVoid();
    }

    @Override
    public Single<ResourceResponse<InlineResponseDefault>> uuid(String uuidParam) {
        ResourceResponse<InlineResponseDefault> response = new ResourceResponse<>();
        response.addHeader(UserApiHeader.CONTENT_TYPE_JSON);
        response.setResponse(new InlineResponseDefault(uuidParam));
        
        return Single.just(response);
    }

    private Single<ResourceResponse<Void>> returnVoid() {
        ResourceResponse<Void> response = new ResourceResponse<>();
        response.addHeader(UserApiHeader.CONTENT_TYPE_JSON);
        return Single.just(response);
    }
}
