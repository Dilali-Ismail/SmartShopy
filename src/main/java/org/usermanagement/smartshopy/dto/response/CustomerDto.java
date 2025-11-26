package org.usermanagement.smartshopy.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerDto {

    private Long id;
    private String name;
    private String email ;

    private Integer TotalOrders;
    private BigDecimal TotalSpent;

    private LocalDateTime firstOrderDate;
    private LocalDateTime LastLocalDate;

    private Userdto userdto;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
