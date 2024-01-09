package com.gfl.client.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProxyCredentials {

    @NotBlank(message = "Username must not be blank")
    @Size(min = 3, max = 50, message = "Username must be between {min} and {max} characters")
    private String username;

    @NotBlank(message = "Password must not be blank")
    @Size(min = 6, max = 100, message = "Password must be between {min} and {max} characters")
    private String password;
}
