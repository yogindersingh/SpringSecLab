package com.learning.spring_security_learning.ExceptionHandlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAccessDeniedException implements AccessDeniedHandler {

  @Autowired
  ObjectMapper objectMapper;

  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response,
                     AccessDeniedException accessDeniedException) throws IOException, ServletException {
    ObjectNode objNode = objectMapper.createObjectNode();
    objNode.put("status", HttpStatus.FORBIDDEN.value());
    objNode.put("message", accessDeniedException.getMessage());
    objNode.put("timestamp", System.currentTimeMillis());
    objNode.put("path", request.getRequestURI());
    response.setContentType("application/json");
    response.setStatus(HttpStatus.FORBIDDEN.value());
    response.getWriter().write(objNode.toString());

  }
}
