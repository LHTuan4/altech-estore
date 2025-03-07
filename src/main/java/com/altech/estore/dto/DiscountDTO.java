package com.altech.estore.dto;

import com.altech.estore.entities.DiscountEntity;
import com.altech.estore.entities.TypeDef;
import lombok.Data;

@Data
public class DiscountDTO {
    private Long id;
    private String description;
    private TypeDef.DiscountType discountType;
    private String discountConfig;
    private long discountMax;
    private int discountPercent;

    public static DiscountEntity toDiscountEntity(DiscountDTO discountDTO) {
        DiscountEntity discountEntity = new DiscountEntity();
        discountEntity.setId(discountDTO.getId());
        discountEntity.setDescription(discountDTO.getDescription());
        discountEntity.setDiscountType(discountDTO.getDiscountType());
        discountEntity.setDiscountConfig(discountDTO.getDiscountConfig());
        discountEntity.setDiscountMax(discountDTO.getDiscountMax());
        discountEntity.setDiscountPercent(discountDTO.getDiscountPercent());
        return discountEntity;
    }

    public static DiscountDTO FromDiscountEntity(DiscountEntity discountEntity) {
        DiscountDTO discountDTO = new DiscountDTO();
        discountDTO.setId(discountEntity.getId());
        discountDTO.setDescription(discountEntity.getDescription());
        discountDTO.setDiscountType(discountEntity.getDiscountType());
        discountDTO.setDiscountConfig(discountEntity.getDiscountConfig());
        discountDTO.setDiscountMax(discountEntity.getDiscountMax());
        discountDTO.setDiscountPercent(discountEntity.getDiscountPercent());
        return discountDTO;
    }
}
