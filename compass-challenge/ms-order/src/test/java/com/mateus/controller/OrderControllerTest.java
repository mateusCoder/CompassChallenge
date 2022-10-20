package com.mateus.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mateus.builder.OrderBuilder;
import com.mateus.domain.dto.OrderDTO;
import com.mateus.exception.BusinessException;
import com.mateus.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@AutoConfigureTestDatabase
class OrderControllerTest {

    @InjectMocks
    OrderController orderController;

    @Mock
    OrderServiceImpl orderService;

    @Mock
    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void save_WhenSendOrderFormDtoWithTotalElements_ExpectedResponseEntityOrderDto() throws JsonProcessingException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/v1/orders/{id}")
                .build(OrderBuilder.getOrder().getId());

        when(orderService.save(any())).thenReturn(uri);

        ResponseEntity<OrderDTO> response = orderController.save(OrderBuilder.getOrderFormDTO());

        assertNotNull(response);
        assertEquals(uri.toString(), "http://localhost/v1/orders/1");
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void findById_WhenSendOrderIdValid_ExpectedResponseEntityOrderDto(){
        when(orderService.findById(anyLong())).thenReturn(OrderBuilder.getOrderDTO());

        ResponseEntity<OrderDTO> response = orderController.findById(OrderBuilder.getOrder().getId());

        assertNotNull(response);
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void findByOrderNumber_WhenSendOrderNumberValid_ExpectedResponseEntityOrderDto(){
        when(orderService.findByOrderNumber(anyLong())).thenReturn(OrderBuilder.getOrderDTO());

        ResponseEntity<OrderDTO> response = orderController.findByOrderNumber(OrderBuilder.getOrder().getOrderNumber());

        assertNotNull(response);
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void findByCpf_WhenSendCpfValid_ExpectedResponseEntityOrderDto() {
        when(orderService.findByCpf(any(), (Pageable) any())).thenReturn(OrderBuilder.getOrderDtoPage());

        Pageable page = PageRequest.of(0, 100);
        ResponseEntity<Page<OrderDTO>> response = orderController.findByCpf(OrderBuilder.getOrder().getCpf(), page);

        assertNotNull(response);
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void updateOrder_WhenListenerOrderValid_ExpectedUpdateOrderStatusById() throws BusinessException, JsonProcessingException {
        doNothing().when(orderService).updateOrder(any());
        String order = objectMapper.writeValueAsString(OrderBuilder.getOrder());

        orderController.updateOrder(order);

        verify(orderService, times(1)).updateOrder(order);
    }
}