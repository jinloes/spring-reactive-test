package com.jinloes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ClientTester2 {
	private static final String JWT = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzY29wZSI6WyJhcGlfYWNjZXNzIl0sImp0aSI6I"
			+ "mJkNjM5ZDQ5LTM1MGYtNDlmYi05NGE2LTNlNDc1Zjk3N2JkZiIsInRlbmFudCI6InRlc3QgdGVuYW50MiIsImNsaWVudF9pZCI6ImF"
			+ "jbWUyIn0.j25iHR0cmzKAGYC5p-aOFk6io1mg4tZ3GX2wrbjlIBo";
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	public static void main(String[] args) throws IOException {
		List<Transport> transports = new ArrayList<>(1);
		transports.add(new WebSocketTransport(new StandardWebSocketClient()));
		WebSocketClient webSocketClient = new SockJsClient(transports);
		WebSocketStompClient stompClient = new WebSocketStompClient(webSocketClient);
		stompClient.setMessageConverter(new StringMessageConverter());
		StompSessionHandler sessionHandler = new StompSessionHandlerAdapter() {
			@Override
			public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
				StompHeaders headers = new StompHeaders();
				headers.setDestination("/topic/greeting-user2ab06a72e1214d2fac0b72009fec2eb0");
				session.subscribe(headers, new StompFrameHandler() {
					@Override
					public Type getPayloadType(StompHeaders headers) {
						return String.class;
					}

					@Override
					public void handleFrame(StompHeaders headers, Object payload) {
						logger.info("Payload: " + payload);
					}
				});
			}
		};
		WebSocketHttpHeaders httpHeaders = new WebSocketHttpHeaders();
		httpHeaders.add("Authorization", "Bearer " + JWT);
		stompClient.connect("ws://localhost:8080/ws", httpHeaders, sessionHandler);
		System.in.read();
	}
}
