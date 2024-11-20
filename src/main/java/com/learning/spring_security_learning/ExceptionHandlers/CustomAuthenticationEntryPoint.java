package com.learning.spring_security_learning.ExceptionHandlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

  @Autowired
  private ObjectMapper objectMapper;

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
      throws IOException {
    //Custom logic to return custom headers
    response.setHeader("server-response-code", HttpStatus.UNAUTHORIZED.name());
    //response.sendError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());

    //Custom logic to change response body structure
    ObjectNode objNode = objectMapper.createObjectNode();
    objNode.put("status", HttpStatus.UNAUTHORIZED.value());
    objNode.put("message", authException.getMessage());
    objNode.put("timestamp", System.currentTimeMillis());
    objNode.put("path", request.getRequestURI());
    response.setContentType("application/json");
    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    response.getWriter().write(objNode.toString());
  }
}
