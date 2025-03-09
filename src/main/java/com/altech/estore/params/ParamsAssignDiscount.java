package com.altech.estore.params;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ParamsAssignDiscount {
    private long discountId;
    private long productId;
}
