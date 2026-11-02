package com.smartstock.repository;

import com.smartstock.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findBySku(String sku);

    List<Product> findByNameContainingIgnoreCaseOrSkuContainingIgnoreCase(String name, String sku);

    boolean existsByCategoryId(Long categoryId);

    boolean existsBySupplierId(Long supplierId);

    @Query("select p from Product p where p.quantity <= p.reorderLevel order by p.quantity asc")
    List<Product> findLowStock();
}
