package com.github.phiz71.vertx.swagger.router.auth.user;

import io.vertx.core.shareddata.impl.ClusterSerializable;
import io.vertx.ext.auth.User;

public interface ApiUser extends User, ClusterSerializable {
    String getAuthProviderName();
}
