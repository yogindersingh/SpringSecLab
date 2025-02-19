package com.learning.spring_security_learning.Controllers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginResponseDTO {

  private String jwtToken;
  private String status;

}