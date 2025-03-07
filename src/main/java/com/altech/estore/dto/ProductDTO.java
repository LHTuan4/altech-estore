package com.altech.estore.dto;

import com.altech.estore.entities.ProductEntity;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductDTO {
    private Long id;
    private String name;
    private BigDecimal price;
    private int stock;

    public static ProductEntity ToEntity(ProductDTO dto) {
        ProductEntity productEntity = new ProductEntity();
        productEntity.setId(dto.getId());
        productEntity.setName(dto.getName());
        productEntity.setPrice(dto.getPrice());
        productEntity.setStock(dto.getStock());
        return productEntity;
    }

    public static ProductDTO FromEntity(ProductEntity productEntity) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(productEntity.getId());
        productDTO.setName(productEntity.getName());
        productDTO.setPrice(productEntity.getPrice());
        productDTO.setStock(productEntity.getStock());
        return productDTO;
    }
}
