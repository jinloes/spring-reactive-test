package com.jinloes.elasticache_redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.invoke.MethodHandles;

@RestController
@RequestMapping("/hello")
public class HelloController {
  private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  @GetMapping(path = "/{id}")
  @Cacheable("jon-test-2-001")
  public String hello(@PathVariable("id") String id) {
    LOGGER.debug("Returning real request.");
    return "hello-" + id;
  }
}
