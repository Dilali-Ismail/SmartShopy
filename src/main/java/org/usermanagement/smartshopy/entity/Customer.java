package org.usermanagement.smartshopy.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.usermanagement.smartshopy.enums.CustomerTier;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.DrbgParameters;
import java.time.LocalDateTime;

@Entity
@Table(name = "customers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType. IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nom;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CustomerTier tier = CustomerTier.BASIC;

    @Column(nullable = false)
    private Integer totalOrders = 0;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalSpent = BigDecimal. ZERO;

    @Column
    private LocalDateTime firstOrderDate;

    @Column
    private LocalDateTime lastOrderDate;

    @OneToOne
    @JoinColumn(name = "user_id" , unique = true)
    private User user;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

  public void updateTier(){
      this.tier = CustomerTier.CalculerTier(
              this.totalOrders,
              this.totalSpent.doubleValue()
      );
  }

  public boolean CanapplyDisount(BigDecimal orderSubtotal){
      if(this.tier.equals(CustomerTier.BASIC)){
          return false ;
      }

      double minorder = this.tier.getMinOrderForDiscount();

      return orderSubtotal.doubleValue() >= minorder;
  }

  public BigDecimal Calculediscount(BigDecimal orderSubtotal ){

     if(!CanapplyDisount(orderSubtotal)){
         return BigDecimal.ZERO;
     }

     BigDecimal discountPercentage = new BigDecimal(this.tier.getRemisePercentage());

     BigDecimal discount = orderSubtotal.multiply(discountPercentage).divide(new BigDecimal("100") , 2 , RoundingMode.HALF_UP);

     return discount;
  }

  public void UpdateState(BigDecimal orderTotal){
      this.totalOrders++;
      this.totalSpent = this.totalSpent.add(orderTotal);

      this.lastOrderDate = LocalDateTime.now();

      if(this.firstOrderDate == null){
          this.firstOrderDate = LocalDateTime.now();
      }
      updateTier();
  }

}
