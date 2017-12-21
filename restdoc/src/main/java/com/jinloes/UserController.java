package com.jinloes;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.lang.invoke.MethodHandles;
import java.util.List;

/**
 * Created by jinloes on 5/16/16.
 */
@RestController("/users")
public class UserController {
	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@RequestMapping(value = "/users/{userId}", method = RequestMethod.GET)
	public Resource<User> getUser(@PathVariable("userId") long userId) {
		Resource<User> user = new Resource<>(new User("First", "Last", new Address("1234 Fake St.", "Somehwere", "CA",
				"12345", "USA")));
		user.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(UserController.class)
				.getDocuments(userId)).withRel("documents"));
		return user;
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public void createUser(@RequestBody User user) {
		LOGGER.debug("Created user", user);
	}

	@RequestMapping(value = "/users/{userId}/documents", method = RequestMethod.GET)
	public List<Document> getDocuments(@PathVariable("userId") long userId) {
		List<Document> documents = Lists.newArrayList(new Document("abc123", "hi"));
		return documents;
	}
}
