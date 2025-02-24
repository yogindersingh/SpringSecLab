package com.learning.spring_security_learning.Controllers;

import com.learning.spring_security_learning.Entities.Customer;
import com.learning.spring_security_learning.Repository.CustomerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class UserController {

  @Autowired
  private CustomerRepo customerRepo;

  @GetMapping("/user")
  public ResponseEntity<?> getUser() {
    String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
    Optional<Customer> customer = customerRepo.findByEmail(userEmail);
    if (customer.isEmpty()) {
      return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<>(customer, HttpStatus.CREATED);
  }

}
