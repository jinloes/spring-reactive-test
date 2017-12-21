package com.jinloes;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.server.RequestUpgradeStrategy;
import org.springframework.web.socket.server.standard.TomcatRequestUpgradeStrategy;

@Configuration
@EnableWebSocket
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		config.enableStompBrokerRelay("/topic")
				.setSystemLogin("jon")
				.setSystemPasscode("jon")
				.setClientLogin("jon")
				.setClientPasscode("jon")
				.setRelayHost("10.3.6.148")
				.setRelayPort(61613);
		config.setUserDestinationPrefix("/clients/");
	}

	@Override
	public void configureClientInboundChannel(ChannelRegistration registration) {
		registration.setInterceptors(new ChannelInterceptorAdapter() {

			@Override
			public Message<?> preSend(Message<?> message, MessageChannel channel) {
				StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
				accessor.getUser();
				if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
					if (accessor.getDestination().matches("^.*-user.+$")) {
						throw new IllegalArgumentException("Cannot join a user's session directly.");
					}
				}
				return message;
			}
		});
	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		RequestUpgradeStrategy upgradeStrategy = new TomcatRequestUpgradeStrategy();

		registry.addEndpoint("/ws")
				/*.setHandshakeHandler(new DefaultHandshakeHandler() {
					@Override
					protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler,
							Map<String, Object> attributes) {
						OAuth2Authentication authentication = (OAuth2Authentication) request.getPrincipal();
						CustomOAuth2Request oAuth2Request = ((CustomOAuth2Request) authentication.getOAuth2Request());
						return new UserPrincipal(oAuth2Request.getTenantId());
					}
				})*/
				.withSockJS();
	}
}
