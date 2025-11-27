package org.usermanagement.smartshopy.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType. IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private Integer stockQuantity = 0;

    @Column(nullable = false)
    private Boolean active = true;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public boolean isAvailable(int requestedQuantity) {
        return this.active && this.stockQuantity >= requestedQuantity;
    }

    public void decrementStock(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("La quantité doit être positive");
        }

        if (this.stockQuantity < quantity) {
            throw new IllegalArgumentException(
                    String.format("Stock insuffisant pour le produit '%s'. Disponible: %d, Demandé: %d",
                            this.name, this.stockQuantity, quantity)
            );
        }

        this.stockQuantity -= quantity;
    }

    public void incrementStock(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("La quantité doit être positive");
        }

        this. stockQuantity += quantity;
    }

    public boolean isLowStock() {
        return this.stockQuantity < 10;
    }

}
