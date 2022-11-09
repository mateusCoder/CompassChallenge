package com.mateus.controller;

import com.mateus.config.annotations.product.GetAllProductDocConfig;
import com.mateus.config.annotations.product.SaveProductDocConfig;
import com.mateus.config.annotations.product.UpdateProductDocConfig;
import com.mateus.domain.dto.ProductDTO;
import com.mateus.domain.dto.ProductFormPostDTO;
import com.mateus.domain.dto.ProductFormPutDTO;
import com.mateus.service.impl.ProductServiceImpl;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductServiceImpl productService;

    private final ModelMapper mapper;

    @PostMapping
    @SaveProductDocConfig
    public ResponseEntity<ProductDTO> save(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "A request to create a new product") @Valid @RequestBody ProductFormPostDTO productFormPostDTO){
        return ResponseEntity.created(productService.save(productFormPostDTO)).build();
    }

    @GetMapping
    @GetAllProductDocConfig
    public ResponseEntity<Page<ProductDTO>> findAll(@ParameterObject @PageableDefault(sort = "active",
            direction = Sort.Direction.DESC) Pageable page){
        return ResponseEntity.ok(productService.findAll(page));
    }

    @GetMapping("/{name}")
    public ResponseEntity<ProductDTO> findByNameAndActiveTrue(@Parameter(description = "Name of product active to be searched")
                                                      @PathVariable String name){
        return ResponseEntity.ok(productService.findByNameAndActiveTrue(name));
    }

    @PutMapping("/{id}")
    @UpdateProductDocConfig
    public ResponseEntity<ProductDTO> update(@Parameter(description = "id of order to be searched") @PathVariable Long id,
                                             @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                                     description = "A request to update an existing product")
                                             @Valid @RequestBody ProductFormPutDTO productFormPutDTO){
        return ResponseEntity.ok(productService.update(id, productFormPutDTO));
    }
}
