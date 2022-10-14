package com.mateus.controller;

import com.mateus.domain.dto.ProductDTO;
import com.mateus.domain.dto.ProductFormPostDTO;
import com.mateus.domain.dto.ProductFormPutDTO;
import com.mateus.service.impl.ProductServiceImpl;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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
    public ResponseEntity<ProductDTO> save(@Valid @RequestBody ProductFormPostDTO productFormPostDTO){
        return ResponseEntity.created(productService.save(productFormPostDTO)).build();
    }

    @GetMapping
    public ResponseEntity<Page<ProductDTO>> findAll(@PageableDefault(sort = "active", direction = Sort.Direction.DESC)
                                                        Pageable page){
        return ResponseEntity.ok(productService.findAll(page));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> update(@PathVariable Long id, @Valid @RequestBody ProductFormPutDTO productFormPutDTO){
        return ResponseEntity.ok(productService.update(id, productFormPutDTO));
    }
}
