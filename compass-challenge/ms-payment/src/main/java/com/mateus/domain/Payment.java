package com.mateus.domain;

import com.mateus.domain.constant.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity(name = "PAYMENT_DB")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderNumber;

    private String cpf;

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDate date;

    @PrePersist
    public void setDate(){
        date = LocalDate.now();
    }
}
