package com.altech.estore.repository;

import com.altech.estore.entities.ProductDiscountsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductDiscountsRepository extends JpaRepository<ProductDiscountsEntity, Long> {
}
