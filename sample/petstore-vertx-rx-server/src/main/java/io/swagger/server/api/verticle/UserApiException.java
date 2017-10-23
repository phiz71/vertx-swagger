package io.swagger.server.api.verticle;

import io.swagger.server.api.model.InlineResponseDefault;
import io.swagger.server.api.MainApiException;
import io.swagger.server.api.MainApiHeader;
import io.swagger.server.api.model.ModelUser;
import java.time.OffsetDateTime;
import io.swagger.server.api.util.ResourceResponse;
import java.util.UUID;
import io.swagger.server.api.util.VerticleHelper;

public final class UserApiException extends MainApiException {
    public UserApiException(int statusCode, String statusMessage) {
        super(statusCode, statusMessage);
    }
    
    public static UserApiException UserApi_deleteUser_400_createException() {
        return new UserApiException(400, "Invalid username supplied");
    }
    public static UserApiException UserApi_deleteUser_404_createException() {
        return new UserApiException(404, "User not found");
    }
    public static UserApiException UserApi_getUserByName_400_createException() {
        return new UserApiException(400, "Invalid username supplied");
    }
    public static UserApiException UserApi_getUserByName_404_createException() {
        return new UserApiException(404, "User not found");
    }
    public static UserApiException UserApi_loginUser_400_createException() {
        return new UserApiException(400, "Invalid username/password supplied");
    }
    public static UserApiException UserApi_updateUser_400_createException() {
        return new UserApiException(400, "Invalid user supplied");
    }
    public static UserApiException UserApi_updateUser_404_createException() {
        return new UserApiException(404, "User not found");
    }
    

}