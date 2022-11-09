package com.mateus.domain;

import com.mateus.domain.constant.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity(name = "ORDER_DB")
public class Order implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cpf;

    private String orderNumber;

    @ElementCollection(targetClass=String.class)
    private List<String> products = new ArrayList<>();

    private BigDecimal totalOrderPrice;

    private LocalDate localDate;

    @Enumerated(EnumType.STRING)
    private Status status;

    public void setProducts(String productName) {
        this.products.add(productName);
    }

    @PrePersist
    public void setDate(){
        localDate = LocalDate.now();
    }
}
