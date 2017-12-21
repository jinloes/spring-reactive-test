package com.jinloes;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.AbstractUser;
import io.vertx.ext.auth.AuthProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

import java.lang.invoke.MethodHandles;

/**
 * Created by jinloes on 1/27/17.
 */
public class OAuthUser extends AbstractUser {
	private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	private final OAuth2AccessToken accessToken;

	public OAuthUser(OAuth2AccessToken accessToken) {
		this.accessToken = accessToken;
	}

	@Override
	protected void doIsPermitted(String permission, Handler<AsyncResult<Boolean>> handler) {
		if (accessToken.getScope().contains(permission)) {
			handler.handle(Future.succeededFuture(true));
			return;
		}

		log.debug("User has no permission [" + permission + "]");
		handler.handle(Future.succeededFuture(false));
	}

	@Override
	public JsonObject principal() {
		JsonObject jsonObject = new JsonObject();
		jsonObject.put("token", accessToken);
		return jsonObject;
	}

	@Override
	public void setAuthProvider(AuthProvider authProvider) {
		// NOOP - we're using JWT in the oauth token
	}
}
