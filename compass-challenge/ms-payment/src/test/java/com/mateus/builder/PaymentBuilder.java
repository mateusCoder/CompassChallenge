package com.mateus.builder;

import com.mateus.domain.Payment;
import com.mateus.domain.constant.Status;
import com.mateus.domain.dto.PaymentDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.time.LocalDate;
import java.util.List;

public class PaymentBuilder {

    private static final Long id = 1L;

    private static final String orderNumber = "46a3eb49-72ae-4e51-afdf-686e395f2b17";

    private static final String cpf = "461.912.588-10";

    private static final Status status = Status.ORDER_CREATED;

    private static final LocalDate date = LocalDate.now();

    public static Payment getPayment(){
        return Payment.builder()
                .id(id)
                .orderNumber(orderNumber)
                .cpf(cpf)
                .status(status)
                .date(date)
                .build();
    }

    public static PaymentDTO getPaymentDTO(){
        return PaymentDTO.builder()
                .id(id)
                .orderNumber(orderNumber)
                .cpf(cpf)
                .status(status)
                .date(date)
                .build();
    }

    public static Page<Payment> getPaymentPageable(){
        return new PageImpl<>(List.of(getPayment()));
    }

    public static Page<PaymentDTO> getPaymentDTOPageable(){
        return new PageImpl<>(List.of(getPaymentDTO()));
    }
}
