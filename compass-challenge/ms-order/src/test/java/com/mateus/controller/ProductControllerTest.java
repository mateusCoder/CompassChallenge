package com.mateus.controller;

import com.mateus.builder.ProductBuilder;
import com.mateus.domain.dto.ProductDTO;
import com.mateus.domain.dto.ProductFormPostDTO;
import com.mateus.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@AutoConfigureTestDatabase
class ProductControllerTest {

    @InjectMocks
    ProductController productController;

    @Mock
    ProductServiceImpl productService;

    @Spy
    ModelMapper mapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void save_WhenSendProductFormPostDtoWithTotalElements_ExpectedResponseEntityProductDto() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path("/v1/products/{id}")
                .build(ProductBuilder.getProduct().getId());

        when(productService.save(any())).thenReturn(uri);

        ResponseEntity<ProductDTO> response = productController.save(ProductBuilder.getProductFormPostDTO());

        assertNotNull(response);
        assertEquals(uri.toString(), "http://localhost/v1/products/1");
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void findAll_WhenFindAll_ExpectedResponseEntityPageableProductDto() {
        when(productService.findAll((Pageable) any())).thenReturn(ProductBuilder.getProductDTOPage());

        Pageable page = PageRequest.of(0, 100);
        ResponseEntity<Page<ProductDTO>> response = productController.findAll(page);

        assertNotNull(response);
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(HttpStatus.OK, response.getStatusCode());

    }

    @Test
    void update_WhenSendProductFormPutDtoWithTotalElements_ExpectedResponseEntityProductDto() {
        when(productService.update(anyLong(), any())).thenReturn(ProductBuilder.getProductDTO());

        ResponseEntity<ProductDTO> response = productController.update(ProductBuilder.getProduct().getId(),
                ProductBuilder.getProductFormPutDTO());

        assertNotNull(response);
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}