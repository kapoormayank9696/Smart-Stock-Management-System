package com.smartstock.controller;

import com.smartstock.exception.BadRequestException;
import com.smartstock.exception.ResourceNotFoundException;
import com.smartstock.model.Supplier;
import com.smartstock.repository.ProductRepository;
import com.smartstock.repository.SupplierRepository;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/suppliers")
public class SupplierController {
    private final SupplierRepository suppliers;
    private final ProductRepository products;

    public SupplierController(SupplierRepository suppliers, ProductRepository products) {
        this.suppliers = suppliers;
        this.products = products;
    }

    @GetMapping
    public List<Supplier> list() {
        return suppliers.findAll(Sort.by("name"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Supplier create(@Valid @RequestBody Supplier s) {
        s.setId(null);
        return suppliers.save(s);
    }

    @PutMapping("/{id}")
    public Supplier update(@PathVariable Long id, @Valid @RequestBody Supplier in) {
        Supplier s = suppliers.findById(id).orElseThrow(() -> new ResourceNotFoundException("Supplier not found"));
        s.setName(in.getName());
        s.setEmail(in.getEmail());
        s.setPhone(in.getPhone());
        s.setAddress(in.getAddress());
        return suppliers.save(s);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        if (!suppliers.existsById(id)) throw new ResourceNotFoundException("Supplier not found");
        if (products.existsBySupplierId(id)) throw new BadRequestException("This supplier has products. Move or delete them first.");
        suppliers.deleteById(id);
    }
}
