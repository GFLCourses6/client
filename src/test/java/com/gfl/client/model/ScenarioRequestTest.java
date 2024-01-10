package com.gfl.client.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ScenarioRequestTest {

    private final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = validatorFactory.getValidator();

    @Test
    void validateValidScenarioRequest() {
        ScenarioRequest scenarioRequest = new ScenarioRequest();
        scenarioRequest.setName("Scenario");
        scenarioRequest.setSite("http://example.com");
        scenarioRequest.setSteps(List.of(new StepRequest("click", "button")));
        Set<ConstraintViolation<ScenarioRequest>> violations = validator.validate(scenarioRequest);
        assertEquals(0, violations.size());
    }

    @ParameterizedTest
    @MethodSource("invalidScenarioRequests")
    void validateInvalidScenarioRequest(ScenarioRequest scenarioRequest, int expectedViolations) {
        Set<ConstraintViolation<ScenarioRequest>> violations = validator.validate(scenarioRequest);
        assertEquals(expectedViolations, violations.size());
    }

    private static Stream<Object[]> invalidScenarioRequests() {
        List<StepRequest> steps = Collections.singletonList(new StepRequest("click", "button"));
        List<StepRequest> step = Collections.singletonList(new StepRequest(null, null));
        return Stream.of(
                new Object[]{new ScenarioRequest(), 3},
                new Object[]{new ScenarioRequest("Scenario", "https://example.com", steps), 0},
                new Object[]{new ScenarioRequest("", "https://example.com", steps), 2},
                new Object[]{new ScenarioRequest("Scenario", "", steps), 2},
                new Object[]{new ScenarioRequest("Scenario", "https://example.com", step), 2},
                new Object[]{new ScenarioRequest("Scenario", "", new ArrayList<>()), 3}
        );
    }
}
