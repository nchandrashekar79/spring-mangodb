package com.example.springmongodb.product;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.Instant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
class ProductRepositoryIntegrationTest {

    @Container
    static final MongoDBContainer mongo = new MongoDBContainer("mongo:8.0");

    @Autowired
    private ProductRepository repository;

    @DynamicPropertySource
    static void configureMongo(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongo::getReplicaSetUrl);
    }

    @BeforeEach
    void cleanDatabase() {
        repository.deleteAll();
    }

    @Test
    void savesAndFindsProductByName() {
        Product product = new Product();
        product.setName("Monitor");
        product.setDescription("4K display");
        product.setPrice(new BigDecimal("399.00"));
        product.setCreatedAt(Instant.now());
        product.setUpdatedAt(Instant.now());

        repository.save(product);

        assertThat(repository.findByNameContainingIgnoreCase("mon"))
                .singleElement()
                .extracting(Product::getName)
                .isEqualTo("Monitor");
    }
}