package com.altech.estore.services;

import com.altech.estore.dto.BasketDTO;
import com.altech.estore.entities.BasketEntity;
import com.altech.estore.entities.BasketItem;
import com.altech.estore.entities.DiscountEntity;
import com.altech.estore.entities.ProductEntity;
import com.altech.estore.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
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
//    private final BasketEntityItemsRepository basketItemsRepository;
    private final DiscountService discountService;

    public BasketDTO get(Long basketId) {
        Optional<BasketEntity> basketEntity = basketRepository.findById(basketId);
        if (basketEntity.isPresent()) {
            BasketEntity basket = basketEntity.get();
//            List<BasketItem> basketItems = basketItemsRepository.findAllById(Collections.singletonList(basket.getId()));
//            basket.setItems(basketItems);
            return BasketDTO.FromBasketEntity(basket);
        }
        return null;
    }
    public BasketDTO add(BasketDTO basketDTO) {
        BasketEntity basketEntity = BasketDTO.ToBasketEntity(basketDTO);
        long now = System.currentTimeMillis();
        basketEntity.setTimeCreated(now);

        return BasketDTO.FromBasketEntity(basketRepository.save(basketEntity));
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

            // Update product stock
//            product.setStock(product.getStock() - quantity);
//            productRepository.save(product);

            // Find existing basket item or create new one
            Optional<BasketItem> existingItemOpt = basket.getItems().stream()
                    .filter(item -> item.getProductId().equals(productId))
                    .findFirst();

            long now = System.currentTimeMillis();
            if (existingItemOpt.isPresent()) {
                BasketItem existingItem = existingItemOpt.get();
                existingItem.setTimeCreated(now);
                existingItem.setQuantity(existingItem.getQuantity() + quantity);
                existingItem.setOriginalPrice(existingItem.getUnitPrice()
                        .multiply(BigDecimal.valueOf(existingItem.getQuantity())));
            } else {
                BasketItem newItem = BasketItem.FromProductEntity(product, quantity);
                basket.getItems().add(newItem);
                basket.setTimeCreated(now);
            }
            // Recalculate basket totals and apply discounts
            recalculateBasket(basket);
            return BasketDTO.FromBasketEntity(basketRepository.save(basket));
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
