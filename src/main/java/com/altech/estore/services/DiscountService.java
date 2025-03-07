package com.altech.estore.services;

import com.altech.estore.dto.DiscountDTO;
import com.altech.estore.entities.*;
import com.altech.estore.repository.DiscountRepository;
import com.altech.estore.repository.ProductDiscountsRepository;
import com.altech.estore.repository.ProductRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DiscountService {
    private final ProductRepository productRepository;
    private final DiscountRepository discountRepository;
    private final ProductDiscountsRepository productDiscountsRepository;
    private final ObjectMapper objectMapper;

    public DiscountDTO addDiscount(DiscountDTO discountDTO) {
        DiscountEntity discountEntity = DiscountDTO.toDiscountEntity(discountDTO);
        long now = System.currentTimeMillis();
        discountEntity.setTimeCreated(now);

        return DiscountDTO.FromDiscountEntity(discountRepository.save(discountEntity));
    }

    public void assignDiscountToProduct(Long discountId, Long productId) {
        ProductEntity productEntity = productRepository
                .findById(productId)
                .orElseThrow( () -> new RuntimeException("no product found"));

        ProductDiscountsEntity productDiscountsEntity = productDiscountsRepository
                .findById(productId)
                .orElse(new ProductDiscountsEntity());
        if (productDiscountsEntity.getProductId() == null) {
            productDiscountsEntity.setProductId(productId);
        }
        productDiscountsEntity.addDiscount(discountId);
        productDiscountsRepository.save(productDiscountsEntity);
    }

    public void removeDiscountFromProduct(Long discountId, Long productId) {
        //
    }

    public List<Long> getDiscountByProduct(Long productId) {
        return productDiscountsRepository.findById(productId)
                .map(ProductDiscountsEntity::getDiscountIds)
                .orElse(null);
    }
}
