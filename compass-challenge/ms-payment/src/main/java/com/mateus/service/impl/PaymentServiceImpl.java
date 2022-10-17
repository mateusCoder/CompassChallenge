package com.mateus.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mateus.domain.Payment;
import com.mateus.domain.constant.Status;
import com.mateus.domain.dto.OrderDataProcessingDTO;
import com.mateus.domain.dto.PaymentDTO;
import com.mateus.repository.PaymentRepository;
import com.mateus.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    private final ModelMapper mapper;

    @Override
    public PaymentDTO findOneByIdAndCpf(Long id, String cpf) {
        Payment payment = paymentRepository.findByIdAndCpf(id, cpf).orElseThrow(() -> new RuntimeException());
        return mapper.map(payment, PaymentDTO.class);
    }

    @Override
    public void processPayment(String order) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        OrderDataProcessingDTO orderProcessing = objectMapper.readValue(order, OrderDataProcessingDTO.class);
        if(orderProcessing.getTotalOrderPrice().compareTo(BigDecimal.valueOf(1000)) >=0){
            orderProcessing.setStatus(Status.UNAUTHORIZED_PAYMENT);
        }else{
            orderProcessing.setStatus(Status.PAYMENT_CONFIRMED);
        }
        System.out.println(orderProcessing);
        savePayment(orderProcessing);

    }

    public void savePayment(OrderDataProcessingDTO orderDataProcessingDTO){
        Payment payment = mapper.map(orderDataProcessingDTO, Payment.class);
        payment.setId(null);
        payment.setDate(LocalDate.now());
        paymentRepository.save(payment);
    }
}
