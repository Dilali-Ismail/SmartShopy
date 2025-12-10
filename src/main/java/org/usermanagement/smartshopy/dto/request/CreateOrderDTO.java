package org.usermanagement.smartshopy.dto.request;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateOrderDTO {
    @NotNull(message = "L'ID du client est obligatoire")
    private Long customerId;

    @NotEmpty(message = "La commande doit contenir au moins un produit")
    @Valid
    private List<OrderItemDTO> items;

    @Size(max = 50, message = "Le code promo ne doit pas dépasser 50 caractères")
    private String promoCode;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class OrderItemDTO {

        @NotNull(message = "L'ID du produit est obligatoire")
        private Long productId;

        @NotNull(message = "La quantité est obligatoire")
        private Integer quantity;
    }

}
