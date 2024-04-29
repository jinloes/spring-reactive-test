package com.jinloes.openapi;

import static io.restassured.RestAssured.given;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.documentationConfiguration;

import com.atlassian.oai.validator.restassured.OpenApiValidationFilter;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FooControllerTest {
  private static final OpenApiValidationFilter VALIDATION_FILTER =
      new OpenApiValidationFilter(new ClassPathResource("example.yaml").getPath());
  private RequestSpecification spec;

  @LocalServerPort
  public void init(int port) {
    RestAssured.port = port;
  }

  @BeforeEach
  public void setUp(RestDocumentationContextProvider restDocumentation) {
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
