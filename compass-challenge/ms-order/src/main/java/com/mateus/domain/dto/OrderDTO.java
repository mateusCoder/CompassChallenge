package com.mateus.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mateus.domain.constant.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OrderDTO {

    private Long id;

    private String cpf;

    private String orderNumber;

    private List<String> products;

    private BigDecimal totalOrderPrice;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate localDate;

    private Status status;
}
