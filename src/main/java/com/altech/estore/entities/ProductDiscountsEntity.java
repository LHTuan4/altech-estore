package com.altech.estore.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDiscountsEntity {
    @Id
    private Long productId;

    @ElementCollection
    private List<Long> discountIds = new ArrayList<>();

    private int size = 0;
    private long version;

    public void addDiscount(Long discountId) {
        if (!discountIds.contains(discountId)) {
            discountIds.add(discountId);
            size = discountIds.size();
        }
    }

    public void removeDiscount(Long discountId) {
        discountIds.remove(discountId);
        size = discountIds.size();
    }
}
