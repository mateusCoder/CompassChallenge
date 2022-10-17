package com.mateus.domain.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Status {

    @JsonProperty("orderCreated")
    ORDER_CREATED("orderCreated"),

    @JsonProperty("paymentConfirmed")
    PAYMENT_CONFIRMED("paymentConfirmed"),

    @JsonProperty("unauthorizedPayment")
    UNAUTHORIZED_PAYMENT("unauthorizedPayment");

    private String description;

    private Status(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @JsonCreator
    public static Status forValues(@JsonProperty("description") String description) {
        for (Status status : Status.values()) {
            if (status.description.equals(description)) {
                return status;
            }
        }
        return null;
    }

}
