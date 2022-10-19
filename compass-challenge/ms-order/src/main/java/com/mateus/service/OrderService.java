package com.mateus.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mateus.domain.dto.OrderDTO;
import com.mateus.domain.dto.OrderFormDTO;
import com.mateus.exception.BusinessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.net.URI;

public interface OrderService {
    
    URI save(OrderFormDTO orderFormDTO) throws JsonProcessingException;
    
    void updateOrder(String order) throws JsonProcessingException, BusinessException;

    Page<OrderDTO> findByCpf(String cpf, Pageable pageable);

    OrderDTO findById(Long id);

    OrderDTO findByOrderNumber(Long orderNumber);

}
