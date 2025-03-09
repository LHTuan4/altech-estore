package com.altech.estore;

import com.altech.estore.dto.BasketDTO;
import com.altech.estore.entities.BasketEntity;
import com.altech.estore.entities.ProductEntity;
import com.altech.estore.repository.ProductRepository;
import com.altech.estore.services.BasketService;
import com.altech.estore.services.DiscountService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Optional;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class BasketServiceTest {
    @Mock
    private ProductRepository productRepository;
    @Mock private DiscountService discountService;
    @InjectMocks
    private BasketService basketService;

    @Test
    public void testAddToBasket() {
        ProductEntity product = new ProductEntity(1L, "Phone", new BigDecimal("100000"), 5, 0 ,0);
        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        basketService.addToBasket(1L, 2L, 1);

        BasketDTO basket = basketService.get(1L);
        Assertions.assertEquals(1, basket.getItems().size());
        Assertions.assertEquals(new BigDecimal("100000"), basket.getPriceTotal());
    }

    @Test
    public void testRemoveFromBasket() {
        ProductEntity product = new ProductEntity(2L, "Tablet", new BigDecimal("500"), 5, 0 ,0);
        Mockito.when(productRepository.findById(2L)).thenReturn(Optional.of(product));

        basketService.addToBasket(1L, 1L, 1);
        basketService.removeItemFromBasket(2L, 1L);

        BasketDTO basket = basketService.get(1L);
        Assertions.assertEquals(1, basket.getItems().size());
    }
}
