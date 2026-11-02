package com.smartstock.controller;

import com.smartstock.exception.BadRequestException;
import com.smartstock.exception.ResourceNotFoundException;
import com.smartstock.model.Product;
import com.smartstock.model.ProductRequest;
import com.smartstock.repository.CategoryRepository;
import com.smartstock.repository.ProductRepository;
import com.smartstock.repository.StockTransactionRepository;
import com.smartstock.repository.SupplierRepository;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductRepository products;
    private final CategoryRepository categories;
    private final SupplierRepository suppliers;
    private final StockTransactionRepository transactions;

    public ProductController(ProductRepository products, CategoryRepository categories,
                             SupplierRepository suppliers, StockTransactionRepository transactions) {
        this.products = products;
        this.categories = categories;
        this.suppliers = suppliers;
        this.transactions = transactions;
    }

    @GetMapping
    public List<Product> list(@RequestParam(required = false) String search) {
        if (search == null || search.isBlank()) return products.findAll(Sort.by("name"));
        String q = search.trim();
        return products.findByNameContainingIgnoreCaseOrSkuContainingIgnoreCase(q, q);
    }

    @GetMapping("/low-stock")
    public List<Product> lowStock() {
        return products.findLowStock();
    }

    @GetMapping("/{id}")
    public Product get(@PathVariable Long id) {
        return products.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Product create(@Valid @RequestBody ProductRequest req) {
        Product p = new Product();
        apply(p, req, null);
        return products.save(p);
    }

    @PutMapping("/{id}")
    public Product update(@PathVariable Long id, @Valid @RequestBody ProductRequest req) {
        Product p = get(id);
        apply(p, req, id);
        return products.save(p);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void delete(@PathVariable Long id) {
        if (!products.existsById(id)) throw new ResourceNotFoundException("Product not found");
        transactions.deleteByProductId(id);
        products.deleteById(id);
    }

    private void apply(Product p, ProductRequest r, Long currentId) {
        String sku = r.sku().trim();
        products.findBySku(sku).ifPresent(x -> {
            if (currentId == null || !x.getId().equals(currentId)) throw new BadRequestException("SKU already exists");
        });
        p.setSku(sku);
        p.setName(r.name().trim());
        p.setDescription(r.description());
        p.setPrice(r.price());
        p.setQuantity(r.quantity());
        p.setReorderLevel(r.reorderLevel());
        p.setCategory(r.categoryId() == null ? null : categories.findById(r.categoryId())
                .orElseThrow(() -> new BadRequestException("Category not found")));
        p.setSupplier(r.supplierId() == null ? null : suppliers.findById(r.supplierId())
                .orElseThrow(() -> new BadRequestException("Supplier not found")));
    }
}
