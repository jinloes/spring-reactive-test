package com.jinloes.jpa_multitenancy;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.stereotype.Component;

@Component
public class TenantIdentifierResolver implements CurrentTenantIdentifierResolver {
  @Override
  public String resolveCurrentTenantIdentifier() {
    String tenantId = TenantContext.getCurrentTenant();
    if (tenantId != null) {
      return tenantId;
    }
    return "public";
  }

  @Override
  public boolean validateExistingCurrentSessions() {
    return true;
  }
}