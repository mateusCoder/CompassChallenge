package com.mateus.repository;

import com.mateus.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByNameAndActiveTrue(String productsName);

    boolean existsByName(String name);
}
