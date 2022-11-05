package com.mateus.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OrderFormDTO {

    @CPF
    @Pattern(regexp = "[0-9]{3}\\.[0-9]{3}\\.[0-9]{3}\\-[0-9]{2}")
    @Schema(type = "string",
            example = "377.483.366-44",
            description = "CPF of the customer responsible for the purchase of the order",
            required = true)
    private String cpf;

    @NotNull
    @Schema(type = "array",
            example = "[\n" +
                    "        {\n" +
                    "            \"productsName\":\"Smartphone K\",\n" +
                    "            \"amount\":1\n" +
                    "        }]",
            description = "List with product names and their respective order quantities",
            required = true)
    private List<OrderProductsFormDTO> orderProducts;

}
