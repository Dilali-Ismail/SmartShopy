package org.usermanagement.smartshopy.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatePromoCodeDTO {

    @NotBlank(message = "Le code est obligatoire")
    private String code;

    @NotNull(message = "Le pourcentage de remise est obligatoire")
    private Integer discountPercentage;

    @NotNull(message = "Le nombre maximum d'utilisations est obligatoire")
    private Integer maxUsages;

}
