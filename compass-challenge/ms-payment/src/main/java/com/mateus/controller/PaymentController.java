package com.mateus.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mateus.config.annotations.payment.GetPaymentByIdAndCpfDocConfig;
import com.mateus.domain.dto.PaymentDTO;
import com.mateus.exception.BusinessException;
import com.mateus.service.impl.PaymentServiceImpl;
import com.mateus.util.QueueUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentServiceImpl paymentService;

    private final RabbitTemplate rabbitTemplate;

    @GetMapping
    @GetPaymentByIdAndCpfDocConfig
    public ResponseEntity<Page<PaymentDTO>> findByIdOrCpf(@RequestParam(value = "id", required = false) Long id,
                                                          @RequestParam(value = "cpf", required = false) String cpf,
                                                          @PageableDefault(
                                                                  sort = "id",
                                                                  size = 50)  Pageable pageable){
        return ResponseEntity.ok(paymentService.findByIdOrCpf(id, cpf, pageable));
    }

    @RabbitListener(queues = QueueUtils.ORDER_NOTIFICATION)
    public void processPayment(String order) throws BusinessException, JsonProcessingException {
        paymentService.processPayment(order);
    }

}
