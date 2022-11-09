package com.mateus.builder;

import com.mateus.domain.Product;
import com.mateus.domain.dto.ProductDTO;
import com.mateus.domain.dto.ProductFormPostDTO;
import com.mateus.domain.dto.ProductFormPutDTO;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductBuilder {

    private static final Long id = 1L;

    private static final String name = "Garrafa Inox";

    private static final String description = "Garrafa térmica inox";

    private static final BigDecimal price = BigDecimal.valueOf(100);

    private static final Boolean active = true;

    public static Product getProduct(){
        return Product.builder()
                .id(id)
                .name(name)
                .description(description)
                .price(price)
                .active(active)
                .build();
    }

    public static ProductDTO getProductDTO(){
        return ProductDTO.builder()
                .id(id)
                .name(name)
                .description(description)
                .price(price)
                .active(active)
                .build();
    }

    public static ProductFormPostDTO getProductFormPostDTO(){
        return ProductFormPostDTO.builder()
                .name(name)
                .description(description)
                .price(price)
                .build();
    }

    public static ProductFormPutDTO getProductFormPutDTO(){
        return ProductFormPutDTO.builder()
                .name(name)
                .description(description)
                .price(price)
                .active(active)
                .build();
    }

    public static Page<Product> getProductPage(){
        return new PageImpl<>(List.of(getProduct()));
    }

    public static Page<ProductDTO> getProductDTOPage(){
        return new PageImpl<>(List.of(getProductDTO()));
    }
}
