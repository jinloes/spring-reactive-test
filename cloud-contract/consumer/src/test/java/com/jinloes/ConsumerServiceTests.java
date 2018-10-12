package com.jinloes;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cloud.contract.stubrunner.StubFinder;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerPort;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
//@ContextConfiguration(initializers = ConsumerServiceTests.TestContextInitializer.class)
@AutoConfigureStubRunner(ids = {"com.jinloes:producer:+:stubs"},
    stubsMode = StubRunnerProperties.StubsMode.LOCAL)
public class ConsumerServiceTests {

  @Autowired
  private ConsumerService consumerService;

  @Test
  public void testGetHello() {
    assertThat(consumerService.getHello()).isEqualTo("val1");
  }


  public static class TestContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
      //TestPropertySourceUtils.addInlinedPropertiesToEnvironment(applicationContext, "producer.port=" + producerPort);
    }
  }

  @TestConfiguration
  static class TestConfig {
    //@StubRunnerPort("producer")
    //private int producerPort;
    @Autowired
    private StubFinder stubFinder;

    @Bean
    public ProducerServiceProvider producerServiceProvider() {
      return () -> Pair.of("localhost", stubFinder.findStubUrl("producer").getPort());
    }
  }
}
