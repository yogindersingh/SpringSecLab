package com.learning.spring_security_learning.Config;


import com.learning.spring_security_learning.CustomCsrfFilter;
import com.learning.spring_security_learning.ExceptionHandlers.CustomAccessDeniedException;
import com.learning.spring_security_learning.ExceptionHandlers.CustomAuthenticationEntryPoint;
import com.learning.spring_security_learning.Handlers.AuthenticationFailureCustomHandler;
import com.learning.spring_security_learning.Handlers.AuthenticationSuccessCustomHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.csrf.CsrfTokenRequestHandler;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Collections;
import java.util.List;

@Configuration
public class ProjectSecurityConfiguration {

  @Autowired
  CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

  @Autowired
  CustomAccessDeniedException customAccessDeniedException;

  @Autowired
  AuthenticationSuccessCustomHandler authenticationSuccessCustomHandler;

  @Autowired
  AuthenticationFailureCustomHandler authenticationFailureCustomHandler;

  @Bean
  SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
    CsrfTokenRequestAttributeHandler csrfTokenRequestAttributeHandler = new CsrfTokenRequestAttributeHandler();
    //Default strategy By spring security is changed session itself for session fixation attacks
    http.sessionManagement(
            httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer.sessionCreationPolicy(
                SessionCreationPolicy.ALWAYS).sessionFixation(
                SessionManagementConfigurer.SessionFixationConfigurer::changeSessionId).invalidSessionUrl(
                "/invalidSession").maximumSessions(1).maxSessionsPreventsLogin(true))
        .securityContext(securityContextConfigurer -> securityContextConfigurer.requireExplicitSave(false))
        //required Channel configuration to support only https traffic
//    .requiresChannel(channel -> channel.anyRequest().requiresSecure())
//        .csrf(AbstractHttpConfigurer::disable)
        .csrf(csrf->{
          csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).csrfTokenRequestHandler(csrfTokenRequestAttributeHandler);
        }).addFilterAfter(new CustomCsrfFilter(), BasicAuthenticationFilter.class)
        .authorizeHttpRequests((requests) -> {
          requests.requestMatchers("/myAccount", "myBalance", "myCards").authenticated()
//              .requestMatchers("myLoans").hasAuthority("VIEW_LOANS")
              .requestMatchers("myLoans").hasRole("ADMIN")
              .requestMatchers("/contact", "/notices", "/error", "/user", "/invalidSession").permitAll().
              requestMatchers("*/*").denyAll();
        });
    //If we want to disable UI form login or Basic credentials login
//    http.formLogin(AbstractHttpConfigurer::disable);
//    http.httpBasic(AbstractHttpConfigurer::disable);
    http.formLogin(httpFormLoginConfigurer -> httpFormLoginConfigurer.successHandler(authenticationSuccessCustomHandler)
        .failureHandler(authenticationFailureCustomHandler));
        //logout Configuration for HTML form login
       // .logout(httpLogoutConfigurer -> httpLogoutConfigurer.logoutSuccessUrl("/logout?logout=true")
    // .invalidateHttpSession(true).clearAuthentication(true).deleteCookies("JSESSIONID));
    //http.httpBasic(Customizer.withDefaults());
    http.httpBasic(httpSecurityHttpBasicConfigurer -> httpSecurityHttpBasicConfigurer.authenticationEntryPoint(
        customAuthenticationEntryPoint));
    http.exceptionHandling(
        httpSecurityExceptionHandlingConfigurer -> httpSecurityExceptionHandlingConfigurer.accessDeniedHandler(
            customAccessDeniedException));

    //CORS configure to allow from specific origin
    http.cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(
        request -> {
          CorsConfiguration config = new CorsConfiguration();
          config.setAllowCredentials(true);
          config.setAllowedOrigins(List.of("http://localhost:4200"));
          //Allowed headers method and origin to accept
          config.setAllowedHeaders(Collections.singletonList(""));
          config.setAllowedMethods(Collections.singletonList("*"));
          //max age for which the browser will remember these configurations
          // Browser will receive allow origin header in response during the preflight call to call the actual API.
          config.setMaxAge(3600L);
          return config;
        }));

    //Global attach the authenticationEntryPoint
//    http.exceptionHandling(httpSecurityExceptionHandlingConfigurer -> httpSecurityExceptionHandlingConfigurer.authenticationEntryPoint(customAuthenticationEntryPoint));
    return http.build();
  }

//This is to define in memory user details manager
/*  @Bean
  UserDetailsService userDetailsService() {
    //here if we don't provide {noop} without defining any password encodespring security will throw password for
    // password encoder
//    UserDetails admin = User.withUsername("admin").password("{noop}admin").roles("Admin").build();
    UserDetails user= User.withUsername("user").password("{noop}user").roles("read").build();
    UserDetails admin = User.withUsername("admin").password("{bcrypt}$2a$12$" +
        ".v4IneOFgXYYzeEoMRorvOElgITLGTKk5AbmcvVGFngW26FMIctvi").roles("admin").build();
    return new InMemoryUserDetailsManager(admin,user);
  }*/

  //This is to define jdbc user details manager
//  @Bean
//  UserDetailsService userDetailsService(DataSource dataSource) {
//    return new JdbcUserDetailsManager(dataSource);
//  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }


  @Bean
  CompromisedPasswordChecker compromisedPasswordChecker() {
    return new HaveIBeenPwnedRestApiPasswordChecker();
  }


}
