package com.altech.estore.services;

import com.altech.estore.entities.DiscountEntity;
import com.altech.estore.entities.ProductDiscounts;
import com.altech.estore.repository.DiscountRepository;
import com.altech.estore.repository.ProductDiscountsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DiscountService {
    private final DiscountRepository discountRepository;
    private final ProductDiscountsRepository productDiscountsRepository;

    public DiscountEntity addDiscount(DiscountEntity discountEntity) {
        long now = System.currentTimeMillis();
        discountEntity.setTimeCreated(now);

        return discountRepository.save(discountEntity);
    }

    public Optional<DiscountEntity> get(Long id) {
        return discountRepository.findById(id);
    }

    public List<Long> getDiscountByProduct(Long productId) {
        return productDiscountsRepository.findById(productId)
                .map(ProductDiscounts::getDiscountIds)
                .orElse(null);
    }
}
