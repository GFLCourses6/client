package com.gfl.client.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class StepRequestTest {

    private final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = validatorFactory.getValidator();

    @Test
    void validateValidStepRequest() {
        StepRequest stepRequest = new StepRequest("click", "button");
        Set<ConstraintViolation<StepRequest>> violations = validator.validate(stepRequest);
        assertTrue(violations.isEmpty());
    }

    @ParameterizedTest
    @MethodSource("invalidStepRequests")
    void validateInvalidStepRequest(StepRequest stepRequest, int expectedViolations) {
        Set<ConstraintViolation<StepRequest>> violations = validator.validate(stepRequest);
        assertEquals(expectedViolations, violations.size());
    }

    private static Stream<Object[]> invalidStepRequests() {
        return Stream.of(
                new Object[]{new StepRequest(null, "button"), 2},
                new Object[]{new StepRequest("click", null), 2},
                new Object[]{new StepRequest("", "button"), 2},
                new Object[]{new StepRequest("click", ""), 2},
                new Object[]{new StepRequest("click", "a".repeat(101)), 1},
                new Object[]{new StepRequest("a".repeat(51), "button"), 1}
        );
    }

    @Test
    void validStepRequest() {
        StepRequest stepRequest = new StepRequest("click", "button");

        assertDoesNotThrow(() -> {
            assertNotNull(stepRequest.getAction());
            assertNotNull(stepRequest.getValue());
            assertNotEquals("", stepRequest.getAction());
            assertNotEquals("", stepRequest.getValue());
        });
    }

    @Test
    void stepRequestWithBlankAction() {
        StepRequest stepRequest = new StepRequest("", "button");
        Set<ConstraintViolation<StepRequest>> violations = validator.validate(stepRequest);
        if (!violations.isEmpty()) {
            assertThrows(ConstraintViolationException.class, () -> {
                throw new ConstraintViolationException("Validation failed", violations);
            });
        }
        assertEquals(2, violations.size());
    }

    @Test
    void stepRequestWithBlankValue() {
        StepRequest stepRequest = new StepRequest("click", "");
        Set<ConstraintViolation<StepRequest>> violations = validator.validate(stepRequest);
        if (!violations.isEmpty()) {
            assertThrows(ConstraintViolationException.class, () -> {
                throw new ConstraintViolationException("Validation failed", violations);
            });
        }
        assertEquals(2, violations.size());
    }

    @Test
    void stepRequestWithExceedingMaxLengthAction() {
        StepRequest stepRequest = new StepRequest("a".repeat(51), "button");
        Set<ConstraintViolation<StepRequest>> violations = validator.validate(stepRequest);
        if (!violations.isEmpty()) {
            assertThrows(ConstraintViolationException.class, () -> {
                throw new ConstraintViolationException("Validation failed", violations);
            });
            assertEquals(1, violations.size());
            assertEquals("Action must be between 1 and 50 characters", violations.iterator().next().getMessage());
        } else {
            fail("Expected ConstraintViolationException, but no violations found.");
        }
    }

    @Test
    void stepRequestWithExceedingMaxLengthValue() {
        StepRequest stepRequest = new StepRequest("click", "a".repeat(101));
        Set<ConstraintViolation<StepRequest>> violations = validator.validate(stepRequest);
        assertThrows(ConstraintViolationException.class, () -> {
            throw new ConstraintViolationException("Validation failed", violations);
        });
        assertEquals(1, violations.size());
        assertEquals("Value must be between 1 and 100 characters", violations.iterator().next().getMessage());
    }
}
