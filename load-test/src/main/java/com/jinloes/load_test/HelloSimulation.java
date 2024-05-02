package com.jinloes.load_test;

import static io.gatling.javaapi.core.CoreDsl.StringBody;
import static io.gatling.javaapi.core.CoreDsl.global;
import static io.gatling.javaapi.core.CoreDsl.rampUsersPerSec;
import static io.gatling.javaapi.http.HttpDsl.header;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

import com.github.javafaker.Faker;
import io.gatling.javaapi.core.CoreDsl;
import io.gatling.javaapi.core.OpenInjectionStep;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;
import java.time.Duration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Stream;

public class HelloSimulation extends Simulation {
  private static final HttpProtocolBuilder HTTP_PROTOCOL_BUILDER = setupProtocolForSimulation();
  private static final Iterator<Map<String, Object>> FEED_DATA = setupTestFeedData();
  private static final ScenarioBuilder POST_SCENARIO_BUILDER = buildPostScenario();

  private static HttpProtocolBuilder setupProtocolForSimulation() {
    return http.baseUrl("http://192.168.65.3:30002")
        .acceptHeader("application/json")
        .maxConnectionsPerHost(10)
        .userAgentHeader("Gatling/Performance Test");
  }

  private static Iterator<Map<String, Object>> setupTestFeedData() {
    Faker faker = new Faker();
    Iterator<Map<String, Object>> iterator;
    iterator = Stream.generate(() -> {
          Map<String, Object> stringObjectMap = new HashMap<>();
          stringObjectMap.put("empName", faker.name()
              .fullName());
          return stringObjectMap;
        })
        .iterator();
    return iterator;
  }

  private static ScenarioBuilder buildPostScenario() {
    return CoreDsl.scenario("Load Test Hello")
        .feed(FEED_DATA)
        .exec(
            http("get-hello")
                .get("/hello")
                .check(status().is(200))
        );
  }

  public HelloSimulation() {
    setUp(POST_SCENARIO_BUILDER.injectOpen(postEndpointInjectionProfile())
        .protocols(HTTP_PROTOCOL_BUILDER))
        .assertions(global()
            .responseTime()
            .max()
            .lte(10000), global().successfulRequests()
            .percent()
            .gt(90d));
  }

  private OpenInjectionStep.RampRate.RampRateOpenInjectionStep postEndpointInjectionProfile() {
    int totalDesiredUserCount = 1;
    double userRampUpPerInterval = 50;
    double rampUpIntervalSeconds = 30;
    int totalRampUptimeSeconds = 1;
    int steadyStateDurationSeconds = 1;

    return rampUsersPerSec(userRampUpPerInterval / (rampUpIntervalSeconds / 60)).to(totalDesiredUserCount)
        .during(Duration.ofSeconds(totalRampUptimeSeconds + steadyStateDurationSeconds));
  }
}