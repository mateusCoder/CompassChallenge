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
public class ProductFormPutDTO {

    @NotBlank
    @Schema(type = "string",
            example = "New Smartphone",
            description = "Name of the product to be updated",
            required = true)
    private String name;

    @Schema(type = "string",
            example = "Smartphone Yellow 2",
            description = "Description of the product to be updated")
    private String description;

    @NotNull
    @Schema(type = "BidDecimal",
            example = "3000",
            description = "Price of the product to be updated",
            required = true)
    private BigDecimal price;

    @NotNull
    @Schema(type = "Boolean",
            example = "true",
            description = "Update if the product is active",
            required = true)
    private Boolean active;
}
