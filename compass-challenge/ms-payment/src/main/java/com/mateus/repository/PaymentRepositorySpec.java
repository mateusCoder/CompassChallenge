package com.mateus.repository;

import com.mateus.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepositorySpec
        extends JpaRepository<Payment, Long>, JpaSpecificationExecutor<Payment> {
}
