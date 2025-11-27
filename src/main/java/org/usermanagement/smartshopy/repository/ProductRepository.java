package org.usermanagement.smartshopy.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.usermanagement.smartshopy.entity.Product;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByActiveTrue();
}

