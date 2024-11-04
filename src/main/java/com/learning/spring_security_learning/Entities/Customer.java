package com.learning.spring_security_learning.Entities;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "customer")
public class Customer {

  @Id
  @UuidGenerator
  private UUID id;

  @Column(name = "FirstName")
  private String firstName;

  @Column(name = "LastName")
  private String lastName;

  @Column(name = "Email")
  private String email;

  @Column
  private String password;

  @OneToMany(mappedBy = "customerId")
  private List<CustomerRoles> roles;

}
