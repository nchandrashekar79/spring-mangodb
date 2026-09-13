package com.example.springmongodb.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository repository;

    @InjectMocks
    private ProductService service;

    @Test
    void createsProductWithTimestamps() {
        ProductRequest request = new ProductRequest(" Keyboard ", "Mechanical", new BigDecimal("99.99"));
        when(repository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ProductResponse response = service.create(request);

        assertThat(response.name()).isEqualTo("Keyboard");
        assertThat(response.createdAt()).isNotNull();
        assertThat(response.updatedAt()).isNotNull();
        verify(repository).save(any(Product.class));
    }

    @Test
    void throwsWhenProductDoesNotExist() {
        when(repository.findById("missing")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById("missing"))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessage("Product not found: missing");
    }
}