package com.mateus.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OrderProductsFormDTO {

    @NotBlank
    @Schema(type = "String",
            description = "Product name to be added to order",
            required = true)
    private String productsName;

    @NotNull
    @Schema(type = "Integer",
            description = "Quantity of the product to be added to the order",
            required = true)
    private Integer amount;
}
