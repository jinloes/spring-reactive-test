package com.jinloes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@RestController
public class Application {

  @GetMapping("/hello")
  public Map<String, String> home() {
    Map<String, String> map = new HashMap<>();
    map.put("key1", "val1");
    return map;
  }

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}
