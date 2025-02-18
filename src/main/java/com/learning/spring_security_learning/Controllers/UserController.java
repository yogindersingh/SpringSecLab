package com.learning.spring_security_learning.Controllers;

import com.learning.spring_security_learning.Entities.Customer;
import com.learning.spring_security_learning.Repository.CustomerRepo;
import com.learning.spring_security_learning.Repository.CustomerRolesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class UserController {

  @Autowired
  private CustomerRepo customerRepo;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @PostMapping("/user")
  public ResponseEntity<String> createUser(
      @RequestBody
      Customer customer) {
    if (customerRepo.findByEmail(customer.getEmail()).isPresent()) {
      return new ResponseEntity<>("User already exists", HttpStatus.BAD_REQUEST);
    }
    customer.setPassword(passwordEncoder.encode(customer.getPassword()));
    customerRepo.save(customer);
    return new ResponseEntity<>("User created", HttpStatus.CREATED);
  }


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
