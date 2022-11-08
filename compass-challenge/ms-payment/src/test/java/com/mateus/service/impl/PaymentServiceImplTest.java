package com.mateus.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mateus.builder.OrderBuilder;
import com.mateus.builder.PaymentBuilder;
import com.mateus.domain.Payment;
import com.mateus.domain.dto.PaymentDTO;
import com.mateus.exception.BusinessException;
import com.mateus.exception.ObjectNotFound;
import com.mateus.repository.PaymentRepositorySpec;
import com.mateus.specification.PaymentSpec;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collections;
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
    PaymentRepositorySpec paymentRepository;

    @Mock
    RabbitTemplate rabbitTemplate;

    @Mock
    PaymentSpec paymentSpec;

    @Spy
    ModelMapper mapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findByIdOrCpf_WhenSendPaymentIdAndCpfValid_ExpectedPaymentDto() {
        when(paymentRepository.findAll((Specification<Payment>) any(), (Pageable) any()))
                .thenReturn(PaymentBuilder.getPaymentPageable());

        Pageable page = PageRequest.of(0, 100);
        Page<PaymentDTO> response = paymentService.findByIdOrCpf(1L, "461.912.588-10", page);

        assertNotNull(response);
        assertEquals(PageImpl.class, response.getClass());
        assertEquals(1, response.getNumberOfElements());
    }

    @Test
    void findByIdOrCpf_WhenNotSendParameters_ExpectedPagePaymentDto() {
        when(paymentRepository.findAll((Specification<Payment>) any(), (Pageable) any()))
                .thenReturn(PaymentBuilder.getPaymentPageable());

        Pageable page = PageRequest.of(0, 100);
        Page<PaymentDTO> response = paymentService.findByIdOrCpf(null, null, page);

        assertNotNull(response);
        assertEquals(PageImpl.class, response.getClass());
        assertEquals(1, response.getNumberOfElements());
    }

    @Test
    void findByIdOrCpf_WhenSendPaymentIdValid_ExpectedPagePaymentDto() {
        when(paymentRepository.findAll((Specification<Payment>) any(), (Pageable) any()))
                .thenReturn(PaymentBuilder.getPaymentPageable());

        Pageable page = PageRequest.of(0, 100);
        Page<PaymentDTO> response = paymentService.findByIdOrCpf(1L, null, page);

        assertNotNull(response);
        assertEquals(PageImpl.class, response.getClass());
        assertEquals(1, response.getNumberOfElements());
    }

    @Test
    void findByIdOrCpf_WhenSendPaymentCPFValid_ExpectedPagePaymentDto() {
        when(paymentRepository.findAll((Specification<Payment>) any(), (Pageable) any()))
                .thenReturn(PaymentBuilder.getPaymentPageable());

        Pageable page = PageRequest.of(0, 100);
        Page<PaymentDTO> response = paymentService.findByIdOrCpf(1L, "461.912.588-10", page);

        assertNotNull(response);
        assertEquals(PageImpl.class, response.getClass());
        assertEquals(1, response.getNumberOfElements());
    }

    @Test
    void findByIdOrCpf_WhenSendPaymentCPFNonexistent_ExpectedEmptyPage() {
        when(paymentRepository.findAll((Specification<Payment>) any(), (Pageable) any()))
                .thenReturn(new PageImpl<>(Collections.EMPTY_LIST));

        Pageable page = PageRequest.of(0, 100);
        Page<PaymentDTO> response = paymentService.findByIdOrCpf(null, "461.912.588-10", page);

        assertNotNull(response);
        assertEquals(PageImpl.class, response.getClass());
        assertEquals(0, response.getNumberOfElements());
    }

    @Test
    void findByIdOrCpf_WhenSendPaymentIdNonexistent_ExpectedEmptyPage() {
        when(paymentRepository.findAll((Specification<Payment>) any(), (Pageable) any()))
                .thenReturn(new PageImpl<>(Collections.EMPTY_LIST));

        Pageable page = PageRequest.of(0, 100);
        Page<PaymentDTO> response = paymentService.findByIdOrCpf(150L, null, page);

        assertNotNull(response);
        assertEquals(PageImpl.class, response.getClass());
        assertEquals(0, response.getNumberOfElements());
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