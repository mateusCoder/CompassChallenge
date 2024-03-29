package com.mateus.domain.dto;

import com.mateus.domain.constant.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OrderDataProcessingDTO implements Serializable {

    private Long id;

    private String cpf;

    private String orderNumber;

    private BigDecimal totalOrderPrice;

    private Status status;
}
