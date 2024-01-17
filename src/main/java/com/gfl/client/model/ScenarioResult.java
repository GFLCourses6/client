package com.gfl.client.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ScenarioResult {

    private Long id;
    private String name;
    private String site;
    private String createdBy;
    private List<StepResult> stepResults;
}
