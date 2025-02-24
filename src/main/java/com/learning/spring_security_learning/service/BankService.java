package com.learning.spring_security_learning.service;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class BankService {


  //Enable method level security
  @PreAuthorize("hasRole('READ')")
  public String getResponseString() {
    return "get the balance";
  }

}
