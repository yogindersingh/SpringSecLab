package com.learning.spring_security_learning.Config;


import com.learning.spring_security_learning.ExceptionHandlers.CustomAccessDeniedException;
import com.learning.spring_security_learning.ExceptionHandlers.CustomAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;

@Configuration
public class ProjectSecurityConfiguration {

  @Autowired
  CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

  @Autowired
  CustomAccessDeniedException customAccessDeniedException;

  @Bean
  SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
    http.sessionManagement(
            httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer.sessionFixation(
                SessionManagementConfigurer.SessionFixationConfigurer::newSession).invalidSessionUrl(
                "/invalidSession").maximumSessions(1).maxSessionsPreventsLogin(true))
        //required Channel configuration to support only https traffic
//    .requiresChannel(channel -> channel.anyRequest().requiresSecure())
        .csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests((requests) -> {
          requests.requestMatchers("/myAccount", "myBalance", "myCards",
                  "myLoans").authenticated().
              requestMatchers("/contact", "/notices", "/error", "/user", "/invalidSession").permitAll().
              requestMatchers("*/*").denyAll();
        });
    //If we want to disable UI form login or Basic credentials login
//    http.formLogin(AbstractHttpConfigurer::disable);
//    http.httpBasic(AbstractHttpConfigurer::disable);
    http.formLogin(Customizer.withDefaults());
    //http.httpBasic(Customizer.withDefaults());
    http.httpBasic(httpSecurityHttpBasicConfigurer -> httpSecurityHttpBasicConfigurer.authenticationEntryPoint(
        customAuthenticationEntryPoint));
    http.exceptionHandling(
        httpSecurityExceptionHandlingConfigurer -> httpSecurityExceptionHandlingConfigurer.accessDeniedHandler(
            customAccessDeniedException));

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
