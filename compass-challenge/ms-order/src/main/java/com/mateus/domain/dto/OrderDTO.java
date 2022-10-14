package com.mateus.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mateus.domain.Product;
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

    private String cpf;

    private Long orderNumber;

    private List<Product> products;

    private BigDecimal totalOrderPrice;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate localDate;

    private Status status;
}
