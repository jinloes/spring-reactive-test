package com.jinloes.jpa_multitenancy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/test_models")
public class TestModelController {
  @Autowired
  private TestModelRepository testModelRepository;

  @GetMapping("/{tenantId}")
  public List<TestModel> getTestModels(@PathVariable("tenantId") String tenantId) {
    TenantContext.setCurrentTenant(tenantId);
    return testModelRepository.findAll();
  }
}
