package com.jinloes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ClientTester {
	private static final String JWT = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzY29wZSI6WyJhcGlfYWNjZXNzIl0sImp0aSI"
			+ "6IjA5ZjljNDVkLTA1NTYtNDY4MS05YWFhLWM4MGNiNWQ5ZjRiYSIsInRlbmFudCI6InRlc3QgdGVuYW50IiwiY2xpZW50X2lkI"
			+ "joiYWNtZSJ9.lQpl3taZros7xzQNVMRaOy7KIrKGkwNKmTPq667kJUQ";
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	public static void main(String[] args) throws IOException, InterruptedException, ExecutionException,
			TimeoutException {
		List<Transport> transports = new ArrayList<>(1);
		transports.add(new WebSocketTransport(new StandardWebSocketClient()));
		WebSocketClient webSocketClient = new SockJsClient(transports);
		WebSocketStompClient stompClient = new WebSocketStompClient(webSocketClient);
		stompClient.setMessageConverter(new StringMessageConverter());
		WebSocketHttpHeaders httpHeaders = new WebSocketHttpHeaders();
		httpHeaders.add("Authorization", "Bearer " + JWT);
		StompSession session = stompClient.connect("ws://localhost:8080/ws", httpHeaders,
				new StompSessionHandlerAdapter() {
				}).get(1, TimeUnit.SECONDS);
		session.subscribe("/clients/topic/events", new StompFrameHandler() {
			@Override
			public Type getPayloadType(StompHeaders headers) {
				return String.class;
			}

			@Override
			public void handleFrame(StompHeaders headers, Object payload) {
				logger.info("Payload: " + payload);
			}
		});
		System.in.read();
	}
}
