package com.vitaliy.productservice.service;

import com.vitaliy.productservice.repository.ProductRepository;
import com.vitaliy.productservice.domain.Product;
import com.vitaliy.productservice.dto.ProductRequest;
import com.vitaliy.productservice.dto.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;

    public ProductResponse create(ProductRequest request) {
        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .category(request.getCategory())
                .active(true)
                .build();
        Product saved = repository.save(product);
        return toResponse(saved);
    }

    public List<ProductResponse> getAll() {
        return repository.findByActiveTrue().stream()
                .map(this::toResponse)
                .toList();
    }

    public ProductResponse getById(String id) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return toResponse(product);
    }

    public ProductResponse update(String id, ProductRequest request) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setCategory(request.getCategory());
        return toResponse(repository.save(product));
    }

    public void delete(String id) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        product.setActive(false);
        repository.save(product);
    }

    private ProductResponse toResponse(Product p) {
        return ProductResponse.builder()
                .id(p.getId())
                .name(p.getName())
                .description(p.getDescription())
                .price(p.getPrice())
                .category(p.getCategory())
                .active(p.isActive())
                .build();
    }


}
