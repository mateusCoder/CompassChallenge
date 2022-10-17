package com.mateus.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mateus.domain.dto.PaymentDTO;

public interface PaymentService {
    PaymentDTO findOneByIdAndCpf(Long id, String cpf);

    void processPayment(String order) throws JsonProcessingException;
}
