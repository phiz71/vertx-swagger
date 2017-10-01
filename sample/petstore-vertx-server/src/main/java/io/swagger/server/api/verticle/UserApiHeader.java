package io.swagger.server.api.verticle;

import io.swagger.server.api.model.InlineResponseDefault;
import io.swagger.server.api.util.MainApiException;
import io.swagger.server.api.util.MainApiHeader;
import io.swagger.server.api.model.ModelUser;
import io.swagger.server.api.util.ResourceResponse;

import java.time.OffsetDateTime;
import java.util.UUID;

public final class UserApiHeader extends MainApiHeader {
    private UserApiHeader(String name, String value) {
        super(name, value);
    }
    
    private UserApiHeader(String name, Iterable<String> values) {
        super(name, values);
    }
    
    public static UserApiHeader UserApi_loginUser_200_createXRateLimit(Integer xRateLimit) {
        return new UserApiHeader("X-Rate-Limit", xRateLimit.toString());
    }
    
    public static UserApiHeader UserApi_loginUser_200_createXExpiresAfter(OffsetDateTime xExpiresAfter) {
        return new UserApiHeader("X-Expires-After", xExpiresAfter.toString());
    }
    
    

}