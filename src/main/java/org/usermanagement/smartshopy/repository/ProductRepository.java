package org.usermanagement.smartshopy.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.usermanagement.smartshopy.entity.Product;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByActiveTrue();
}

