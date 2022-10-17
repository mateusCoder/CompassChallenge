package com.mateus.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mateus.domain.dto.OrderDTO;
import com.mateus.domain.dto.OrderFormDTO;
import com.mateus.service.impl.OrderServiceImpl;
import com.mateus.util.QueueUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderServiceImpl orderService;

    @PostMapping
    public ResponseEntity<OrderDTO> save(@Valid @RequestBody OrderFormDTO orderFormDTO) throws JsonProcessingException {
        return ResponseEntity.created(orderService.save(orderFormDTO)).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> findOne(@PathVariable Long id){
        return ResponseEntity.ok(orderService.findById(id));
    }

    @RabbitListener(queues = QueueUtils.PAYMENT_NOTIFICATION)
    public void updateOrder(String order) throws JsonProcessingException {
        orderService.updateOrder(order);
    }
}
