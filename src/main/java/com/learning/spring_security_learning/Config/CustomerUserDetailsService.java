package com.learning.spring_security_learning.Config;

import com.learning.spring_security_learning.Entities.Customer;
import com.learning.spring_security_learning.Repository.CustomerRepo;
import com.learning.spring_security_learning.Repository.CustomerRolesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerUserDetailsService implements UserDetailsService {

  @Autowired
  private CustomerRepo customerRepo;

  @Autowired
  private CustomerRolesRepo customerRolesRepo;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Customer customer = customerRepo.findByEmail(username)
        .orElseThrow(() -> new UsernameNotFoundException("User not found with " +
            "username: " + username));
    List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
    customerRolesRepo.findAllByCustomerId(customer.getId()).forEach(role->grantedAuthorities.add(new SimpleGrantedAuthority(role.getRoleName())));
    return new User(customer.getEmail(),customer.getPassword(),grantedAuthorities);
  }

}
