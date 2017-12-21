package com.jinloes;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

/**
 * Created by jinloes on 1/13/17.
 */
@Configuration
public class WebSocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {

	@Override
	protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
		messages
				.nullDestMatcher().authenticated()
				.simpSubscribeDestMatchers("/user/queue/errors").permitAll()
				.simpDestMatchers("/app/**").hasRole("USER")
				.simpSubscribeDestMatchers("/user/**", "/topic/greeting*").authenticated()
				.simpTypeMatchers(SimpMessageType.CONNECT, SimpMessageType.MESSAGE, SimpMessageType.SUBSCRIBE)
				.authenticated()
				.simpTypeMatchers(SimpMessageType.UNSUBSCRIBE, SimpMessageType.DISCONNECT).permitAll()
				.anyMessage().denyAll();
	}

	@Override
	protected boolean sameOriginDisabled() {
		return true;
	}
}
