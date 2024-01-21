package com.gfl.client.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Setter
@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class StepResult {

    private Long id;
    private Step step;
    private String executionMessage;
    private ScenarioResult scenarioResult;
    private ExecutionStatus executionStatus;
    private Instant createdDate;
}
