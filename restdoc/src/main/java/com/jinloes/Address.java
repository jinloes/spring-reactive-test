package com.jinloes;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by jinloes on 5/17/16.
 */
public class Address {
	private String street;
	private String city;
	private String state;
	private String zipcode;
	private String country;

	@JsonCreator
	public Address(@JsonProperty("street") String street, @JsonProperty("city") String city,
			@JsonProperty("state") String state, @JsonProperty("zipcode") String zipcode,
			@JsonProperty("country") String country) {
		this.street = street;
		this.city = city;
		this.state = state;
		this.zipcode = zipcode;
		this.country = country;
	}

	public String getStreet() {
		return street;
	}

	public String getCity() {
		return city;
	}

	public String getState() {
		return state;
	}

	public String getZipcode() {
		return zipcode;
	}

	public String getCountry() {
		return country;
	}
}
