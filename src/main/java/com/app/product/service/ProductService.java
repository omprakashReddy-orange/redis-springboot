package com.app.product.service;

import com.app.product.dto.CreateProductDTO;
import com.app.product.dto.ProductResponseDTO;
import com.app.product.dto.UpdateProductDTO;
import com.app.product.entity.Product;
import com.app.product.exception.ProductAlreadyExistsException;
import com.app.product.exception.ProductNotFoundException;
import com.app.product.repository.ProductRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductResponseDTO createProduct(CreateProductDTO dto) {
        if (productRepository.findByName(dto.getName()).isPresent()) {
            throw new ProductAlreadyExistsException(
                    "Product with name '" + dto.getName() + "' already exists");
        }

        Product product = new Product();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setQuantity(dto.getQuantity());
        product.setCategory(dto.getCategory());

        Product savedProduct = productRepository.save(product);
        return mapToResponseDTO(savedProduct);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "products", key = "#id")
    public ProductResponseDTO getProductById(Long id) {
        simulateSlowDbCall();
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(
                        "Product with id " + id + " not found"));
        return mapToResponseDTO(product);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "products", key = "'all'")
    public List<ProductResponseDTO> getAllProducts() {
        simulateSlowDbCall();
        return productRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @CachePut(value = "products", key = "#id")
    public ProductResponseDTO updateProduct(Long id, UpdateProductDTO dto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(
                        "Product with id " + id + " not found"));

        if (dto.getName() != null) {
            if (!dto.getName().equals(product.getName()) &&
                    productRepository.findByName(dto.getName()).isPresent()) {
                throw new ProductAlreadyExistsException(
                        "Product with name '" + dto.getName() + "' already exists");
            }
            product.setName(dto.getName());
        }

        if (dto.getDescription() != null) {
            product.setDescription(dto.getDescription());
        }

        if (dto.getPrice() != null) {
            product.setPrice(dto.getPrice());
        }

        if (dto.getQuantity() != null) {
            product.setQuantity(dto.getQuantity());
        }

        if (dto.getCategory() != null) {
            product.setCategory(dto.getCategory());
        }

        Product updatedProduct = productRepository.save(product);
        return mapToResponseDTO(updatedProduct);
    }

    @CacheEvict(value = "products", key = "#id")
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException(
                    "Product with id " + id + " not found");
        }
        productRepository.deleteById(id);
    }

    /**
     * Simulates a slow database call (e.g., complex query, remote DB).
     * Used purely for demo purposes to show latency WITHOUT caching.
     * We will remove this once caching is introduced.
     */
    private void simulateSlowDbCall() {
        try {
            Thread.sleep(500); // 500ms artificial delay
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private ProductResponseDTO mapToResponseDTO(Product product) {
        return ProductResponseDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .category(product.getCategory())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }
}
