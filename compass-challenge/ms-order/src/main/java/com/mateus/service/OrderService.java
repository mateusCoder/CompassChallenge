package com.mateus.service;

import com.mateus.domain.dto.OrderDTO;
import com.mateus.domain.dto.OrderFormDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.net.URI;

public interface OrderService {
    URI save(OrderFormDTO orderFormDTO);

    OrderDTO findById(Long id);
}
