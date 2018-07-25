package com.jinloes.elasticache_redis;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.aws.autoconfigure.cache.ElastiCacheAutoConfiguration;
import org.springframework.cloud.aws.cache.config.annotation.CacheClusterConfig;
import org.springframework.cloud.aws.cache.config.annotation.EnableElastiCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
@EnableAutoConfiguration(exclude = ElastiCacheAutoConfiguration.class)
@EnableElastiCache({@CacheClusterConfig(name = "jon-test-2-001")})
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @Bean
  @Profile("local")
  @Primary
  public AWSCredentialsProvider credentialsProvider(@Value("AWS_ACCESS_KEY") String accessKey,
      @Value("AWS_SECRET_ACCESS_KEY") String secretKey) {
    return new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey));
  }

  @Bean
  @Profile("default")
  public AWSCredentialsProvider credentialsProvider() {
    return InstanceProfileCredentialsProvider.getInstance();
  }
}
