package com.learning.spring_security_learning.Events;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.security.authorization.event.AuthorizationDeniedEvent;

@Configuration
@Slf4j
public class ProjectAuthorizationEvents {

  @EventListener
  public void onFailureAuthorization(AuthorizationDeniedEvent authentication) {
    log.info("Authorization Denied : " + authentication.getAuthentication().get());
  }
}
