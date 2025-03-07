package com.altech.estore.services;

import com.altech.estore.dto.BasketDTO;
import com.altech.estore.entities.BasketEntity;
import com.altech.estore.entities.BasketItem;
import com.altech.estore.entities.DiscountEntity;
import com.altech.estore.entities.ProductEntity;
import com.altech.estore.repository.BasketRepository;
import com.altech.estore.repository.DiscountRepository;
import com.altech.estore.repository.ProductDiscountsRepository;
import com.altech.estore.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

@Service
@RequiredArgsConstructor
public class BasketService {
    private final ReentrantLock basketLock = new ReentrantLock();

    private final BasketRepository basketRepository;
    private final ProductRepository productRepository;
    private final DiscountRepository discountRepository;
    private final ProductDiscountsRepository productDiscountsRepository;
    private final DiscountService discountService;

    public BasketEntity add(BasketEntity basketEntity) {
        long now = System.currentTimeMillis();
        basketEntity.setTimeCreated(now);

        return basketRepository.save(basketEntity);
    }

    public BasketDTO addToBasket(Long basketId, Long productId, int quantity) {
        basketLock.lock();
        try {
            if (quantity <= 0) {
                throw new RuntimeException("Quantity must be greater than zero");
            }

            BasketEntity basket = basketRepository.findById(basketId)
                    .orElseThrow(() -> new RuntimeException("Basket not found"));
            ProductEntity product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));

            if (product.getStock() < quantity) {
                throw new RuntimeException("Not enough stock available");
            }

            // Update product stock (with optimistic locking handled by @Version)
            product.setStock(product.getStock() - quantity);
            productRepository.save(product);

            // Find existing basket item or create new one
            Optional<BasketItem> existingItemOpt = basket.getItems().stream()
                    .filter(item -> item.getProductId().equals(productId))
                    .findFirst();

            if (existingItemOpt.isPresent()) {
                BasketItem existingItem = existingItemOpt.get();
                existingItem.setQuantity(existingItem.getQuantity() + quantity);
                existingItem.setOriginalPrice(existingItem.getUnitPrice()
                        .multiply(BigDecimal.valueOf(existingItem.getQuantity())));
            } else {
                BasketItem newItem = BasketItem.FromProductEntity(product, quantity);
                basket.getItems().add(newItem);
            }
            // Recalculate basket totals and apply discounts
            recalculateBasket(basket);
            return BasketDTO.FromBasketEntity(basket);
        } finally {
            basketLock.unlock();
        }

    }
    public BasketDTO removeItemFromBasket(Long basketId, Long productId) {
        basketLock.lock();
        try {
            BasketEntity basket = basketRepository.findById(basketId)
                    .orElseThrow(() -> new RuntimeException("Basket not found"));
            ProductEntity product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));

            Optional<BasketItem> itemToRemoveOpt = basket.getItems().stream()
                    .filter(item -> item.getProductId().equals(productId))
                    .findFirst();

            if (itemToRemoveOpt.isPresent()) {
                BasketItem itemToRemove = itemToRemoveOpt.get();
                product.setStock(product.getStock() + itemToRemove.getQuantity());
                productRepository.save(product);

                // Remove item from basket
                basket.getItems().remove(itemToRemove);

                // Recalculate basket totals
                recalculateBasket(basket);
                return BasketDTO.FromBasketEntity(basketRepository.save(basket));
            }

            throw new RuntimeException("Item not found in basket");
        } finally {
            basketLock.unlock();
        }
    }


    private void recalculateBasket(BasketEntity basket) {
        BigDecimal originalTotal = BigDecimal.ZERO;
        BigDecimal finalTotal = BigDecimal.ZERO;

        // Apply discounts to each item in the basket
        for (BasketItem item : basket.getItems()) {
            ProductEntity product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            BigDecimal originalPrice = product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            item.setOriginalPrice(originalPrice);
            originalTotal = originalTotal.add(originalPrice);

            // Find the best discount for this product
            List<Long> discountIds = discountService.getDiscountByProduct(item.getProductId());
            List<DiscountEntity> discounts = null;
            if (!discountIds.isEmpty()) {
                discounts = discountRepository.findAllById(discountIds);
            }
            applyBestDiscount(item, product, discounts);

            finalTotal = finalTotal.add(item.getTotalPrice());
        }

        basket.setOriginalPriceTotal(originalTotal);
        basket.setPriceTotal(finalTotal);
        basket.setDiscountPriceTotal(originalTotal.subtract(finalTotal));
    }

    private void applyBestDiscount(BasketItem item, ProductEntity product, List<DiscountEntity> discounts) {
        if (discounts == null || discounts.isEmpty()) {
            return;
        }
        BigDecimal bestDiscountAmount = BigDecimal.ZERO;
        DiscountEntity bestDiscount = null;

        for (DiscountEntity discount : discounts) {
            // some default check

            //
            BigDecimal discountAmount = BasketItem.getBasketItemDiscountPrice(item, discount);
            if (discountAmount.compareTo(bestDiscountAmount) > 0) {
                bestDiscountAmount = discountAmount;
                bestDiscount = discount;
            }
        }

        // Apply the best discount found
        if (bestDiscount != null) {
            item.setDiscountId(bestDiscount.getId());
            item.setDiscountPrice(bestDiscountAmount);
            item.setTotalPrice(item.getOriginalPrice().subtract(bestDiscountAmount));
        }
    }
}
