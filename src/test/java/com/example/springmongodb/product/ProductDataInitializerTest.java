package com.example.springmongodb.product;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ResourceLoader;

@ExtendWith(MockitoExtension.class)
class ProductDataInitializerTest {

    @Mock
    private ProductRepository repository;

    @Mock
    private ResourceLoader resourceLoader;

    @Test
    void loadsProductsFromConfiguredJsonFileWhenCollectionIsEmpty() throws Exception {
        String json = "[{\"name\":\"Keyboard\",\"description\":\"Compact\",\"price\":99.99}]";
        when(repository.count()).thenReturn(0L);
        when(resourceLoader.getResource("file:products.json"))
                .thenReturn(new ByteArrayResource(json.getBytes(StandardCharsets.UTF_8)));

        ProductDataInitializer initializer = new ProductDataInitializer(
                repository, resourceLoader, "file:products.json");

        initializer.run(null);

        verify(repository).saveAll(org.mockito.ArgumentMatchers.argThat(products ->
                products.iterator().hasNext()
                        && products.iterator().next().getName().equals("Keyboard")
                        && products.iterator().next().getPrice().toString().equals("99.99")
                        && products.iterator().next().getCreatedAt() != null));
    }

    @Test
    void doesNotLoadDataWhenCollectionAlreadyHasProducts() throws Exception {
        when(repository.count()).thenReturn(1L);

        ProductDataInitializer initializer = new ProductDataInitializer(
                repository, resourceLoader, "classpath:products.json");

        initializer.run(null);

        verify(repository, never()).saveAll(anyList());
        verify(resourceLoader, never()).getResource("classpath:products.json");
    }
}