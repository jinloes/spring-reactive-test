package com.jinloes;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.AuthProvider;
import io.vertx.ext.auth.User;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

/**
 * Created by jinloes on 1/27/17.
 */
public class JwtAuthProvier implements AuthProvider {
	private final JwtTokenStore tokenStore;

	public JwtAuthProvier(JwtTokenStore tokenStore) {
		this.tokenStore = tokenStore;
	}

	@Override
	public void authenticate(JsonObject authInfo, Handler<AsyncResult<User>> resultHandler) {
		try {
			String accessTokenValue = authInfo.getString("jwt");
			OAuth2AccessToken accessToken = tokenStore.readAccessToken(accessTokenValue);
			if (accessToken == null) {
				Future.failedFuture("Invalid access token: " + accessTokenValue);
			} else if (accessToken.isExpired()) {
				tokenStore.removeAccessToken(accessToken);
				Future.failedFuture("Access token expired: " + accessTokenValue);
			} else {
				OAuth2Authentication result = tokenStore.readAuthentication(accessToken);
				if (result == null) {
					Future.failedFuture("Invalid access token: " + accessTokenValue);
				}
			}
			resultHandler.handle(Future.succeededFuture(new OAuthUser(accessToken)));
		} catch (Exception e) {
			Future.failedFuture(e);
		}
	}
}
