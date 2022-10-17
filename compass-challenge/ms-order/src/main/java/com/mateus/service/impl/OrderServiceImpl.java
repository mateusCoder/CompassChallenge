package com.mateus.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mateus.domain.Order;
import com.mateus.domain.Product;
import com.mateus.domain.constant.Status;
import com.mateus.domain.dto.OrderDTO;
import com.mateus.domain.dto.OrderDataProcessingDTO;
import com.mateus.domain.dto.OrderFormDTO;
import com.mateus.domain.dto.OrderProductsFormDTO;
import com.mateus.exception.ObjectNotFound;
import com.mateus.repository.OrderRepository;
import com.mateus.repository.ProductRepository;
import com.mateus.service.OrderService;
import com.mateus.util.QueueUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final ProductRepository productRepository;

    private final ModelMapper mapper;

    private final RabbitTemplate rabbitTemplate;

    private Long orderNumber = 1L;

    @Override
    public URI save(OrderFormDTO orderFormDTO) throws JsonProcessingException {
        Order order = mapper.map(orderFormDTO, Order.class);
        List<Product> products = orderFormDTO.getOrderProducts().stream().map(e ->
                productRepository.findByNameAndActiveTrue(e.getProductsName())
                        .orElseThrow(() -> new ObjectNotFound("Product Not Found!"))).toList();
        List<BigDecimal> productsPrice = products.stream().map(Product::getPrice).toList();
        List<Integer> productsAmount = orderFormDTO.getOrderProducts().stream()
                .map(OrderProductsFormDTO::getAmount).toList();

        order.setNumber(orderNumber++);
        products.forEach(order::setProducts);
        order.setTotalOrderPrice(calculateTotalOrderAmount(productsPrice, productsAmount));
        order.setLocalDate(LocalDate.now());
        order.setStatus(Status.ORDER_CREATED);
        orderRepository.save(order);

        String response = convertIntoJson(mapper.map(order, OrderDataProcessingDTO.class));
        rabbitTemplate.convertAndSend(QueueUtils.ORDER_NOTIFICATION, response);

        return ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").build(order.getId());
    }

    @Override
    public OrderDTO findById(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new ObjectNotFound("Order Not Found!"));
        return mapper.map(order, OrderDTO.class);
    }

    @Override
    public void updateOrder(String order) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        OrderDataProcessingDTO orderProcessing = objectMapper.readValue(order, OrderDataProcessingDTO.class);
        Order orderUpdate = orderRepository.findById(orderProcessing.getId()).orElseThrow(() -> new RuntimeException());
        orderUpdate.setStatus(orderProcessing.getStatus());
        orderRepository.save(orderUpdate);
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
