package com.jinloes;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.OAuth2Request;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * A custom {@link OAuth2Request} that contains extra information.
 */
public class CustomOAuth2Request extends OAuth2Request {
	private String tenantId;

	public CustomOAuth2Request(String tenantId) {
		this.tenantId = tenantId;
	}

	public CustomOAuth2Request(OAuth2Request other) {
		super(other);
	}

	public CustomOAuth2Request(Map<String, String> requestParameters, String clientId,
			Collection<? extends GrantedAuthority> authorities, boolean approved, Set<String> scope,
			Set<String> resourceIds, String redirectUri, Set<String> responseTypes,
			Map<String, Serializable> extensionProperties) {
		super(requestParameters, clientId, authorities, approved, scope, resourceIds, redirectUri, responseTypes,
				extensionProperties);
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
}
