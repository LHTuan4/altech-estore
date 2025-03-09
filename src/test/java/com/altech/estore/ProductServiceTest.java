package com.altech.estore;

import com.altech.estore.dto.ProductDTO;
import com.altech.estore.entities.ProductEntity;
import com.altech.estore.repository.ProductRepository;
import com.altech.estore.services.ProductService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import java.math.BigDecimal;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private ProductService productService;

    @Test
    public void testCreateProduct() {
        ProductEntity product = new ProductEntity(null, "Phone", new BigDecimal("10000"), 5, 0, 0);
        ProductDTO savedProduct = productService.addProduct(ProductDTO.FromEntity(product));

        Assertions.assertNotNull(savedProduct);
        Assertions.assertEquals("Laptop", savedProduct.getName());
        Assertions.assertEquals(new BigDecimal("1000"), savedProduct.getPrice());
    }

    @Test
    public void testDeleteProduct() {
        Long productId = 4L;
        Mockito.doNothing().when(productRepository).deleteById(productId);

        Assertions.assertDoesNotThrow(() -> productService.deleteProduct(productId));
    }
}

