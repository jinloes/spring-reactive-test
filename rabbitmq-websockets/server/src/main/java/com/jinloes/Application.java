package com.jinloes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.lang.invoke.MethodHandles;

@SpringBootApplication
@EnableScheduling
public class Application {
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	@Autowired
	private SimpMessagingTemplate template;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Scheduled(initialDelay = 3000, fixedRate = 1000)
	public void test() {
		//logger.info("sending hi");
		template.convertAndSendToUser("acme", "/topic/events",
				"hi-" + System.currentTimeMillis());
	}
}
