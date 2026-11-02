package com.smartstock.config;

import com.smartstock.model.*;
import com.smartstock.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/** Adds a few sample records on the very first run (only if the database is empty). */
@Component
public class DataSeeder implements CommandLineRunner {
    private final CategoryRepository categories;
    private final SupplierRepository suppliers;
    private final ProductRepository products;

    public DataSeeder(CategoryRepository categories, SupplierRepository suppliers, ProductRepository products) {
        this.categories = categories;
        this.suppliers = suppliers;
        this.products = products;
    }

    @Override
    public void run(String... args) {
        if (categories.count() > 0 || products.count() > 0) return;

        Category electronics = category("Electronics", "Gadgets and accessories");
        Category grocery = category("Grocery", "Packaged food and staples");
        Category stationery = category("Stationery", "Office and school supplies");

        Supplier s1 = supplier("Sharma Traders", "sharma@example.com", "9876500001", "Delhi");
        Supplier s2 = supplier("Gupta Wholesale", "gupta@example.com", "9876500002", "Noida");

        product("EL-1001", "USB-C Cable 1m", 199, 120, 30, electronics, s1);
        product("EL-1002", "Wireless Mouse", 599, 8, 15, electronics, s1);
        product("GR-2001", "Basmati Rice 5kg", 649, 45, 20, grocery, s2);
        product("GR-2002", "Sunflower Oil 1L", 165, 6, 12, grocery, s2);
        product("ST-3001", "A4 Notebook (200 pages)", 85, 200, 50, stationery, s2);
        product("ST-3002", "Gel Pen (Blue)", 10, 14, 40, stationery, s1);
    }

    private Category category(String name, String desc) {
        Category c = new Category();
        c.setName(name);
        c.setDescription(desc);
        return categories.save(c);
    }

    private Supplier supplier(String name, String email, String phone, String address) {
        Supplier s = new Supplier();
        s.setName(name);
        s.setEmail(email);
        s.setPhone(phone);
        s.setAddress(address);
        return suppliers.save(s);
    }

    private void product(String sku, String name, int price, int qty, int reorder, Category c, Supplier s) {
        Product p = new Product();
        p.setSku(sku);
        p.setName(name);
        p.setPrice(BigDecimal.valueOf(price));
        p.setQuantity(qty);
        p.setReorderLevel(reorder);
        p.setCategory(c);
        p.setSupplier(s);
        products.save(p);
    }
}
