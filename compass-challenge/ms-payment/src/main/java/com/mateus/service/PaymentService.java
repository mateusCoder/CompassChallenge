package com.mateus.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mateus.domain.dto.PaymentDTO;
import com.mateus.exception.BusinessException;

public interface PaymentService {
    PaymentDTO findOneByIdAndCpf(Long id, String cpf);

    void processPayment(String order) throws JsonProcessingException, BusinessException;
}
