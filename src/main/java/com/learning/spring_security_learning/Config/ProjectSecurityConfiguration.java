package com.learning.spring_security_learning.Config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class ProjectSecurityConfiguration {

  @Bean
  SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests((requests) -> {
      requests.requestMatchers("/myAccount", "myBalance", "myCards",
              "myLoans").authenticated().
          requestMatchers("/contact", "/notices","/error").permitAll().
          requestMatchers("*/*").denyAll();
    });
    //If we want to disable UI form login or Basic credentials login
//    http.formLogin(AbstractHttpConfigurer::disable);
//    http.httpBasic(AbstractHttpConfigurer::disable);
    http.formLogin(Customizer.withDefaults());
    http.httpBasic(Customizer.withDefaults());
    return http.build();
  }
}
