package org.usermanagement.smartshopy.dto.response;

import lombok.*;
import org.usermanagement.smartshopy.dto.request.CreateOrderDTO;
import org.usermanagement.smartshopy.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrderDTO {

    private Long id;
    private CustomerDto customer;
    private List<CreateOrderDTO.OrderItemDTO> items;
    private BigDecimal subtotalHT;
    private BigDecimal loyaltyDiscount;
    private BigDecimal totalAfterDiscount;
    private BigDecimal tva;
    private BigDecimal totalTTC;
    private BigDecimal amountPaid;
    private BigDecimal remainingAmount;
    private OrderStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime confirmedAt;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class OrderItemDTO {
        private Long id;
        private ProductDTO product;
        private Integer quantity;
        private BigDecimal unitPrice;
        private BigDecimal subtotal;
    }

}
