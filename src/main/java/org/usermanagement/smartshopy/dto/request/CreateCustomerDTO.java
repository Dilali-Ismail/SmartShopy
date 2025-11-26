package org.usermanagement.smartshopy.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateCustomerDTO {
    @NotBlank(message = "Le nom est obligatoire")
    private String nom ;
    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "Email invalide")
    private String email ;
    @NotBlank(message = "Le username est obligatoire")
    private String username;
    @NotBlank(message = "Le pasword est obligatoire")
    private String password ;

}
