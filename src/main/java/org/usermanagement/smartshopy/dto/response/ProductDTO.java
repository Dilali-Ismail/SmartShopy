package org.usermanagement.smartshopy.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stockQuantity;
    private Boolean active;
    private Boolean lowStock;  // Indique si stock < 10
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
