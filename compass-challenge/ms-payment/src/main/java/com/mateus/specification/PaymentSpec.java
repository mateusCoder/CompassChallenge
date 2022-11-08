package com.mateus.specification;

import com.mateus.domain.Payment;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class PaymentSpec{

    public static Specification<Payment> getSpecification(Long id, String cpf){
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicate = new ArrayList<>();

            if(id != null){
                predicate.add(criteriaBuilder.equal(root.get("id"), id));
            }
            if (cpf != null){
                predicate.add(criteriaBuilder.equal(root.get("cpf"), cpf));
            }

            return criteriaBuilder.and(predicate.toArray(new Predicate[0]));
        });
    }
}
