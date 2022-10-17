package com.mateus.exception.handler;

import com.mateus.exception.Conflict;
import com.mateus.exception.ObjectNotFound;
import com.mateus.exception.StandardError;
import com.mateus.exception.ValidationError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;

@RestControllerAdvice
public class ResourceExceptionHandler {

    @ExceptionHandler(ObjectNotFound.class)
    public ResponseEntity<StandardError> objectNotfound(ObjectNotFound ex,
                                                        HttpServletRequest request){
        StandardError error = new StandardError(
                Instant.now(),
                HttpStatus.NOT_FOUND.value(),
                "Not Found",
                ex.getMessage(),
                request.getRequestURI());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(Conflict.class)
    public ResponseEntity<StandardError> conflict(Conflict ex,
                                                  HttpServletRequest request){
        StandardError error = new StandardError(
                Instant.now(),
                HttpStatus.CONFLICT.value(),
                "Conflict",
                ex.getMessage(),
                request.getRequestURI());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationError> validation(MethodArgumentNotValidException ex){
        ValidationError error = new ValidationError();

        for(FieldError f : ex.getBindingResult().getFieldErrors()){
            error.addError(f.getField(), f.getDefaultMessage());
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}

