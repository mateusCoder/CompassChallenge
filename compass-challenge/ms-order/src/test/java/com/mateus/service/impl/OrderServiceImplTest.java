package com.mateus.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mateus.builder.OrderBuilder;
import com.mateus.builder.ProductBuilder;
import com.mateus.domain.Order;
import com.mateus.domain.Product;
import com.mateus.domain.dto.OrderDTO;
import com.mateus.domain.dto.OrderDataProcessingDTO;
import com.mateus.domain.dto.ProductDTO;
import com.mateus.exception.BusinessException;
import com.mateus.exception.Conflict;
import com.mateus.exception.ObjectNotFound;
import com.mateus.repository.OrderRepository;
import com.mateus.repository.ProductRepository;
import com.mateus.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@AutoConfigureTestDatabase
class OrderServiceImplTest {

    @InjectMocks
    OrderServiceImpl orderService;

    @Mock
    OrderRepository orderRepository;

    @Mock
    ProductRepository productRepository;

    @Mock
    RabbitTemplate rabbitTemplate;

    @Spy
    ModelMapper mapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void save_WhenSendOrderFormDtoWithTotalElementsValid_ExpectedOrderDto() throws JsonProcessingException {
        MockHttpServletRequest request =new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        when(productRepository.findByNameAndActiveTrue(any())).thenReturn(Optional.of(ProductBuilder.getProduct()));
        when(orderRepository.save(any())).thenReturn(OrderBuilder.getOrder());
        doNothing().when(rabbitTemplate).convertAndSend(any());

        URI response = orderService.save(OrderBuilder.getOrderFormDTO());

        verify(productRepository, times(1)).findByNameAndActiveTrue(anyString());
        verify(orderRepository, times(1)).save(any(Order.class));

    }

    @Test
    public void save_WhenSendOrderFormDtoWithProductNameInvalid_ExpectedOrderDto() throws JsonProcessingException {
        MockHttpServletRequest request =new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        when(orderRepository.save(any())).thenReturn(OrderBuilder.getOrder());

        ObjectNotFound response = assertThrows(ObjectNotFound.class, () ->
                orderService.save(OrderBuilder.getOrderFormDTO()));

        assertEquals("Product Not Found!", response.getMessage());

    }

    @Test
    public void findById_WhenSendOrderIdValid_ExpectedOrderDto() {
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(OrderBuilder.getOrder()));

        OrderDTO response = orderService.findById(OrderBuilder.getOrder().getId());

        assertNotNull(response);
        assertEquals(OrderDTO.class, response.getClass());
        assertEquals(OrderBuilder.getOrder().getOrderNumber(), response.getOrderNumber());
    }

    @Test
    public void findById_WhenSendOrderIdInvalid_ExpectedObjectNotFoundException() {
        ObjectNotFound response = assertThrows(ObjectNotFound.class, () ->
                orderService.findById(OrderBuilder.getOrder().getId()));

        assertEquals("Order Not Found!", response.getMessage());
    }

    @Test
    public void findByOrderNumber_WhenSendOrderNumberValid_OrderDto() {
        when(orderRepository.findByOrderNumber(anyLong())).thenReturn(Optional.of(OrderBuilder.getOrder()));

        OrderDTO response = orderService.findByOrderNumber(OrderBuilder.getOrder().getId());

        assertNotNull(response);
        assertEquals(OrderDTO.class, response.getClass());
        assertEquals(OrderBuilder.getOrder().getOrderNumber(), response.getOrderNumber());
    }

    @Test
    public void findByOrderNumber_WhenSendOrderNumberInvalid_ExpectedObjectNotFoundException() {
        ObjectNotFound response = assertThrows(ObjectNotFound.class, () ->
                orderService.findByOrderNumber(OrderBuilder.getOrder().getId()));

        assertEquals("Order Not Found!", response.getMessage());
    }

    @Test
    public void findByCpf_WhenSendCpfValid_ExpectedOrderDto() {
        when(orderRepository.findByCpf(anyString(), (Pageable) any())).thenReturn(OrderBuilder.getOrderPage());

        Pageable page = PageRequest.of(0, 100);
        Page<OrderDTO> response = orderService.findByCpf(OrderBuilder.getOrder().getCpf(), page);

        assertNotNull(response);
        assertEquals(1, response.getTotalElements());
    }


    @Test
    public void updateOrder_WhenListenerOrderValid_ExpectedUpdateOrderStatusById() throws JsonProcessingException, BusinessException {
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(OrderBuilder.getOrder()));
        when(orderRepository.save(any())).thenReturn(OrderBuilder.getOrder());

        ObjectMapper objectMapper = new ObjectMapper();
        String order = objectMapper.writeValueAsString(OrderBuilder.getOrderDataProcessingDTORequest());
        orderService.updateOrder(order);

        verify(orderRepository, times(1)).save(any(Order.class));
        verify(orderRepository, times(1)).findById(anyLong());
    }

    @Test
    public void updateOrder_WhenListenerOrderWithProductIdInvalid_ExpectedObjectNotFoundException() throws JsonProcessingException {
        when(orderRepository.save(any())).thenReturn(OrderBuilder.getOrder());
        ObjectMapper objectMapper = new ObjectMapper();
        String order = objectMapper.writeValueAsString(OrderBuilder.getOrderDataProcessingDTORequest());

        ObjectNotFound response = assertThrows(ObjectNotFound.class, () ->
                orderService.updateOrder(order));

        assertEquals("Product Not Found!", response.getMessage());
    }

}