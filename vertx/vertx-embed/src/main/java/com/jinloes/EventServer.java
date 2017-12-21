package com.jinloes;

import com.amazonaws.handlers.AsyncHandler;
import com.amazonaws.services.sns.AmazonSNSAsync;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.stereotype.Component;

/**
 * Created by jinloes on 1/27/17.
 */
@Component
public class EventServer extends AbstractVerticle {
	@Autowired
	private AmazonSNSAsync amazonSns;
	@Autowired
	private String topicArn;
	@Autowired
	private JwtTokenStore jwtTokenStore;

	@Override
	public void start() {
		Router router = Router.router(vertx);

		router.route().handler(BodyHandler.create());
		OAuth2Handler oAuth2Handler = new OAuth2Handler(new JwtAuthProvier(jwtTokenStore));
		oAuth2Handler.addAuthority("create_event");
		router.get("/health").handler(routingContext -> {
			routingContext.response()
					.setStatusCode(200)
					.end();
		});
		router.get("/hello").handler(rc -> {
			rc.response()
					.putHeader("content-type", "application/json")
					.end(new JsonObject().put("greeting", "Hello World!").encode());
		});
		router.route("/events").handler(oAuth2Handler);
		router.post("/events")
				.handler(routingContext -> {
					String message = routingContext.getBodyAsString();
					Event event = Json.decodeValue(message, Event.class);
					amazonSns.publishAsync(topicArn, message,
							new AsyncHandler<PublishRequest, PublishResult>() {
								@Override
								public void onError(Exception exception) {
									routingContext.response()
											.setStatusCode(500)
											.end();
								}

								@Override
								public void onSuccess(PublishRequest request, PublishResult publishResult) {
									routingContext.response()
											.setStatusCode(201)
											.putHeader("Content-Type", "application/json; charset=utf-8")
											.end(Json.encodePrettily(event));
								}
							});
				});
		router.route().handler(StaticHandler.create());
		vertx.createHttpServer().requestHandler(router::accept).listen(8181);
	}
}
