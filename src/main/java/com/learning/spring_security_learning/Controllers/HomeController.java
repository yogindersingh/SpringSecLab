package com.learning.spring_security_learning.Controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class HomeController {

  @GetMapping(path = "/home")
  public String home(){
    return "Hello World";
  }

}
