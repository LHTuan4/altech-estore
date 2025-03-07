package com.altech.estore.entities;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BasketItem {
    private Long productId;
    private Long discountId;
    private int quantity;

    private BigDecimal unitPrice;
    private BigDecimal originalPrice;
    private BigDecimal discountPrice;
    private BigDecimal totalPrice;

    private long timeCreated;
    private long timeUpdated;
}
