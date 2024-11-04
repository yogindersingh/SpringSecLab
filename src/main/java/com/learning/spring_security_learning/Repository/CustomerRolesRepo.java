package com.learning.spring_security_learning.Repository;

import com.learning.spring_security_learning.Entities.CustomerRoles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


@Repository
public interface CustomerRolesRepo extends JpaRepository<CustomerRoles, UUID> {

  List<CustomerRoles> findAllByCustomerId(UUID customerId);
}
