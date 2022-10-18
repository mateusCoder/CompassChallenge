package com.mateus.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mateus.domain.dto.OrderDTO;
import com.mateus.domain.dto.OrderFormDTO;
import com.mateus.service.impl.OrderServiceImpl;
import com.mateus.util.QueueUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
    public ResponseEntity<OrderDTO> findById(@PathVariable Long id){
        return ResponseEntity.ok(orderService.findById(id));
    }

    @GetMapping("/orderNumber/{orderNumber}")
    public ResponseEntity<OrderDTO> findByOrderNumber(@PathVariable Long orderNumber){
        return ResponseEntity.ok(orderService.findByOrderNumber(orderNumber));
    }

    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<Page<OrderDTO>> findByCpf(@PathVariable String cpf, @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable){
        return ResponseEntity.ok(orderService.findByCpf(cpf, pageable));
    }

    @RabbitListener(queues = QueueUtils.PAYMENT_NOTIFICATION)
    public void updateOrder(String order) throws JsonProcessingException {
        orderService.updateOrder(order);
    }
}
