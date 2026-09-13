package com.example.springmongodb.product;

import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class ProductService {

    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public List<ProductResponse> findAll(String name) {
        List<Product> products = StringUtils.hasText(name)
                ? repository.findByNameContainingIgnoreCase(name)
                : repository.findAll();
        return products.stream().map(this::toResponse).toList();
    }

    public List<ProductResponse> findAll() {
        return repository.findAll().stream().map(this::toResponse).toList();
    }

    public ProductResponse findById(String id) {
        return repository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    public ProductResponse create(ProductRequest request) {
        Instant now = Instant.now();
        Product product = new Product();
        product.setName(request.name().trim());
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setCreatedAt(now);
        product.setUpdatedAt(now);
        return toResponse(repository.save(product));
    }

    public ProductResponse update(String id, ProductRequest request) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        product.setName(request.name().trim());
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setUpdatedAt(Instant.now());
        return toResponse(repository.save(product));
    }

    public void delete(String id) {
        if (!repository.existsById(id)) {
            throw new ProductNotFoundException(id);
        }
        repository.deleteById(id);
    }

    private ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getCreatedAt(),
                product.getUpdatedAt());
    }
}