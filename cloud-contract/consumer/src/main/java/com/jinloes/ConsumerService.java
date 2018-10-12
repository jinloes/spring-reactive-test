package com.jinloes;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Objects;

@Service
public class ConsumerService {
  private final RestTemplate restTemplate;
  private final ProducerServiceProvider producerServiceProvider;

  @Autowired
  public ConsumerService(RestTemplate restTemplate, ProducerServiceProvider producerServiceProvider) {
    this.restTemplate = restTemplate;
    this.producerServiceProvider = producerServiceProvider;
  }

  public String getHello() {
    Pair<String, Integer> details = producerServiceProvider.getDetails();
    ResponseEntity<Map> response = restTemplate.exchange(String.format("http://localhost:%s/hello", details.getRight()),
        HttpMethod.GET, null, Map.class);
    return Objects.toString(response.getBody().get("key1"));
  }
}
