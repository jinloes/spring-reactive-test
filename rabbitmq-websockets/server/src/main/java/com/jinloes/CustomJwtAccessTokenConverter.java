package com.jinloes;

import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.Map;

/**
 * A custon {@link JwtAccessTokenConverter} that parses extra information from the jwt.
 */
public class CustomJwtAccessTokenConverter extends JwtAccessTokenConverter {
	@Override
	public OAuth2Authentication extractAuthentication(Map<String, ?> map) {
		OAuth2Authentication authentication = super.extractAuthentication(map);
		CustomOAuth2Request customRequest = new CustomOAuth2Request(authentication.getOAuth2Request());
		customRequest.setTenantId((String) map.get("tenant"));
		return new OAuth2Authentication(customRequest, authentication.getUserAuthentication());
	}
}
