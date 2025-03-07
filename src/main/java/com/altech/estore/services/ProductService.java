package com.altech.estore.services;

import com.altech.estore.entities.ProductEntity;
import com.altech.estore.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.locks.ReentrantLock;

@Service
@RequiredArgsConstructor
public class ProductService{
    private final ReentrantLock productLock = new ReentrantLock();

    private final ProductRepository repository;

    public ProductEntity addProduct(ProductEntity productEntity) {
        long now = System.currentTimeMillis();
        productEntity.setTimeCreated(now);

        return repository.save(productEntity);
    }
    public ProductEntity updateProduct(ProductEntity productEntity) {
        productLock.lock();
        try {
            ProductEntity curProduct = repository.findById(productEntity.getId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            long now = System.currentTimeMillis();
            curProduct.setName(productEntity.getName());
            curProduct.setPrice(productEntity.getPrice());
            curProduct.setStock(productEntity.getStock());
            curProduct.setTimeUpdated(now);

            return repository.save(curProduct);
        } finally {
            productLock.unlock();
        }
    }
}
