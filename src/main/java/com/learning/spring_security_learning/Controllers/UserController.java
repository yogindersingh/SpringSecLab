package com.learning.spring_security_learning.Controllers;

import com.learning.spring_security_learning.Entities.Customer;
import com.learning.spring_security_learning.Repository.CustomerRepo;
import com.learning.spring_security_learning.Repository.CustomerRolesRepo;
import com.learning.spring_security_learning.constants.ApplicationConstants;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class UserController {

  @Autowired
  private CustomerRepo customerRepo;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  Environment env;

  @Autowired
  AuthenticationManager authenticationManager;

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


  @PostMapping("/apiLogin")
  public ResponseEntity<LoginResponseDTO> apiLogin (@RequestBody LoginRequestDTO loginRequest) {
    String jwt = "";
    Authentication authentication = UsernamePasswordAuthenticationToken.unauthenticated(loginRequest.getUsername(),
        loginRequest.getPassword());
    Authentication authenticationResponse = authenticationManager.authenticate(authentication);
    if(null != authenticationResponse && authenticationResponse.isAuthenticated()) {
      if (null != env) {
        String secret = env.getProperty(ApplicationConstants.JWT_SECRET_KEY,
            ApplicationConstants.JWT_SECRET_DEFAULT_VALUE);
        SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        jwt = Jwts.builder().issuer("SpringSecLab").subject("JWT Token")
            .claim("username", authenticationResponse.getName())
            .claim("authorities", authenticationResponse.getAuthorities().stream().map(
                GrantedAuthority::getAuthority).collect(Collectors.joining(",")))
            .issuedAt(new java.util.Date())
            .expiration(new java.util.Date((new java.util.Date()).getTime() + 30000000))
            .signWith(secretKey).compact();
      }
    }
    return ResponseEntity.status(HttpStatus.OK).header(ApplicationConstants.JWT_HEADER,jwt)
        .body(new LoginResponseDTO(HttpStatus.OK.getReasonPhrase(), jwt));
  }
}
