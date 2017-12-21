package com.jinloes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.invoke.MethodHandles;

/**
 * Created by jinloes on 5/26/16.
 */
@SpringBootApplication
@RestController
@EnableDiscoveryClient
@Scope("request")
public class Application {
	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@Autowired
	private ConfigTest configTest;

	@RequestMapping("/")
	public String home() {
		LOGGER.info("CONFIG VALUE IS {}", configTest.getTest());
		return "Hello Docker World " + configTest.getTest();
	}


	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
