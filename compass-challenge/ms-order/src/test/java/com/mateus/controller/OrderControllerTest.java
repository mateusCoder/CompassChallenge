package com.mateus.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mateus.builder.ProductBuilder;
import com.mateus.domain.Order;
import com.mateus.domain.constant.Status;
import com.mateus.domain.dto.OrderFormDTO;
import com.mateus.domain.dto.OrderProductsFormDTO;
import com.mateus.domain.dto.ProductDTO;
import com.mateus.exception.ObjectNotFound;
import com.mateus.repository.OrderRepository;
import com.mateus.repository.feignClients.ProductFeign;
import com.mateus.service.impl.OrderServiceImpl;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@AutoConfigureMockMvc()
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class OrderControllerTest {

    private final MockMvc mockMvc;

    private final ProductFeign productFeign;

    private final OrderRepository orderRepository;

    private final ObjectMapper objectMapper;

    @Mock
    OrderServiceImpl orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        ProductDTO product = productFeign.findByNameAndActiveTrue("Garrafa").getBody();
        Order order = orderRepository.save(new Order(null, "461.912.588-10", null, List.of("Garrafa"), product.getPrice(),
                LocalDate.now(), Status.ORDER_CREATED));
    }

    @Test
    public void save_WhenSendOrderFormDtoWithMissingElements_ExpectedBadRequestException() throws Exception {
        String orderRequest = objectMapper.writeValueAsString(new OrderFormDTO("",
                List.of(new OrderProductsFormDTO("Garrafa Inox", 1))));

        mockMvc.perform(MockMvcRequestBuilders.post("/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderRequest))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void findById_WhenSendOrderIdValid_ExpectedResponseEntityOrderDto() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/orders/{id}", 1L))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(Assertions::assertNotNull)
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void findById_WhenSendOrderIdNonExistent_ExpectedObjectNotFoundException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/orders/{id}", 20L))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof ObjectNotFound))
                .andDo(MockMvcResultHandlers.print());
    }


    @Test
    public void findByOrderNumber_WhenSendOrderNumberNonExistent_ExpectedObjectNotFoundException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/orders/orderNumber/{orderNumber}", "10"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof ObjectNotFound))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void findByOrderNumber_WhenSendOrderNumberValid_ExpectedResponseEntityOrderDto() throws Exception {
        Order order = orderRepository.save(new Order(null, "461.912.588-10",
                "fda02b10-a8f7-4df7-99a6-802edefd5624",
                List.of(ProductBuilder.getProductDTO().getBody().getName()),
                ProductBuilder.getProductDTO().getBody().getPrice(),
                LocalDate.now(), Status.ORDER_CREATED));
        orderRepository.save(order);
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/orders/orderNumber/{orderNumber}",
                        "fda02b10-a8f7-4df7-99a6-802edefd5624"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }


    @Test
    public void findByCpf_WhenSendCpfValid_ExpectedResponseEntityOrderDto() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/orders/customer/{cpf}", "461.912.588-10"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(Assertions::assertNotNull)
                .andDo(MockMvcResultHandlers.print());
    }

}