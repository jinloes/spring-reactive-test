package com.jinloes;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;

import javax.validation.constraints.NotNull;

/**
 * Created by jinloes on 5/16/16.
 */
public class User {
	@NotNull
	private String firstName;
	private String lastName;
	private Address address;

	@JsonCreator
	public User(@JsonProperty("first_name") String firstName,
			@JsonProperty("last_name") String lastName, @JsonProperty("address") Address address) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
	}

	@JsonProperty("first_name")
	public String getFirstName() {
		return firstName;
	}

	@JsonProperty("last_name")
	public String getLastName() {
		return lastName;
	}

	public Address getAddress() {
		return address;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.add("firstName", firstName)
				.add("lastName", lastName)
				.add("address", address)
				.toString();
	}
}
