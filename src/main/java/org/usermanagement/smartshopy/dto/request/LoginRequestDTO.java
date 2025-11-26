package org.usermanagement.smartshopy.dto.request;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginRequestDTO {
    @NotBlank(message = "username est obligatoire")
    private String username;
    @NotBlank(message = "password est obligatoire")
    private String password;
}
