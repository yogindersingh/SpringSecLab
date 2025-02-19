package com.learning.spring_security_learning.service;

import com.learning.spring_security_learning.Controllers.LoginRequestDTO;
import com.learning.spring_security_learning.Controllers.LoginResponseDTO;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BankService {


  //Enable method level security
  @PreAuthorize("hasRole('READ')")
  public String getResponseString() {
    return "get the balance";
  }


  //Image for method filtering authorization
//
//  @PreFilter("filterObject.username!='firstname'")
//  @PostFilter("filterObject.username!='firstname'")
//  public List<LoginRequestDTO> saveData(List<LoginRequestDTO> data) {
//    return data;
//  }
}
