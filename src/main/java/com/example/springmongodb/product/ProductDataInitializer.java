package com.example.springmongodb.product;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class ProductDataInitializer implements ApplicationRunner {

    private final ProductRepository repository;
    private final ResourceLoader resourceLoader;
    private final String dataFile;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ProductDataInitializer(
            ProductRepository repository,
            ResourceLoader resourceLoader,
            @Value("${app.product-data-file:classpath:products.json}") String dataFile) {
        this.repository = repository;
        this.resourceLoader = resourceLoader;
        this.dataFile = dataFile;
    }

    @Override
    public void run(ApplicationArguments args) throws IOException {
        /*if (repository.count() > 0) {
            return;
        } */

        List<ProductSeed> seeds;
        try (InputStream input = resourceLoader.getResource(dataFile).getInputStream()) {
            seeds = Arrays.asList(objectMapper.readValue(input, ProductSeed[].class));
        }

        Instant now = Instant.now();
        List<Product> products = seeds.stream()
                .map(seed -> toProduct(seed, now))
                .toList();
        repository.saveAll(products);
    }

    private Product toProduct(ProductSeed seed, Instant now) {
        Product product = new Product();
        product.setName(seed.name());
        product.setDescription(seed.description());
        product.setPrice(seed.price());
        product.setCreatedAt(now);
        product.setUpdatedAt(now);
        return product;
    }

    private record ProductSeed(String name, String description, BigDecimal price) {
    }
}