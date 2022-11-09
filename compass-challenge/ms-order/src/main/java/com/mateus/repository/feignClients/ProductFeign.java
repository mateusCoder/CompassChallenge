package com.mateus.repository.feignClients;

import com.mateus.domain.dto.ProductDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-product", url = "http://localhost:8070/v1/products")
public interface ProductFeign {

    @GetMapping("/{name}")
    public ResponseEntity<ProductDTO> findByNameAndActiveTrue(@PathVariable String name);
}
