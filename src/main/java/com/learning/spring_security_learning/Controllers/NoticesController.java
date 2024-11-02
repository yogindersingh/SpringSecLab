package com.learning.spring_security_learning.Controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class NoticesController {

  @GetMapping(path = "/notices")
  public String home(){
    return "notices saved successfully";
  }

}
