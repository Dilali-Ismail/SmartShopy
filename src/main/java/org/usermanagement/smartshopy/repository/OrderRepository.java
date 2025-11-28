package org.usermanagement.smartshopy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.usermanagement.smartshopy.entity.Order;
import org.usermanagement.smartshopy.enums.OrderStatus;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByCustomerId(Long customerId);
    List<Order> findByStatus(OrderStatus status);

}
