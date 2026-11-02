package com.smartstock.controller;

import com.smartstock.model.Product;
import com.smartstock.repository.CategoryRepository;
import com.smartstock.repository.ProductRepository;
import com.smartstock.repository.StockTransactionRepository;
import com.smartstock.repository.SupplierRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
    private final ProductRepository products;
    private final CategoryRepository categories;
    private final SupplierRepository suppliers;
    private final StockTransactionRepository transactions;

    public DashboardController(ProductRepository products, CategoryRepository categories,
                               SupplierRepository suppliers, StockTransactionRepository transactions) {
        this.products = products;
        this.categories = categories;
        this.suppliers = suppliers;
        this.transactions = transactions;
    }

    @GetMapping
    public Map<String, Object> summary() {
        List<Product> all = products.findAll();
        List<Product> low = products.findLowStock();
        BigDecimal value = all.stream()
                .map(p -> p.getPrice().multiply(BigDecimal.valueOf(p.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        int units = all.stream().mapToInt(Product::getQuantity).sum();

        Map<String, Object> m = new LinkedHashMap<>();
        m.put("totalProducts", all.size());
        m.put("totalUnits", units);
        m.put("totalValue", value);
        m.put("lowStockCount", low.size());
        m.put("categories", categories.count());
        m.put("suppliers", suppliers.count());
        m.put("lowStock", low);
        m.put("recent", transactions.findTop8ByOrderByCreatedAtDesc());
        return m;
    }
}
