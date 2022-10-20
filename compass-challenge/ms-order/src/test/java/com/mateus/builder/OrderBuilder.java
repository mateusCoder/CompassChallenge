package com.mateus.builder;

import com.mateus.domain.Order;
import com.mateus.domain.Product;
import com.mateus.domain.constant.Status;
import com.mateus.domain.dto.OrderDTO;
import com.mateus.domain.dto.OrderDataProcessingDTO;
import com.mateus.domain.dto.OrderFormDTO;
import com.mateus.domain.dto.OrderProductsFormDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class OrderBuilder {

    private static final Long id = 1L;

    private static final String cpf = "461.912.588-10";

    private static final Long orderNumber = 1L;

    private static final List<Product> products = new ArrayList<>(Set.of(ProductBuilder.getProduct()));

    private static final BigDecimal totalOrderPrice = BigDecimal.valueOf(1000);

    private static final LocalDate localDate = LocalDate.now();

    private static final Status status = Status.ORDER_CREATED;

    private static final String productsName = ProductBuilder.getProduct().getName();

    private static final Integer amount = 10;

    private static final List<OrderProductsFormDTO> orderProducts = new ArrayList<>(Set.of(OrderBuilder.getOrderProductsFormDTO()));

    public static Order getOrder(){
        return Order.builder()
                .id(id)
                .cpf(cpf)
                .orderNumber(orderNumber)
                .products(products)
                .totalOrderPrice(totalOrderPrice)
                .localDate(localDate)
                .status(status)
                .build();
    }

    public static OrderDTO getOrderDTO(){
        return OrderDTO.builder()
                .id(id)
                .cpf(cpf)
                .orderNumber(orderNumber)
                .products(products)
                .totalOrderPrice(totalOrderPrice)
                .localDate(localDate)
                .status(status)
                .build();
    }

    public static OrderProductsFormDTO getOrderProductsFormDTO(){
        return OrderProductsFormDTO.builder()
                .productsName(productsName)
                .amount(amount)
                .build();
    }

    public static OrderFormDTO getOrderFormDTO(){
        return OrderFormDTO.builder()
                .cpf(cpf)
                .orderProducts(orderProducts)
                .build();
    }

    public static OrderDataProcessingDTO getOrderDataProcessingDTORequest(){
        return OrderDataProcessingDTO.builder()
                .id(id)
                .cpf(cpf)
                .orderNumber(orderNumber)
                .totalOrderPrice(totalOrderPrice)
                .status(status)
                .build();
    }

    public static OrderDataProcessingDTO getOrderDataProcessingDTOUpdated(){
        return OrderDataProcessingDTO.builder()
                .id(id)
                .cpf(cpf)
                .orderNumber(orderNumber)
                .totalOrderPrice(totalOrderPrice)
                .status(Status.UNAUTHORIZED_PAYMENT)
                .build();
    }

    public static Page<Order> getOrderPage(){
        return new PageImpl<>(List.of(getOrder()));
    }

    public static Page<OrderDTO> getOrderDtoPage(){
        return new PageImpl<>(List.of(getOrderDTO()));
    }
}
