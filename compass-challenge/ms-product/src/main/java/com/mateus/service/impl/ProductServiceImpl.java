package com.mateus.service.impl;

import com.mateus.domain.Product;
import com.mateus.domain.dto.ProductDTO;
import com.mateus.domain.dto.ProductFormPostDTO;
import com.mateus.domain.dto.ProductFormPutDTO;
import com.mateus.exception.Conflict;
import com.mateus.exception.ObjectNotFound;
import com.mateus.repository.ProductRepository;
import com.mateus.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private static final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ProductRepository productRepository;

    private final ModelMapper mapper;

    @Override
    public URI save(ProductFormPostDTO productFormPostDTO) {
        Product product = mapper.map(productFormPostDTO, Product.class);
        if (!productRepository.existsByName(productFormPostDTO.getName())){
            product.setActive(true);
            productRepository.save(product);
            log.info("Product created successfully");
            return ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").build(product.getId());
        }else {
            log.error("Error when trying to save existing product");
            throw new Conflict("Product already exists");
        }
    }

    @Override
    public Page<ProductDTO> findAll(Pageable page) {
        Page<Product> products = productRepository.findAll(page);
        return products.map(e -> mapper.map(e, ProductDTO.class));
    }

    @Override
    public ProductDTO findByNameAndActiveTrue(String name) {
        return mapper.map(productRepository.findByNameAndActiveTrue(name).orElseThrow(() ->  {
            log.error("Error when trying to update product");
            throw new ObjectNotFound("Product Not Found!");
        }), ProductDTO.class) ;
    }

    @Override
    public ProductDTO update(Long id, ProductFormPutDTO productFormPutDTO) {
        checkExistenceOfProduct(id);
        Product product = mapper.map(productFormPutDTO, Product.class);
        product.setId(id);
        productRepository.save(product);
        log.info("Product updated successfully");
        return mapper.map(product, ProductDTO.class);
    }

    public Product checkExistenceOfProduct(Long id){
        return productRepository.findById(id).orElseThrow(() ->  {
            log.error("Error when trying to update product");
            throw new ObjectNotFound("Product Not Found!");
        });
    }
}
