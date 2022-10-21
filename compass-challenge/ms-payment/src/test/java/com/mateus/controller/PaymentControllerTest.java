package com.mateus.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mateus.builder.OrderBuilder;
import com.mateus.builder.PaymentBuilder;
import com.mateus.domain.dto.PaymentDTO;
import com.mateus.exception.BusinessException;
import com.mateus.service.impl.PaymentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@AutoConfigureTestDatabase
class PaymentControllerTest {

    @InjectMocks
    PaymentController paymentController;

    @Mock
    PaymentServiceImpl paymentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findOneByIdAndCpf() {
        when(paymentService.findOneByIdAndCpf(anyLong(), anyString())).thenReturn(PaymentBuilder.getPaymentDTO());

        ResponseEntity<PaymentDTO> response = paymentController.findOneByIdAndCpf(PaymentBuilder.getPayment().getId(),
                PaymentBuilder.getPayment().getCpf());

        assertNotNull(response);
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void processPayment() throws BusinessException, JsonProcessingException {
        doNothing().when(paymentService).processPayment(anyString());
        ObjectMapper objectMapper = new ObjectMapper();
        String order = objectMapper.writeValueAsString(OrderBuilder.getOrderDataProcessingDtoRequest());

        paymentController.processPayment(order);

        verify(paymentService, times(1)).processPayment(order);
    }
}