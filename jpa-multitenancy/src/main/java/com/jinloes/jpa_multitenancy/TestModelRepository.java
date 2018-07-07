package com.jinloes.jpa_multitenancy;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TestModelRepository extends JpaRepository<TestModel, UUID> {
}
