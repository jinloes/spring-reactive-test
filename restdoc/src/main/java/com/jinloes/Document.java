package com.jinloes;

/**
 * Created by jinloes on 5/16/16.
 */
public class Document {
	private String id;
	private String name;

	public Document(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}
}
