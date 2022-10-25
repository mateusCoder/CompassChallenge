package com.mateus.builder;

import com.mateus.domain.constant.Status;
import com.mateus.domain.dto.OrderDataProcessingDTO;

import java.math.BigDecimal;

public class OrderBuilder {

    private static final Long id = 1L;

    private static final String cpf = "461.912.588-10";

    private static final String orderNumber = "46a3eb49-72ae-4e51-afdf-686e395f2b17";

    public static OrderDataProcessingDTO getOrderDataProcessingDtoRequestPaymentConfirmed(){
        return OrderDataProcessingDTO.builder()
                .id(id)
                .cpf(cpf)
                .orderNumber(orderNumber)
                .totalOrderPrice(BigDecimal.valueOf(900))
                .status(Status.ORDER_CREATED)
                .build();
    }

    public static OrderDataProcessingDTO getOrderDataProcessingDtoRequestUnauthorizedPayment(){
        return OrderDataProcessingDTO.builder()
                .id(id)
                .cpf(cpf)
                .orderNumber(orderNumber)
                .totalOrderPrice(BigDecimal.valueOf(1001))
                .status(Status.ORDER_CREATED)
                .build();
    }

}
