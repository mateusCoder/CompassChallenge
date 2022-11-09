package com.mateus.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mateus.builder.ProductBuilder;
import com.mateus.domain.dto.ProductFormPostDTO;
import com.mateus.domain.dto.ProductFormPutDTO;
import com.mateus.exception.Conflict;
import com.mateus.exception.ObjectNotFound;
import com.mateus.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.math.BigDecimal;

@SpringBootTest
@AutoConfigureMockMvc()
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class ProductControllerTest {

    private final MockMvc mockMvc;

    private final ProductRepository productRepository;

    private final ObjectMapper objectMapper;

    @BeforeEach
    void setUp(){
        productRepository.save(ProductBuilder.getProduct());
    }

    @Test
    public void save_WhenSendProductFormPostDtoWithTotalElements_ExpectedResponseEntityProductDto() throws Exception {
        String productRequest = objectMapper.writeValueAsString(
                new ProductFormPostDTO("Pote Plastico", "Pote 1L transparente plastico",
                        BigDecimal.valueOf(10)));

        mockMvc.perform(MockMvcRequestBuilders.post("/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productRequest))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    public void save_WhenSendProductFormPostDtoWithMissingElements_ExpectedBadRequestException()  throws Exception {
        String productRequest = objectMapper.writeValueAsString(
                new ProductFormPostDTO("", "Pote 1L transparente plastico",
                        BigDecimal.valueOf(10)));

        mockMvc.perform(MockMvcRequestBuilders.post("/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productRequest))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void save_WhenSendProductFormPostDtoWithProductExistingName_ExpectedConflictException() throws Exception {
        String productRequest = objectMapper.writeValueAsString(ProductBuilder.getProductFormPostDTO());

        mockMvc.perform(MockMvcRequestBuilders.post("/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(productRequest))
                .andExpect(MockMvcResultMatchers.status().isConflict())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof Conflict))
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    void findAll_WhenFindAll_ExpectedResponseEntityPageableProductDto() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/products"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(Assertions::assertNotNull)
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    void update_WhenSendProductFormPutDtoWithTotalElements_ExpectedResponseEntityProductDto() throws Exception {
        productRepository.save(ProductBuilder.getProduct());
        String productRequest = objectMapper.writeValueAsString(ProductBuilder.getProductFormPutDTO());
        mockMvc.perform(MockMvcRequestBuilders.put("/v1/products/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productRequest))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void update_WhenSendProductFormPutDtoWithAInvalidId_ExpectedObjectNotFoundException() throws Exception {
        String productRequest = objectMapper.writeValueAsString(ProductBuilder.getProductFormPutDTO());

        mockMvc.perform(MockMvcRequestBuilders.put("/v1/products/{id}", 10L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productRequest))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof ObjectNotFound))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void update_WhenSendProductFormPutDtoWithMissingElements_ExpectedBadRequestException() throws Exception {
        String productRequest = objectMapper.writeValueAsString(
                new ProductFormPutDTO("", "Pote 1L transparente plastico",
                BigDecimal.valueOf(10), true));

        mockMvc.perform(MockMvcRequestBuilders.put("/v1/products/{id}", 3L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productRequest))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andDo(MockMvcResultHandlers.print());
    }

}