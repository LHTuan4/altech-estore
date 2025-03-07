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
public class ProductDiscounts {
    @Id
    private Long productId;

    @ElementCollection
    private List<Long> discountIds = new ArrayList<>();

    private int size = 0;
    private long version;
}
