package com.mateus.domain;

import com.mateus.domain.constant.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity(name="\"Order\"")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cpf;

    private String number;

    @OneToMany
    @JoinColumn(name = "orderId", referencedColumnName = "id")
    private List<Product> products = new ArrayList<>();

    private BigDecimal totalOrderAmount;

    private LocalDate localDate;

    @Enumerated(EnumType.STRING)
    private Status status;
}
