package com.mateus.builder;

import com.mateus.domain.dto.ProductDTO;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

public class ProductBuilder {

    private static final Long id = 1L;

    private static final String name = "Garrafa Inox";

    private static final String description = "Garrafa térmica inox";

    private static final BigDecimal price = BigDecimal.valueOf(100);

    private static final Boolean active = true;

    public static ResponseEntity<ProductDTO> getProductDTO(){
        return ResponseEntity.ok(ProductDTO.builder()
                .name(name)
                .description(description)
                .price(price)
                .active(active)
                .build());
    }
}
