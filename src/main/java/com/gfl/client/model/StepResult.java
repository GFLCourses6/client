package com.gfl.client.model;

import lombok.*;

import java.time.Instant;

@Setter
@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class StepResult {

    private Long id;
    private String action;
    private String value;
    private String executionMessage;
    private ExecutionStatus executionStatus;
    private Instant createdDate;
}
