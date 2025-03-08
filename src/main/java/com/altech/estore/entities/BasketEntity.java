package com.altech.estore.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BasketEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<BasketItem> items;

    private BigDecimal originalPriceTotal = BigDecimal.ZERO;
    private BigDecimal discountPriceTotal = BigDecimal.ZERO;
    private BigDecimal priceTotal = BigDecimal.ZERO;

    private long timeCreated;
    private long timeUpdated;

    public void addItem(BasketItem item) {
        items.add(item);
        recalculateTotals();
    }

    public void removeItem(BasketItem item) {
        items.remove(item);
        recalculateTotals();
    }

    private void recalculateTotals() {
        originalPriceTotal = items.stream()
                .map(BasketItem::getOriginalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        discountPriceTotal = items.stream()
                .map(item -> item.getDiscountPrice() != null ? item.getDiscountPrice() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        priceTotal = items.stream()
                .map(BasketItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
