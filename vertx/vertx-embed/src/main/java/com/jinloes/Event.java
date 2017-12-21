package com.jinloes;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by jinloes on 1/27/17.
 */
public class Event {
	private final String name;
	private final String user;
	private final String serial;

	@JsonCreator
	public Event(@JsonProperty("name") String name, @JsonProperty("user") String user,
			@JsonProperty("serial") String serial) {
		this.name = name;
		this.user = user;
		this.serial = serial;
	}

	public String getName() {
		return name;
	}

	public String getUser() {
		return user;
	}

	public String getSerial() {
		return serial;
	}
}
