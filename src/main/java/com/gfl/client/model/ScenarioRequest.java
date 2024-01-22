package com.gfl.client.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class ScenarioRequest {

    @Null(message = "Username can't be set explicitly")
    private String username;

    @NotBlank(message = "Name must not be blank")
    @Size(min = 1, max = 50, message = "Name must be between {min} and {max} characters")
    private String name;

    @NotBlank(message = "Site must not be blank")
    @Size(min = 1, max = 255, message = "Site must be between {min} and {max} characters")
    private String site;

    @Valid
    @NotNull(message = "Steps are required")
    @Size(min = 1, message = "At least one step is required")
    private List<StepRequest> steps;

    public ScenarioRequest(String name, String site, List<StepRequest> steps) {
        this.name = name;
        this.site = site;
        this.steps = steps;
    }
}
