package com.gfl.client.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class StepRequest {

    @NotNull(message = "Action is required")
    @NotBlank(message = "Action must not be blank")
    @Size(min = 1, max = 50, message = "Action must be between {min} and {max} characters")
    private String action;

    @NotNull(message = "Value is required")
    @NotBlank(message = "Value must not be blank")
    @Size(min = 1, max = 100, message = "Value must be between {min} and {max} characters")
    private String value;
}
