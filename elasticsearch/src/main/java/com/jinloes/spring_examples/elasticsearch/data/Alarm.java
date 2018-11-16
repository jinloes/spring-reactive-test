package com.jinloes.spring_examples.elasticsearch.data;

public class Alarm {
  private int id;
  private int org;

  public Alarm(int id, int org) {
    this.id = id;
    this.org = org;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getOrg() {
    return org;
  }

  public void setOrg(int org) {
    this.org = org;
  }
}
