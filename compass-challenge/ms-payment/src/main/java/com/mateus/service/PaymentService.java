package com.mateus.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mateus.domain.dto.OrderDataProcessingDTO;
import com.mateus.domain.dto.PaymentDTO;

public interface PaymentService {
    PaymentDTO findOneByIdAndCpf(Long id, String cpf);

    OrderDataProcessingDTO processPayment(String order) throws JsonProcessingException;
}
