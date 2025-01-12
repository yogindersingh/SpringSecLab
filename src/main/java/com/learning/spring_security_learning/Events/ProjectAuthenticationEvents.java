package com.learning.spring_security_learning.Events;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;

@Configuration
@Slf4j
public class ProjectAuthenticationEvents {

  @EventListener
  public void onSuccessfulAuthentication(AuthenticationSuccessEvent authentication) {
    log.info("Authentication successful");
  }

  @EventListener
  public void onFailureAuthentication(AbstractAuthenticationFailureEvent authentication) {
    log.info("Authentication failure");
  }
}
