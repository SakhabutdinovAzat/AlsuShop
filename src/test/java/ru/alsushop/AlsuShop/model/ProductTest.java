package ru.alsushop.AlsuShop.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.clothingstore.model.Product;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {
    Product product;

    @BeforeEach
    void createProduct(){
        product = new Product("Alex", 1000);
    }

    @Test
    void getName() {
        assertEquals("Alex", product.getName());
    }

    @Test
    void setName() {
        product.setName("Bob");
        assertEquals("Bob", product.getName());
    }
}