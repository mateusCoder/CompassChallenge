package com.mateus.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mateus.builder.OrderBuilder;
import com.mateus.builder.PaymentBuilder;
import com.mateus.domain.Payment;
import com.mateus.domain.dto.PaymentDTO;
import com.mateus.exception.BusinessException;
import com.mateus.exception.ObjectNotFound;
import com.mateus.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@AutoConfigureTestDatabase
class PaymentServiceImplTest {

    @InjectMocks
    PaymentServiceImpl paymentService;

    @Mock
    PaymentRepository paymentRepository;

    @Mock
    RabbitTemplate rabbitTemplate;

    @Spy
    ModelMapper mapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findOneByIdAndCpf__WhenSendPaymentIdAndCpfValid_ExpectedPaymentDto() {
        when(paymentRepository.findByIdAndCpf(anyLong(), anyString())).
                thenReturn(Optional.of(PaymentBuilder.getPayment()));

        PaymentDTO response = paymentService.findOneByIdAndCpf(PaymentBuilder.getPayment().getId(),
                PaymentBuilder.getPayment().getCpf());

        assertNotNull(response);
        assertEquals(PaymentDTO.class, response.getClass());
        assertEquals(PaymentBuilder.getPaymentDTO().getId(), response.getId());
    }

    @Test
    void findOneByIdAndCpf__WhenSendPaymentIdAndCpfInvalid_ExpectedObjectNotFoundException() {
        ObjectNotFound response = assertThrows(ObjectNotFound.class, () ->
                paymentService.findOneByIdAndCpf(PaymentBuilder.getPaymentDTO().getId(),
                        PaymentBuilder.getPayment().getCpf()));

        assertEquals("Payment Not Found!", response.getMessage());
    }

    @Test
    public void processPayment_WhenListenerOrderWithPaymentConfirmedValid_ExpectedUpdateOrderStatusById()
            throws JsonProcessingException, BusinessException {
        when(paymentRepository.save(any())).thenReturn(PaymentBuilder.getPayment());
        doNothing().when(rabbitTemplate).convertAndSend(any());

        ObjectMapper objectMapper = new ObjectMapper();
        String order = objectMapper.writeValueAsString(OrderBuilder.getOrderDataProcessingDtoRequestPaymentConfirmed());
        paymentService.processPayment(order);

        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    public void processPayment_WhenListenerOrderWithUnauthorizedPaymentValid_ExpectedUpdateOrderStatusById()
            throws JsonProcessingException, BusinessException {
        when(paymentRepository.save(any())).thenReturn(PaymentBuilder.getPayment());
        doNothing().when(rabbitTemplate).convertAndSend(any());

        ObjectMapper objectMapper = new ObjectMapper();
        String order = objectMapper.writeValueAsString(
                OrderBuilder.getOrderDataProcessingDtoRequestUnauthorizedPayment());
        paymentService.processPayment(order);

        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

}