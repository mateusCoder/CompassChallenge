package com.mateus.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mateus.domain.Payment;
import com.mateus.domain.constant.Status;
import com.mateus.domain.dto.OrderDataProcessingDTO;
import com.mateus.domain.dto.PaymentDTO;
import com.mateus.exception.BusinessException;
import com.mateus.repository.PaymentRepositorySpec;
import com.mateus.service.PaymentService;
import com.mateus.specification.PaymentSpec;
import com.mateus.util.QueueUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private static final Logger log = LoggerFactory.getLogger(PaymentServiceImpl.class);

    private final PaymentRepositorySpec paymentRepositorySpec;

    private final RabbitTemplate rabbitTemplate;

    private final ModelMapper mapper;

    @Override
    public Page<PaymentDTO> findByIdOrCpf(Long id, String cpf, Pageable page) {
        Specification<Payment> specification = PaymentSpec.getSpecification(id, cpf);
        Page<Payment> payments = paymentRepositorySpec.findAll(specification, page);

        return payments.map(e -> mapper.map(e, PaymentDTO.class));
    }

    @Override
    public void processPayment(String order) throws BusinessException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        OrderDataProcessingDTO orderProcessing = objectMapper.readValue(order, OrderDataProcessingDTO.class);
        if(orderProcessing.getTotalOrderPrice().compareTo(BigDecimal.valueOf(1000)) >=0){
            orderProcessing.setStatus(Status.UNAUTHORIZED_PAYMENT);
        }else{
            orderProcessing.setStatus(Status.PAYMENT_CONFIRMED);
        }
        savePayment(orderProcessing);

        String response = convertIntoJson(orderProcessing);
        rabbitTemplate.convertAndSend(QueueUtils.PAYMENT_NOTIFICATION, response);
    }


    public void savePayment(OrderDataProcessingDTO orderDataProcessingDTO){
        Payment payment = mapper.map(orderDataProcessingDTO, Payment.class);
        payment.setId(null);
        paymentRepositorySpec.save(payment);
    }

    protected String convertIntoJson(OrderDataProcessingDTO order) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(order);
    }
}
