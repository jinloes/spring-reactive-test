package com.jinloes;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNSAsync;
import com.amazonaws.services.sns.AmazonSNSAsyncClientBuilder;
import com.amazonaws.services.sns.model.CreateTopicResult;
import io.vertx.core.Vertx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.consul.ConditionalOnConsulEnabled;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.util.concurrent.Executors;
import javax.annotation.PostConstruct;

/**
 * Created by jinloes on 1/27/17.
 */
@ConditionalOnConsulEnabled
@SpringBootApplication
public class Application {
	@Autowired
	private EventServer eventServer;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@PostConstruct
	public void deployVerticle() {
		Vertx.vertx().deployVerticle(eventServer);
	}

	@Bean
	public AmazonSNSAsync amazonSns() {
		ClientConfiguration clientConfiguration = new ClientConfiguration()
				.withMaxConnections(300);
		return AmazonSNSAsyncClientBuilder.standard()
				.withRegion(Regions.US_EAST_1)
				.withExecutorFactory(() -> Executors.newFixedThreadPool(150))
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

	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter();
		accessTokenConverter.setSigningKey("123");
		return accessTokenConverter;
	}

	@Bean
	public JwtTokenStore jwtTokenStore() {
		return new JwtTokenStore(accessTokenConverter());
	}

	@Bean
	public JwtAuthProvier jwtAuthProvier() {
		return new JwtAuthProvier(jwtTokenStore());
	}
}
