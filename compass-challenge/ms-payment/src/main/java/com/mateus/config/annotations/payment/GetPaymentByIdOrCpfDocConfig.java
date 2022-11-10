package com.mateus.config.annotations.payment;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Operation(summary = "List all payments or a list of a specific CPF or a specific payment",
        description = "This method returns a all payments or a list of a specific CPF or a specific payment. Use the payment id and cpf or don't use parameters for all payments.",
        tags = {"Payment"})
@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Ok", content = @Content(mediaType = "application/json"))} )
public @interface GetPaymentByIdOrCpfDocConfig {
}
