package com.jinloes.jpa_multitenancy;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.hibernate.MultiTenancyStrategy;
import org.hibernate.cfg.Environment;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@EnableJpaRepositories
public class Application {

  @Autowired
  private JpaProperties jpaProperties;

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @Bean
  public JpaVendorAdapter jpaVendorAdapter() {
    return new HibernateJpaVendorAdapter();
  }

  @Bean
  public DataSource dataSource() {
    HikariConfig config = new HikariConfig();
    config.setDriverClassName("org.postgresql.Driver");
    config.setJdbcUrl("jdbc:postgresql://localhost:32770/");
    config.setUsername("postgres");
    HikariDataSource dataSource = new HikariDataSource(config);
    return dataSource;
  }

  @Bean
  public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource,
      MultiTenantConnectionProvider multiTenantConnectionProvider,
      CurrentTenantIdentifierResolver tenantIdentifierResolver) {
    LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();

    em.setDataSource(dataSource);
    em.setJpaVendorAdapter(jpaVendorAdapter());
    em.setPackagesToScan("com.*");

    Map<String, Object> jpaProperties = new HashMap<>();
    jpaProperties.put(Environment.MULTI_TENANT, MultiTenancyStrategy.SCHEMA);
    jpaProperties.put(Environment.MULTI_TENANT_CONNECTION_PROVIDER, multiTenantConnectionProvider);
    jpaProperties.put(Environment.MULTI_TENANT_IDENTIFIER_RESOLVER, tenantIdentifierResolver);
    jpaProperties.put(Environment.FORMAT_SQL, true);
    em.setJpaPropertyMap(jpaProperties);

    return em;
  }
}
