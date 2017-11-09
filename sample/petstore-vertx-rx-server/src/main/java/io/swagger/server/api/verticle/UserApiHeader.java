package io.swagger.server.api.verticle;

import io.swagger.server.api.model.InlineResponseDefault;
import io.swagger.server.api.MainApiException;
import io.swagger.server.api.MainApiHeader;
import io.swagger.server.api.model.ModelUser;
import java.time.OffsetDateTime;
import io.swagger.server.api.util.ResourceResponse;
import java.util.UUID;
import io.swagger.server.api.util.VerticleHelper;

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
    
    public static UserApiHeader UserApi_loginUser_400_createWwWAuthenticate(String wwWAuthenticate) {
        return new UserApiHeader("WWW_Authenticate", wwWAuthenticate.toString());
    }
    
    

}