package com.mateus.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mateus.domain.dto.PaymentDTO;
import com.mateus.exception.BusinessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PaymentService {

    Page<PaymentDTO> findByIdOrCpf(Long id, String cpf, Pageable pageable);

    void processPayment(String order) throws JsonProcessingException, BusinessException;

}
