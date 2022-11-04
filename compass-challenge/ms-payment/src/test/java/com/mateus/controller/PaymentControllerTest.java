package com.mateus.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mateus.builder.PaymentBuilder;
import com.mateus.domain.Payment;
import com.mateus.exception.ObjectNotFound;
import com.mateus.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc()
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class PaymentControllerTest {

    private final MockMvc mockMvc;

    private final PaymentRepository paymentRepository;


    private final ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        Payment payment = paymentRepository.save(PaymentBuilder.getPayment());
    }

    @Test
    void findOneByIdAndCpf__WhenSendPaymentIdAndCpfValid_ExpectedResponseEntityPaymentDto() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/payments/{id}/customer/{cpf}", 1L, "461.912.588-10"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(Assertions::assertNotNull)
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void findByOrderNumber_WhenSendPaymentIdAndCpfNonExistent_ExpectedObjectNotFoundException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/payments/{id}/customer/{cpf}", 2L, "461.912.588-10"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof ObjectNotFound))
                .andDo(MockMvcResultHandlers.print());
    }

}