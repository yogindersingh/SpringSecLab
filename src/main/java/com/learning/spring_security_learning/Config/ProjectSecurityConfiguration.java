package com.learning.spring_security_learning.Config;


import com.learning.spring_security_learning.ExceptionHandlers.CustomAccessDeniedException;
import com.learning.spring_security_learning.ExceptionHandlers.CustomAuthenticationEntryPoint;
import com.learning.spring_security_learning.JwtCustomConverter.KeycloakRoleConverter;
import com.learning.spring_security_learning.filters.CustomCsrfFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

@Configuration
public class ProjectSecurityConfiguration {

  @Autowired
  CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

  @Autowired
  CustomAccessDeniedException customAccessDeniedException;


  @Bean
  SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
    JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
    jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new KeycloakRoleConverter());
    CsrfTokenRequestAttributeHandler csrfTokenRequestAttributeHandler = new CsrfTokenRequestAttributeHandler();
    //Default strategy By spring security is changed session itself for session fixation attacks
    http.sessionManagement(
            httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .invalidSessionUrl("/invalidSession"))

        .csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
            .csrfTokenRequestHandler(csrfTokenRequestAttributeHandler).ignoringRequestMatchers("/apiLogin")).addFilterAfter(new CustomCsrfFilter(),
            BasicAuthenticationFilter.class)

        .authorizeHttpRequests((requests) -> {
          requests.requestMatchers("/myAccount", "/myBalance", "/myCards", "/user").authenticated()
//              .requestMatchers("myLoans").hasAuthority("VIEW_LOANS")
              .requestMatchers("/myLoans").hasRole("ADMIN")
              .requestMatchers("/contact", "/notices", "/error", "/invalidSession").permitAll().
              requestMatchers("*/*").denyAll();
        });

    http.oauth2ResourceServer(rsc->rsc.jwt(jc->jc.jwtAuthenticationConverter(jwtAuthenticationConverter)));
    return http.build();
  }


}
