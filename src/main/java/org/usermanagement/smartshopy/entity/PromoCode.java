package org.usermanagement.smartshopy.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "promo_codes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PromoCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @Column(nullable = false)
    private Integer discountPercentage;

    @Column(nullable = false)
    private Integer maxUsages;

    @Column(nullable = false)
    @Builder.Default
    private Integer currentUsages = 0;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public Boolean isValid(){

        return currentUsages < maxUsages;
    }

    public void incrementUsage() {
        this.currentUsages++;
    }

    public boolean hasUsagesLeft() {
        return currentUsages < maxUsages;
    }

}
