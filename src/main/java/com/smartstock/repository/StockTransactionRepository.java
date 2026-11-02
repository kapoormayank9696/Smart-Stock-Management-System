package com.smartstock.repository;

import com.smartstock.model.StockTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface StockTransactionRepository extends JpaRepository<StockTransaction, Long> {
    List<StockTransaction> findTop100ByOrderByCreatedAtDesc();

    List<StockTransaction> findTop8ByOrderByCreatedAtDesc();

    void deleteByProductId(Long productId);
}
