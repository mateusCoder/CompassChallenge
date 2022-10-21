package com.mateus.config.annotations.product;

import com.mateus.domain.dto.ProductDTO;
import com.mateus.exception.FieldMessage;
import com.mateus.exception.StandardError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Operation(summary = "Change a specific product", description = "This method change a specific product. Use the product id.", tags = {"Product"})
@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Ok", content = @Content(schema = @Schema(implementation = ProductDTO.class), mediaType = "application/json")),
        @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(array = @ArraySchema(schema = @Schema(implementation = FieldMessage.class)), mediaType = "application/json")),
        @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = StandardError.class), mediaType = "application/json"))} )
public @interface UpdateProductDocConfig {
}
