package com.gfl.client.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class ScenarioRequest {

    @NotBlank(message = "Username must not be blank")
    @Size(min = 1, max = 50, message = "Username must be between {min} and {max} characters")
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
}
