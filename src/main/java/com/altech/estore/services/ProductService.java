package com.altech.estore.services;

import com.altech.estore.dto.ProductDTO;
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

    public ProductDTO addProduct(ProductDTO productDTO) {
        ProductEntity productEntity = ProductDTO.ToEntity(productDTO);
        long now = System.currentTimeMillis();
        productEntity.setTimeCreated(now);

        return ProductDTO.FromEntity(repository.save(productEntity));
    }
    public ProductDTO updateProduct(ProductDTO productDTO) {
        productLock.lock();
        try {
            ProductEntity curProduct = repository.findById(productDTO.getId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            long now = System.currentTimeMillis();
            curProduct.setName(productDTO.getName());
            curProduct.setPrice(productDTO.getPrice());
            curProduct.setStock(productDTO.getStock());
            curProduct.setTimeUpdated(now);

            return ProductDTO.FromEntity(repository.save(curProduct));
        } finally {
            productLock.unlock();
        }
    }

    public void deleteProduct(Long id) {
        productLock.lock();
        try {
            ProductEntity product = repository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            repository.delete(product);
        } finally {
            productLock.unlock();
        }
    }
}
