package com.jinloes;

import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.auth.AuthProvider;
import io.vertx.ext.auth.User;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.impl.AuthHandlerImpl;

import java.lang.invoke.MethodHandles;
import java.util.regex.Pattern;

/**
 * Created by jinloes on 1/27/17.
 */
public class OAuth2Handler extends AuthHandlerImpl {
	private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	private static final Pattern BEARER = Pattern.compile("^Bearer$", Pattern.CASE_INSENSITIVE);

	public OAuth2Handler(AuthProvider authProvider) {
		super(authProvider);
	}

	@Override
	public void handle(RoutingContext context) {
		User user = context.user();
		if (user != null) {
			// Already authenticated in, just authorise
			authorise(user, context);
		} else {
			final HttpServerRequest request = context.request();

			String token = null;

			if (request.method() == HttpMethod.OPTIONS
					&& request.headers().get(HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS) != null) {
				for (String ctrlReq : request.headers().get(HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS).split(",")) {
					if (ctrlReq.equalsIgnoreCase("authorization")) {
						// this request has auth in access control
						context.next();
						return;
					}
				}
			}
			final String authorization = request.headers().get(HttpHeaders.AUTHORIZATION);
			if (authorization != null) {
				String[] parts = authorization.split(" ");
				if (parts.length == 2) {
					final String scheme = parts[0],
							credentials = parts[1];

					if (BEARER.matcher(scheme).matches()) {
						token = credentials;
					}
				} else {
					log.warn("Format is Authorization: Bearer [token]");
					context.fail(401);
					return;
				}
			} else {
				log.warn("No Authorization header was found");
				context.fail(401);
				return;
			}

			JsonObject authInfo = new JsonObject().put("jwt", token);

			authProvider.authenticate(authInfo, res -> {
				if (res.succeeded()) {
					final User user2 = res.result();
					context.setUser(user2);
					authorise(user2, context);
				} else {
					log.warn("JWT decode failure", res.cause());
					context.fail(401);
				}
			});
		}
	}
}
