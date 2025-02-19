package com.learning.spring_security_learning.Controllers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@AllArgsConstructor
@Getter
public class LoginRequestDTO {

  private String username;
  private String password;

}