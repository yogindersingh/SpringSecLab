package com.learning.spring_security_learning.Entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
  @Column(name = "id")
  private UUID id;

  @Column(name = "firstName")
  private String firstName;

  @Column(name = "lastName")
  private String lastName;

  @Column(name = "email")
  private String email;

  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  @Column(name = "password")
  private String password;

  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "customerId", nullable = false)
  private List<CustomerRoles> roles;

}
