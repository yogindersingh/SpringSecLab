package com.learning.spring_security_learning.Controllers;

import com.learning.spring_security_learning.service.BankService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class BalanceController {

  @Autowired
  BankService bankService;

  @GetMapping(path = "/myBalance")
  public String home() {
    return bankService.getResponseString();
  }

}
