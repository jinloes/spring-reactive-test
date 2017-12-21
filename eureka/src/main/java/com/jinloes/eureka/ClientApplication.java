package com.jinloes.eureka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@EnableDiscoveryClient
@SpringBootApplication
public class ClientApplication {
  public static void main(String[] args) {
    SpringApplication.run(ClientApplication.class, args);
  }
}

@RestController
class ServiceInstances{
  private final DiscoveryClient discoveryClient;

  @Autowired
  public ServiceInstances(DiscoveryClient discoveryClient) {
    this.discoveryClient = discoveryClient;
  }

  @GetMapping(value = "/services/{applicationName}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public List<ServiceInstance> getServiceInstances(@PathVariable("applicationName") String applicationName) {
    return discoveryClient.getInstances(applicationName);
  }
}
