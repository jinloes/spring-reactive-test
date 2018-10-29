package com.jinloes.docker.authz.service;

import com.jinloes.docker.authz.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
  private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
  private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

  private final UserDetailsManager userDetailsManager;

  @Autowired
  public UserServiceImpl(UserDetailsManager userDetailsManager) {
    this.userDetailsManager = userDetailsManager;
  }

  @Override
  public void create(User user) {
    Optional<User> existing = Optional.ofNullable(userDetailsManager.loadUserByUsername(user.getUsername()))
        .map(User.class::cast);
    existing.ifPresent(it -> {
      throw new IllegalArgumentException("user already exists: " + it.getUsername());
    });

    String hash = encoder.encode(user.getPassword());
    user.setPassword(hash);

    userDetailsManager.createUser(user);

    log.info("new user has been created: {}", user.getUsername());
  }
}
