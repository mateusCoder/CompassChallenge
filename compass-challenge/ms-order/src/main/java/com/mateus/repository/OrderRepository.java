package com.mateus.repository;

import com.mateus.domain.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Object> findByOrderNumber(Long orderNumber);

    Page<Object> findByCpf(String cpf, Pageable pageable);
}
