package com.jinloes;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
	private String jwtSigningKey = "456";
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private MessageSource messageSource;

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.exceptionHandling().authenticationEntryPoint(restAuthenticationEntryPoint())
				.and()
				.authorizeRequests()
				.antMatchers(HttpMethod.GET, "/long_poll").permitAll()
				.antMatchers(HttpMethod.GET, "/docs/**").permitAll()
				.antMatchers(HttpMethod.GET, "/health").permitAll()
				.antMatchers(HttpMethod.GET, "/info").permitAll()
				.antMatchers(HttpMethod.GET, "/metrics").permitAll()
				.antMatchers("/ws/**").access("#oauth2.hasScope('api_access')")
				.anyRequest().authenticated();
	}

	@Override
	public void configure(ResourceServerSecurityConfigurer config) {

	}

	@Bean
	public TokenStore tokenStore() {
		return new JwtTokenStore(accessTokenConverter());
	}

	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter converter = new CustomJwtAccessTokenConverter();
		converter.setSigningKey(jwtSigningKey);
		return converter;
	}

	@Bean
	public RestAuthenticationEntryPoint restAuthenticationEntryPoint() {
		return new RestAuthenticationEntryPoint(objectMapper, messageSource);
	}
}
