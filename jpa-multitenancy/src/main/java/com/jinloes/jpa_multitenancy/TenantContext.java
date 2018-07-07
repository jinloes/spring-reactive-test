package com.jinloes.jpa_multitenancy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

public class TenantContext {
  private static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
  private static ThreadLocal<String> currentTenant = new ThreadLocal<>();

  public static void setCurrentTenant(String tenant) {
    logger.debug("Setting tenant to " + tenant);
    currentTenant.set(tenant);
  }

  public static String getCurrentTenant() {
    return currentTenant.get();
  }

  public static void clear() {
    currentTenant.set(null);
  }
}
