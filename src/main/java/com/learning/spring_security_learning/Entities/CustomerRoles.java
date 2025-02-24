package com.learning.spring_security_learning.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "customerRoles")
public class CustomerRoles {

  @Id
  @UuidGenerator
  @Column(name = "roleId")
  private UUID id;

  @Column(name = "roleName")
  private String roleName;

  @ManyToOne
  @JoinColumn(name = "customerId", insertable = false, updatable = false)
  private Customer customer;

}