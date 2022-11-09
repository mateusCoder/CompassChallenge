package com.mateus.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mateus.domain.Order;
import com.mateus.domain.constant.Status;
import com.mateus.domain.dto.*;
import com.mateus.exception.BusinessException;
import com.mateus.exception.ObjectNotFound;
import com.mateus.repository.OrderRepository;
import com.mateus.repository.feignClients.ProductFeign;
import com.mateus.service.OrderService;
import com.mateus.util.QueueUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final ModelMapper mapper;

    private final RabbitTemplate rabbitTemplate;

    private final ProductFeign productFeign;

    @Override
    public URI save(OrderFormDTO orderFormDTO) throws JsonProcessingException {
        Order order = mapper.map(orderFormDTO, Order.class);
        List<ProductDTO> products = orderFormDTO.getOrderProducts().stream().map(e ->
                productFeign.findByNameAndActiveTrue(e.getProductsName()).getBody()).toList();
        List<BigDecimal> productsPrice = products.stream().map(ProductDTO::getPrice).toList();
        List<Integer> productsAmount = orderFormDTO.getOrderProducts().stream()
                .map(OrderProductsFormDTO::getAmount).toList();

        order.setOrderNumber(UUID.randomUUID().toString());
        products.stream().map(ProductDTO::getName).toList().forEach(order::setProducts);
        order.setTotalOrderPrice(calculateTotalOrderAmount(productsPrice, productsAmount));
        order.setStatus(Status.ORDER_CREATED);
        orderRepository.save(order);

        String response = convertIntoJson(mapper.map(order, OrderDataProcessingDTO.class));
        rabbitTemplate.convertAndSend(QueueUtils.ORDER_NOTIFICATION, response);

        log.info("Order saved and sent successfully");
        return ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").build(order.getId());
    }

    @Override
    public OrderDTO findById(Long id) {
        return mapper.map(orderRepository.findById(id).orElseThrow(() -> {
            log.error("Error trying to find order by non-existent id");
            throw new ObjectNotFound("Order Not Found!");
        }), OrderDTO.class);
    }

    @Override
    public OrderDTO findByOrderNumber(String orderNumber) {
        return mapper.map(orderRepository.findByOrderNumber(orderNumber).orElseThrow(() -> {
            log.error("Error trying to find order by non-existent orderNumber");
            throw new ObjectNotFound("Order Not Found!");
        }), OrderDTO.class);
    }

    @Override
    public Page<OrderDTO> findByCpf(String cpf, Pageable pageable) {
        Page<Order> orders = orderRepository.findByCpf(cpf, pageable);
        return orders.map(e -> mapper.map(e, OrderDTO.class));
    }

    @Override
    public void updateOrder(String order) throws BusinessException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        OrderDataProcessingDTO orderProcessing = objectMapper.readValue(order, OrderDataProcessingDTO.class);
        Order orderUpdate = orderRepository.findById(orderProcessing.getId()).orElseThrow(() -> {
            log.error("Error trying to update status order by non-existent id");
            throw new ObjectNotFound("Product Not Found!");
        });
        orderUpdate.setStatus(orderProcessing.getStatus());
        orderRepository.save(orderUpdate);
        log.info("Order status updated successfully");
    }

    protected BigDecimal calculateTotalOrderAmount(List<BigDecimal> productsPrice, List<Integer> productsAmount) {
        BigDecimal total = BigDecimal.valueOf(0);
        for(int i = 0; i < productsPrice.size(); i++) {
            BigDecimal productPrice = productsPrice.get(i);
            BigDecimal productAmount = BigDecimal.valueOf(productsAmount.get(i));
            total = total.add(productPrice.multiply(productAmount));
        }
        return total;
    }

    protected String convertIntoJson(OrderDataProcessingDTO order) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(order);
    }
}
