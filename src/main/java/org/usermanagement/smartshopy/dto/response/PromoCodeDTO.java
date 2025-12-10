package org.usermanagement.smartshopy.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PromoCodeDTO {

    private Long id;
    private String code;
    private Integer discountPercentage;
    private Integer maxUsages;
    private Integer currentUsages;
    private Integer remainingUsages;
    private Boolean isValid;
    private LocalDateTime createdAt;
}
