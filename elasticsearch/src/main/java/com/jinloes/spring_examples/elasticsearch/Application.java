package com.jinloes.spring_examples.elasticsearch;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jinloes.spring_examples.elasticsearch.data.Alarm;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.UUID;

@SpringBootApplication
@RestController
public class Application {
  @Autowired
  private RestHighLevelClient restClient;
  @Autowired
  private ObjectMapper objectMapper;

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @GetMapping("/create")
  public void createAlarm() throws IOException {
    IndexRequest request = new IndexRequest("alarm", "alarm", UUID.randomUUID().toString());
    request.source(objectMapper.writeValueAsString(new Alarm(1, 1)), XContentType.JSON);
    restClient.index(request);
  }
}
