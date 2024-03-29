package com.gfl.client.model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProxyNetworkConfig {

    @NotBlank(message = "hostname can't be blank")
    @Size(min = 1, max = 50, message = "Name must be between {min} and {max} characters")
    private String hostname;

    @Min(value = 0, message = "port can't be less than 0")
    @Max(value = 65535, message = "port can't be more than 65535")
    @NotNull(message = "port can't be null")
    private Integer port;
}
