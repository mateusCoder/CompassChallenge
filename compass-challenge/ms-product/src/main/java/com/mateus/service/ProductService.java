package com.mateus.service;

import com.mateus.domain.dto.ProductDTO;
import com.mateus.domain.dto.ProductFormPostDTO;
import com.mateus.domain.dto.ProductFormPutDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.net.URI;

public interface ProductService {
    URI save(ProductFormPostDTO productFormPostDTO);

    ProductDTO update(Long id, ProductFormPutDTO productFormPutDTO);

    Page<ProductDTO> findAll(Pageable page);
}
