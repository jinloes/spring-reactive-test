package com.jinloes;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.handlers.AsyncHandler;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNSAsync;
import com.amazonaws.services.sns.AmazonSNSAsyncClientBuilder;
import com.amazonaws.services.sns.model.CreateTopicResult;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.Executors;

/**
 * Created by jinloes on 1/27/17.
 */
@RestController
@EnableAsync
@SpringBootApplication
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Autowired
	private ObjectMapper objectMapper;

	@Bean
	public AmazonSNSAsync amazonSns() {
		ClientConfiguration clientConfiguration = new ClientConfiguration();
		clientConfiguration.setMaxConnections(200);
		return AmazonSNSAsyncClientBuilder.standard()
				.withRegion(Regions.US_EAST_1)
				.withExecutorFactory(() -> Executors.newFixedThreadPool(200))
				.withClientConfiguration(clientConfiguration)
				.withCredentials(new EnvironmentVariableCredentialsProvider())
				.build();
	}

	@Bean
	public String topicArn(AmazonSNSAsync amazonSns) {
		CreateTopicResult createTopicResult = amazonSns.createTopic(
				"log_events");
		return createTopicResult.getTopicArn();
	}

	@RequestMapping(value = "/events", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public DeferredResult<ResponseEntity<Event>> createEvent(@RequestBody Event event) throws JsonProcessingException {
		DeferredResult<ResponseEntity<Event>> deferredResult = new DeferredResult<>();
		amazonSns().publishAsync(topicArn(amazonSns()), objectMapper.writeValueAsString(event),
				new AsyncHandler<PublishRequest, PublishResult>() {
					@Override
					public void onError(Exception exception) {
						deferredResult.setResult(new ResponseEntity<>(event,
								HttpStatus.INTERNAL_SERVER_ERROR));
					}

					@Override
					public void onSuccess(PublishRequest request, PublishResult publishResult) {
						deferredResult.setResult(new ResponseEntity<>(event, HttpStatus.CREATED));
					}
				});
		return deferredResult;
	}
}
