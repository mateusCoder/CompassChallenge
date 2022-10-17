package com.mateus.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mateus.domain.dto.OrderDTO;
import com.mateus.domain.dto.OrderFormDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.net.URI;

public interface OrderService {
    URI save(OrderFormDTO orderFormDTO) throws JsonProcessingException;

    OrderDTO findById(Long id);

    void updateOrder(String order) throws JsonProcessingException;
}
