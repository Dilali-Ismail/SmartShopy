package org.usermanagement.smartshopy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.usermanagement.smartshopy.entity.Payment;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment,Long> {

    List<Payment> findByOrderId(Long id);
}
