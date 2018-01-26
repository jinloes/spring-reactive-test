package com.jinloes.openapi;

import com.atlassian.oai.validator.restassured.SwaggerValidationFilter;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static io.restassured.RestAssured.given;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.documentationConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FooControllerTest {
  @Rule
  public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");
  private static final SwaggerValidationFilter VALIDATION_FILTER =
      new SwaggerValidationFilter(new ClassPathResource("example.yaml").getPath());
  private RequestSpecification spec;

  @LocalServerPort
  public void init(int port) {
    RestAssured.port = port;
  }

  @Before
  public void setUp() {
    this.spec = new RequestSpecBuilder()
        .addFilter(VALIDATION_FILTER)
        .addFilter(documentationConfiguration(restDocumentation)
            .operationPreprocessors()
            .withResponseDefaults(prettyPrint()))
        .addFilter(document("{method_name}"))
        .build();
  }

  @Test
  public void getFoos() {
    given(spec)
        .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
        .when()
        .get("/foos")
        .then()
        .assertThat()
        .statusCode(200);
  }

  @Test
  public void getFoo() {
    given(spec)
        .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
        .when()
        .get("/foos/{fooId}", 123)
        .then()
        .assertThat()
        .statusCode(200);
  }
}
