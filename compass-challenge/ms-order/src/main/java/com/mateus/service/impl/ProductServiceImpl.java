package com.mateus.service.impl;

import com.mateus.domain.Product;
import com.mateus.domain.dto.ProductDTO;
import com.mateus.domain.dto.ProductFormPostDTO;
import com.mateus.domain.dto.ProductFormPutDTO;
import com.mateus.exception.ObjectNotFound;
import com.mateus.repository.ProductRepository;
import com.mateus.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final ModelMapper mapper;

    @Override
    public URI save(ProductFormPostDTO productFormPostDTO) {
        Product product = mapper.map(productFormPostDTO, Product.class);
        product.setActive(true);
        productRepository.save(product);
        return ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").build(product.getId());
    }

    @Override
    public Page<ProductDTO> findAll(Pageable page) {
        Page<Product> products = productRepository.findAll(page);
        return products.map(e -> mapper.map(e, ProductDTO.class));
    }

    @Override
    public ProductDTO update(Long id, ProductFormPutDTO productFormPutDTO) {
        productRepository.findById(id).orElseThrow(() -> new ObjectNotFound("Product Not Found!"));
        Product product = mapper.map(productFormPutDTO, Product.class);
        product.setId(id);
        productRepository.save(product);
        return mapper.map(product, ProductDTO.class);
    }

}
