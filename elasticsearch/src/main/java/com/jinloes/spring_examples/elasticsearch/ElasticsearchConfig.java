/*
 * *****************************************************************************
 *  Copyright 2018 VMware, Inc.  All rights reserved. -- VMware Confidential
 * *****************************************************************************
 */

package com.jinloes.spring_examples.elasticsearch;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticsearchConfig {
  @Bean
  public RestHighLevelClient client() {
    RestClientBuilder client = RestClient.builder(
        new HttpHost("search-jon-alarm-test-jberstbrwvssyxkfonq3hz3ws4.eu-west-2.es.amazonaws.com", 443, "https"));
    return new RestHighLevelClient(client);
  }
}
