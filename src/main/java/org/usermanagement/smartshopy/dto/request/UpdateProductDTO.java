package org.usermanagement.smartshopy.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class UpdateProductDTO {

    @Size(max = 200, message = "Le nom ne doit pas dépasser 200 caractères")
    private String name;

    @Size(max = 1000, message = "La description ne doit pas dépasser 1000 caractères")
    private String description;

    @DecimalMin(value = "0.01", message = "Le prix doit être supérieur à 0")
    private BigDecimal price;

    @Min(value = 0, message = "La quantité ne peut pas être négative")
    private Integer stockQuantity;

    private Boolean active;
}
