package com.mateus.service.impl;

import com.mateus.builder.ProductBuilder;
import com.mateus.domain.Product;
import com.mateus.domain.dto.ProductDTO;
import com.mateus.exception.Conflict;
import com.mateus.exception.ObjectNotFound;
import com.mateus.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
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
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.net.URI;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@AutoConfigureTestDatabase
class ProductServiceImplTest {

    @InjectMocks
    ProductServiceImpl productService;

    @Mock
    ProductRepository productRepository;

    @Spy
    ModelMapper mapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void save_WhenSendProductFormPostDtoWithTotalElements_ExpectedNewURI() {
        MockHttpServletRequest request =new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        when(productRepository.existsByName(any())).thenReturn(false);
        when(productRepository.save(any())).thenReturn(ProductBuilder.getProduct());

        URI response = productService.save(ProductBuilder.getProductFormPostDTO());

        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    public void save_WhenSendProductFormPostDtoWithProductExistingName_ExpectedConflictException() {
        MockHttpServletRequest request =new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        when(productRepository.existsByName(any())).thenReturn(true);
        when(productRepository.save(any())).thenReturn(ProductBuilder.getProduct());

        Conflict response = assertThrows(Conflict.class, () ->
                productService.save(ProductBuilder.getProductFormPostDTO()));

        assertEquals("Product already exists", response.getMessage());
    }

    @Test
    public void findAll_WhenFindAll_ExpectedPageableProductDto() {
        when(productRepository.findAll((Pageable) any())).thenReturn(ProductBuilder.getProductPage());

        Pageable page = PageRequest.of(0, 100);
        Page<ProductDTO> response = productService.findAll(page);

        assertNotNull(response);
        assertEquals(1, response.getTotalElements());
    }

    @Test
    public void update_WhenSendProductFormPutDtoWithTotalElements_ExpectedProductDto() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(ProductBuilder.getProduct()));
        when(productRepository.save(any())).thenReturn(ProductBuilder.getProduct());

        ProductDTO response = productService.update(ProductBuilder.getProduct().getId(),
                ProductBuilder.getProductFormPutDTO());

        assertNotNull(response);
        assertEquals(ProductDTO.class, response.getClass());
        assertEquals(ProductBuilder.getProduct().getName(), response.getName());
    }

    @Test
    public void update_WhenSendProductFormPutDtoWithAInvalidId_ExpectedObjectNotFoundException() {
        when(productRepository.save(any())).thenReturn(ProductBuilder.getProduct());

        ObjectNotFound response = assertThrows(ObjectNotFound.class, () ->
                productService.update(2L, ProductBuilder.getProductFormPutDTO()));

        assertEquals("Product Not Found!", response.getMessage());
    }
}