package com.jinloes.openapi;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@SpringBootApplication
public class Application {
  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @RestController
  @RequestMapping("/foos")
  public static class FooController {

    @GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<Map<String, Object>>> getFoos() {
      return ResponseEntity.<List<Map<String, Object>>>ok(Lists.<Map<String, Object>>newArrayList(
          ImmutableMap.<String, Object>of("val1", "abc123", "val2", 123))
      );
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Map<String, Object>> getFoos(@PathVariable("id") long id) {
      return ResponseEntity.<Map<String, Object>>ok(ImmutableMap.<String, Object>of("val1", "abc123", "val2", 123));
    }
  }
}
