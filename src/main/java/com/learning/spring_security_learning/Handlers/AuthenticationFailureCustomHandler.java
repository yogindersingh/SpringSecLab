package com.learning.spring_security_learning.Handlers;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class AuthenticationFailureCustomHandler implements AuthenticationFailureHandler {

  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                      AuthenticationException exception) throws IOException, ServletException {
    log.error("Authentication Failed : {}", exception.getMessage());

    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

    response.setContentType("application/json");

    response.getWriter().write("{\"error\": \"Authentication failed\", \"message\": \"" + exception.getMessage() + "\"}");

  }

}
