package com.altech.estore.entities;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Embeddable
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

    public static BigDecimal getBasketItemDiscountPrice(BasketItem basketItem, DiscountEntity discount) {
        if (basketItem == null) {
            return BigDecimal.ZERO;
        }
        return calDiscountItem(basketItem, discount);
    }

    private static BigDecimal calDiscountItem(BasketItem basketItem, DiscountEntity discount) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode config = objectMapper.readTree(discount.getDiscountConfig());

            switch (discount.getDiscountType()) {
                case ADDTIONAL_ITEM:
                    int quantity = config.get("quantity").asInt();
                    if (basketItem.getQuantity() < quantity) {
                        return BigDecimal.ZERO;
                    }
                    double discountItems = Math.floor(basketItem.getQuantity() * 0.0 / quantity);
                    return BigDecimal.valueOf(discountItems *(basketItem.getUnitPrice().longValue() * (1 - discount.getDiscountPercent() / 100)));

                default:
                    return BigDecimal.ZERO;
            }
        } catch (Exception e) {
        }
        return BigDecimal.ZERO;
    }

    public static BasketItem FromProductEntity(ProductEntity productEntity, int quantity) {
        BasketItem basketItem = new BasketItem();
        basketItem.setProductId(productEntity.getId());
        basketItem.setQuantity(quantity);
        basketItem.setUnitPrice(productEntity.getPrice());
        basketItem.setOriginalPrice(productEntity.getPrice().multiply(basketItem.getUnitPrice()));
        basketItem.setDiscountPrice(BigDecimal.ZERO);
        basketItem.setTotalPrice(basketItem.getOriginalPrice());
        return basketItem;
    }
}
