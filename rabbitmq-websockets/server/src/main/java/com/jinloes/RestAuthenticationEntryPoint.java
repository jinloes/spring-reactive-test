package com.jinloes;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Entry point for the rest API authentication.
 */
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
	private final ObjectMapper objectMapper;
	private final MessageSource messageSource;

	public RestAuthenticationEntryPoint(ObjectMapper objectMapper, MessageSource messageSource) {
		this.objectMapper = objectMapper;
		this.messageSource = messageSource;
	}

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException
			authenticationException) throws IOException, ServletException {
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.getWriter().write(objectMapper.writeValueAsString(""));

	}
}
