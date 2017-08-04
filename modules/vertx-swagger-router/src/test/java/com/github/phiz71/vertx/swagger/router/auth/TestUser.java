package com.github.phiz71.vertx.swagger.router.auth;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.AbstractUser;
import io.vertx.ext.auth.AuthProvider;

public class TestUser extends AbstractUser {

	String username;
	
	public TestUser(String username) {
		super();
		this.username = username;
	}

	@Override
	public void setAuthProvider(AuthProvider arg0) {
		// Not necessary for the test
	}
	
	@Override
	public JsonObject principal() {
		return new JsonObject().put("username", username);
	}
	
	@Override
	protected void doIsPermitted(String arg0, Handler<AsyncResult<Boolean>> arg1) {
		// Not necessary for the test
		
	}

	
}
