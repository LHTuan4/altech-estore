package com.altech.estore.repository;

import com.altech.estore.entities.ProductDiscountsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductDiscountsRepository extends JpaRepository<ProductDiscountsEntity, Long> {
}
