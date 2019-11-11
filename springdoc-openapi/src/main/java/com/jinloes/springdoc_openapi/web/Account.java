package com.jinloes.springdoc_openapi.web;

public class Account {
    private final String name;

    public Account(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
