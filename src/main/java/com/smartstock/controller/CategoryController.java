package com.smartstock.controller;

import com.smartstock.exception.BadRequestException;
import com.smartstock.exception.ResourceNotFoundException;
import com.smartstock.model.Category;
import com.smartstock.repository.CategoryRepository;
import com.smartstock.repository.ProductRepository;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryRepository categories;
    private final ProductRepository products;

    public CategoryController(CategoryRepository categories, ProductRepository products) {
        this.categories = categories;
        this.products = products;
    }

    @GetMapping
    public List<Category> list() {
        return categories.findAll(Sort.by("name"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Category create(@Valid @RequestBody Category c) {
        categories.findByNameIgnoreCase(c.getName().trim()).ifPresent(x -> {
            throw new BadRequestException("Category already exists");
        });
        c.setId(null);
        c.setName(c.getName().trim());
        return categories.save(c);
    }

    @PutMapping("/{id}")
    public Category update(@PathVariable Long id, @Valid @RequestBody Category in) {
        Category c = categories.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        categories.findByNameIgnoreCase(in.getName().trim()).ifPresent(x -> {
            if (!x.getId().equals(id)) throw new BadRequestException("Category already exists");
        });
        c.setName(in.getName().trim());
        c.setDescription(in.getDescription());
        return categories.save(c);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        if (!categories.existsById(id)) throw new ResourceNotFoundException("Category not found");
        if (products.existsByCategoryId(id)) throw new BadRequestException("This category has products. Move or delete them first.");
        categories.deleteById(id);
    }
}
