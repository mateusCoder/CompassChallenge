package com.mateus.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mateus.domain.dto.PaymentDTO;
import com.mateus.service.impl.PaymentServiceImpl;
import com.mateus.util.QueueUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentServiceImpl paymentService;

    private final RabbitTemplate rabbitTemplate;

    @GetMapping("/{id}/client/{cpf}")
    public ResponseEntity<PaymentDTO> findOneByIdAndCpf(@PathVariable Long id, @PathVariable String cpf){
        return ResponseEntity.ok(paymentService.findOneByIdAndCpf(id, cpf));
    }

    @RabbitListener(queues = QueueUtils.ORDER_NOTIFICATION)
    public void processPayment(String order) throws JsonProcessingException {
        paymentService.processPayment(order);
    }

}
