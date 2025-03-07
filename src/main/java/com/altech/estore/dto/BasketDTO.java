package com.altech.estore.dto;

import com.altech.estore.entities.BasketEntity;
import com.altech.estore.entities.BasketItem;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class BasketDTO {
    private Long id;
    private List<BasketItem> items;
    private BigDecimal originalPriceTotal = BigDecimal.ZERO;
    private BigDecimal discountPriceTotal = BigDecimal.ZERO;
    private BigDecimal priceTotal = BigDecimal.ZERO;

    public static BasketEntity ToBasketEntity(BasketDTO basketDTO) {
        BasketEntity basketEntity = new BasketEntity();
        basketEntity.setId(basketDTO.getId());
        basketEntity.setItems(basketDTO.getItems());
        basketEntity.setOriginalPriceTotal(basketDTO.getOriginalPriceTotal());
        basketEntity.setDiscountPriceTotal(basketDTO.getDiscountPriceTotal());
        basketEntity.setPriceTotal(basketDTO.getPriceTotal());
        return basketEntity;
    }

    public static BasketDTO FromBasketEntity(BasketEntity basketEntity) {
        BasketDTO basketDTO = new BasketDTO();
        basketDTO.setId(basketEntity.getId());
        basketDTO.setItems(basketEntity.getItems());
        basketDTO.setOriginalPriceTotal(basketEntity.getOriginalPriceTotal());
        basketDTO.setDiscountPriceTotal(basketEntity.getDiscountPriceTotal());
        basketDTO.setPriceTotal(basketEntity.getPriceTotal());
        return basketDTO;
    }
}
