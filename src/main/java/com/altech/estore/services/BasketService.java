package com.altech.estore.services;

import com.altech.estore.entities.BasketEntity;
import com.altech.estore.entities.BasketItem;
import com.altech.estore.entities.ProductEntity;
import com.altech.estore.repository.BasketRepository;
import com.altech.estore.repository.DiscountRepository;
import com.altech.estore.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.concurrent.locks.ReentrantLock;

@Service
@RequiredArgsConstructor
public class BasketService {
    private final ReentrantLock basketLoc = new ReentrantLock();

    private final BasketRepository basketRepository;
    private final ProductRepository productRepository;
    private final DiscountRepository discountRepository;

    public BasketEntity add(BasketEntity basketEntity) {
        long now = System.currentTimeMillis();
        basketEntity.setTimeCreated(now);

        return basketRepository.save(basketEntity);
    }

    public BasketItem addToBasket(Long basketId, Long productId, int quantity) {
        basketLoc.lock();
        try {
            BasketEntity basketEntity = basketRepository.findById(basketId).orElseThrow(() -> new RuntimeException("basket not fount"));
            ProductEntity productEntity = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("product not found"));

            if (productEntity.getStock() < quantity) {
                throw new RuntimeException("product is out of stock");
            }

            productEntity.setStock(productEntity.getStock() - quantity);
            productRepository.save(productEntity);

            BasketItem basketItem = new BasketItem();
            basketItem.setProductId(productId);
            basketItem.setQuantity(quantity);
            basketItem.setUnitPrice(productEntity.getPrice());
            basketItem.setOriginalPrice(basketItem.getUnitPrice().multiply(BigDecimal.valueOf(basketItem.getQuantity())));

            basketEntity.addItem(basketItem);
            basketRepository.save(basketEntity);

            return basketItem;
        } finally {
            basketLoc.unlock();
        }
    }

    public void removeFromBasket(Long basketId, Long productId) {
        basketLoc.lock();
        try {
            BasketEntity basketEntity = basketRepository.findById(basketId).orElseThrow(() -> new RuntimeException("basket not fount"));

            BasketItem removeItem = basketEntity.getItems().stream().filter(item -> item.getProductId().equals(productId)).findFirst().orElse(null);
            if (removeItem == null){
                return;
            }

            ProductEntity productEntity = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("product not found"));
            productEntity.setStock(productEntity.getStock() + removeItem.getQuantity());
            productRepository.save(productEntity);

            basketEntity.removeItem(removeItem);
            basketRepository.save(basketEntity);
        } finally {
            basketLoc.unlock();
        }
    }
}
