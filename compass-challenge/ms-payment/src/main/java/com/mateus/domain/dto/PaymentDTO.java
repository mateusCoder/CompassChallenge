package com.mateus.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mateus.domain.constant.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PaymentDTO {

    private Long id;

    private Long orderNumber;

    private String cpf;

    private Status status;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate date;
}
