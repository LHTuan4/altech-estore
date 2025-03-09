package com.altech.estore.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiscountEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    @Enumerated(EnumType.STRING)
    private TypeDef.DiscountType discountType;

    private String discountConfig;
    private long discountMax;
    private int discountPercent;
    private long timeCreated;
    private long timeUpdated;
}
