package org.usermanagement.smartshopy.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateCustomerDTO {
    @NotBlank(message = "pas etre vide")
    private String nom ;
    @Email(message = "Email invalide")
    private String email;


}
