package com.jinloes;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * Created by jinloes on 6/2/16.
 */
@Configuration
@Scope("prototype")
public class ConfigTest {
	@Value("${test.a}")
	private String test;

	public String getTest() {
		return test;
	}
}
