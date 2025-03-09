package com.altech.estore.params;

import com.altech.estore.entities.BasketItem;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ParamsAddItemBasket {
    private Long basketId;
    private BasketItem request;
}
