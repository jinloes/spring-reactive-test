package com.jinloes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.CaseFormat;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentation;
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.hypermedia.HypermediaDocumentation;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.restdocs.snippet.Attributes;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;

/**
 * Created by jinloes on 5/16/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = Application.class)
public class UserControllerTest {

	@Rule
	public RestDocumentation restDocumentation = new RestDocumentation("build/generated-snippets");

	@Autowired
	private WebApplicationContext context;

	@Autowired
	private ObjectMapper objectMapper;

	private MockMvc mockMvc;
	private RestDocumentationResultHandler document;

	@Before
	public void setUp() {
		this.document = MockMvcRestDocumentation.document("{method-name}",
				Preprocessors.preprocessResponse(Preprocessors.prettyPrint()));
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
				.apply(MockMvcRestDocumentation.documentationConfiguration(this.restDocumentation))
				.alwaysDo(document)
				.build();
	}

	@Test
	public void getUser() throws Exception {
		document.snippets(RequestDocumentation.pathParameters(
				RequestDocumentation.parameterWithName("userId").description("The user's id")),
				HypermediaDocumentation.links(
						HypermediaDocumentation.linkWithRel("documents").description("User's documents")),
				PayloadDocumentation.responseFields(
						PayloadDocumentation.fieldWithPath("first_name").description("User's first name"),
						PayloadDocumentation.fieldWithPath("last_name").description("User's last name"),
						PayloadDocumentation.fieldWithPath("address.street").description("User's street address"),
						PayloadDocumentation.fieldWithPath("address.city").description("User's city"),
						PayloadDocumentation.fieldWithPath("address.state").description("User's state"),
						PayloadDocumentation.fieldWithPath("address.zipcode").description("User's zipcode"),
						PayloadDocumentation.fieldWithPath("address.country").description("User's country"),
						PayloadDocumentation.fieldWithPath("links").description("Relational links")));
		this.mockMvc.perform(RestDocumentationRequestBuilders.get("/users/{userId}", 1)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.first_name").value("First"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.last_name").value("Last"));
	}

	@Test
	public void createUser() throws Exception {
		ConstrainedFields fields = new ConstrainedFields(User.class);
		User user = new User("first", "last", new Address("", "", "", "", ""));
		document.snippets(PayloadDocumentation.requestFields(
				fields.withPath("first_name")
						.type(JsonFieldType.STRING)
						.description("The user's first name"),
				fields.withPath("last_name")
						.type(JsonFieldType.STRING)
						.optional()
						.description("The user's last name"),
				fields.withPath("address.street")
						.type(JsonFieldType.STRING)
						.optional()
						.description("The user's street address"),
				fields.withPath("address.city")
						.type(JsonFieldType.STRING)
						.optional()
						.description("The user's city"),
				fields.withPath("address.state")
						.type(JsonFieldType.STRING)
						.optional()
						.description("The user's state"),
				fields.withPath("address.zipcode")
						.type(JsonFieldType.STRING)
						.optional()
						.description("The user's zipcode"),
				fields.withPath("address.country")
						.type(JsonFieldType.STRING)
						.optional()
						.description("The user's country")));
		this.mockMvc.perform(MockMvcRequestBuilders.post("/users")
				.content(objectMapper.writeValueAsString(user))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isCreated());
	}

	private static class ConstrainedFields {

		private final ConstraintDescriptions constraintDescriptions;

		ConstrainedFields(Class<?> input) {
			this.constraintDescriptions = new ConstraintDescriptions(input);
		}

		private FieldDescriptor withPath(String path) {
			String formattedPath = CaseFormat.LOWER_UNDERSCORE.converterTo(CaseFormat.LOWER_CAMEL).convert(path);
			return PayloadDocumentation.fieldWithPath(path)
					.attributes(Attributes.key("constraints")
							.value(StringUtils.collectionToDelimitedString(
									this.constraintDescriptions.descriptionsForProperty(formattedPath), ". ")));
		}
	}
}
