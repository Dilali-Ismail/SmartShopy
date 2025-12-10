package org.usermanagement.smartshopy.dto.response;

import lombok.*;
import org.usermanagement.smartshopy.enums.PaymentMethod;
import org.usermanagement.smartshopy.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentDTO {
    private Long id;
    private Long orderId;
    private BigDecimal amount;
    private PaymentMethod method;
    private String reference;
    private PaymentStatus status ;
    private String notes;
    private LocalDateTime createdAt;
}
