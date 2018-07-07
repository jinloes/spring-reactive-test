package com.jinloes.hello;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

@RestController("/hello")
public class HelloController {

  @GetMapping
  public Map<String, Object> hello() throws UnknownHostException {
    Map<String, Object> map = new HashMap<>();
    map.put("message", "Hello from: " + InetAddress.getLocalHost().getHostName());
    return map;
  }
}
