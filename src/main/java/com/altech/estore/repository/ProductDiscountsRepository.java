package com.altech.estore.repository;

import com.altech.estore.entities.ProductDiscounts;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductDiscountsRepository extends JpaRepository<ProductDiscounts, Long> {
}
