package com.gfl.client.exception.handler;

import com.gfl.client.mapper.ProxyMapper;
import com.gfl.client.service.proxy.queue.AsyncProxyQueueTaskProcessor;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.apache.http.client.methods.HttpUriRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebMvcTest(ErrorHandlerController.class)
class ErrorHandlerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AsyncProxyQueueTaskProcessor asyncProxyQueueTaskProcessor;

    @MockBean
    HttpUriRequest httpUriRequest;

    @MockBean
    private ProxyMapper mapper;

    @InjectMocks
    private ErrorHandlerController handler;

    private MethodArgumentNotValidException exception;
    private BindingResult bindingResult;
    private FieldError fieldError;


    @BeforeEach
    void setUp() {
        bindingResult = mock(BindingResult.class);
        exception = mock(MethodArgumentNotValidException.class);
        fieldError = mock(org.springframework.validation.FieldError.class);
    }

    @Test
    void testHandleMethodArgumentNotValidException() {

        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(Collections.singletonList(fieldError));
        when(fieldError.getDefaultMessage()).thenReturn("error message");

        ResponseEntity<String> response = handler.handleValidationException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Validation errors: error message; ", response.getBody());
        verify(exception).getBindingResult();
        verify(bindingResult).getAllErrors();
        verify(fieldError).getDefaultMessage();
    }


    @Test
    void testHandleMethodArgumentNotValidExceptions() {
        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(Collections.singletonList(fieldError));
        when(fieldError.getDefaultMessage()).thenReturn("error message");

        ResponseEntity<String> response = handler.handleValidationException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Validation errors: error message; ", response.getBody());
        verify(fieldError).getDefaultMessage();
        verify(exception).getBindingResult();
        verify(bindingResult).getAllErrors();
    }

    @Test
    void testHandleConstraintViolationException() {
        ConstraintViolationException ex = mock(ConstraintViolationException.class);
        ConstraintViolation violation = mock(ConstraintViolation.class);
        Set<ConstraintViolation<?>> violations = Collections.singleton(violation);
        when(ex.getConstraintViolations()).thenReturn(violations);
        when(violation.getMessage()).thenReturn("error message");

        ResponseEntity<String> response = handler.handleValidationException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Validation error(s): error message; ", response.getBody());
        verify(ex).getConstraintViolations();
        verify(violation).getMessage();
    }

    @Test
    void testHandleDataIntegrityViolationException() {
        DataIntegrityViolationException ex = mock(DataIntegrityViolationException.class);
        when(ex.getMessage()).thenReturn("error message");
        ResponseEntity<String> response = handler.handleDataIntegrityViolation(ex);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("error message", response.getBody());
        verify(ex).getMessage();
    }

    @Test
    void testHandleException() {
        Exception ex = mock(Exception.class);
        when(ex.getMessage()).thenReturn("error message");
        ResponseEntity<String> response = handler.handleException(ex);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("error message", response.getBody());
        verify(ex).getMessage();
    }

    @Test
    void testHandleAllUncaughtException() {
        RuntimeException ex = mock(RuntimeException.class);
        when(ex.getMessage()).thenReturn("error message");
        ResponseEntity<String> response = handler.handleAllUncaughtException(ex);
        assertEquals(HttpStatus.METHOD_NOT_ALLOWED, response.getStatusCode());
        assertEquals("error message", response.getBody());
        verify(ex).getMessage();
    }

    @Test
    void testHandleConstraintViolationExceptions() {
        ConstraintViolationException ex = mock(ConstraintViolationException.class);
        ConstraintViolation violation = mock(ConstraintViolation.class);

        Set<ConstraintViolation<?>> violations = Collections.singleton(violation);
        when(ex.getConstraintViolations()).thenReturn(violations);
        when(violation.getMessage()).thenReturn("error message");

        ResponseEntity<String> response = handler.handleValidationException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Validation error(s): error message; ", response.getBody());
        verify(ex).getConstraintViolations();
        verify(violation).getMessage();
    }
}
