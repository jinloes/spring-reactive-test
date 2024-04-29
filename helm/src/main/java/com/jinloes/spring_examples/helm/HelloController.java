package com.jinloes.spring_examples.helm;

import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {

  @GetMapping(path = "hello")
  public ResponseEntity<?> hello() {
    return ResponseEntity.ok(Map.of("message", "hello"));
  }
}
