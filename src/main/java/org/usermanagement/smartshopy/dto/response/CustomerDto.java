package org.usermanagement.smartshopy.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.usermanagement.smartshopy.enums.CustomerTier;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerDto {

    private Long id;
    private String nom;
    private String email ;

    private Integer totalOrders;
    private BigDecimal totalSpent;

    private CustomerTier tier;

    private LocalDateTime firstOrderDate;
    private LocalDateTime lastOrderDate;

    private Userdto user;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
