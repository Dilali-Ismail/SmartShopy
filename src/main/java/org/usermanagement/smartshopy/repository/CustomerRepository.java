package org.usermanagement.smartshopy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.usermanagement.smartshopy.entity.Customer;
import org.usermanagement.smartshopy.enums.CustomerTier;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer,Long> {
   Optional<Customer> findByEmail(String email);
   boolean existsByEmail(String email);
   List<Customer> findCustomerByTier (CustomerTier tier);
   Optional<Customer> findByUserId(Long UserId);

}
