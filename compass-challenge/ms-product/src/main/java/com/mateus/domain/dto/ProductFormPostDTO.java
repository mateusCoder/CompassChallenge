package com.mateus.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProductFormPostDTO {

    @NotBlank
    @Schema(type = "string",
            example = "Smartphone K",
            description = "Name of the product to be created",
            required = true)
    private String name;

    @Schema(type = "string",
            example = "Black slim smartphone",
            description = "Description of the product to be created")
    private String description;

    @NotNull
    @Schema(type = "BigDecimal",
            example = "500.00",
            description = "Price of the product to be created",
            required = true)
    private BigDecimal price;
}
