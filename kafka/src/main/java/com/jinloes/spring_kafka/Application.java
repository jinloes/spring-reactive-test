package com.jinloes.spring_kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.lang.invoke.MethodHandles;

@SpringBootApplication
@EnableScheduling
public class Application {
  private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
  @Autowired
  private KafkaTemplate<String, String> kafkaTemplate;

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @Scheduled(fixedDelay = 1000)
  public void sendMessage() {
    LOGGER.info("Sending hello");
    kafkaTemplate.send("hi", "hello world");
  }

  @KafkaListener(topics = "hi")
  public void listen(@Payload String message, @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition) {
    LOGGER.info("Received message {} from partition {}", message, partition);
  }
}
