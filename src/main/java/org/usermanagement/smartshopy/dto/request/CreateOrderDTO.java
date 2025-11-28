package org.usermanagement.smartshopy.dto.request;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateOrderDTO {

    @NotEmpty(message = "La commande doit contenir au moins un produit")
    @Valid
    private List<OrderItemDTO> items;

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
