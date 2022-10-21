package controller;

import com.mateus.controller.ResourceExceptionHandler;
import com.mateus.exception.Conflict;
import com.mateus.exception.ObjectNotFound;
import com.mateus.exception.StandardError;
import com.mateus.exception.ValidationError;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@AutoConfigureTestDatabase
class ResourceExceptionHandlerTest {

    @InjectMocks
    ResourceExceptionHandler resourceExceptionHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void objectNotFound_WhenInstantiatingAnObjectNotFound_ExpectedObjectNotFoundException() {
        ResponseEntity<StandardError> response = resourceExceptionHandler
                .objectNotFound(
                        new ObjectNotFound("Object Not Found!"),
                        new MockHttpServletRequest()
                );

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(StandardError.class, response.getBody().getClass());
        assertEquals("Object Not Found!", response.getBody().getMessage());
        assertEquals(404, response.getBody().getStatus());
    }

    @Test
    void conflict_WhenInstantiatingAnObjectConflict_ExpectedConflictException() {
        ResponseEntity<StandardError> response = resourceExceptionHandler
                .conflict(
                        new Conflict("Conflict Exception"),
                        new MockHttpServletRequest()
                );

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(StandardError.class, response.getBody().getClass());
        assertEquals("Conflict Exception", response.getBody().getMessage());
        assertEquals(409, response.getBody().getStatus());
    }

    @Test
    void validation_WhenInstantiatingAnObjectValidation_ExpectedBadRequestException() {
        MethodParameter methodParameter = mock(MethodParameter.class);
        BindingResult bindingResult = mock(BindingResult.class);

        when(bindingResult.getFieldErrors()).thenReturn(Arrays.asList (new FieldError("object", "400", "bad request")));

        ResponseEntity<ValidationError> response = resourceExceptionHandler
                .validation(new MethodArgumentNotValidException(methodParameter, bindingResult));

        assertNotNull(response);
        assertNotNull(response.getBody());
        when(bindingResult.getFieldErrors()).thenReturn(Arrays.asList(new FieldError("object", "400", "bad request")));
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(400, response.getStatusCodeValue());
    }
}