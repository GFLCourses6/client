package com.gfl.client.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class StepRequest {

    @NotBlank(message = "Action must not be blank")
    @Size(min = 1, max = 50, message = "Action must be between {min} and {max} characters")
    private String action;

    @NotBlank(message = "Value must not be blank")
    @Size(min = 1, max = 100, message = "Value must be between {min} and {max} characters")
    private String value;
}
