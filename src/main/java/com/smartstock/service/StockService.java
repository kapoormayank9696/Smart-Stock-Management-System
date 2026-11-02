package com.smartstock.service;

import com.smartstock.exception.BadRequestException;
import com.smartstock.exception.ResourceNotFoundException;
import com.smartstock.model.Product;
import com.smartstock.model.StockRequest;
import com.smartstock.model.StockTransaction;
import com.smartstock.repository.ProductRepository;
import com.smartstock.repository.StockTransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StockService {
    private final ProductRepository products;
    private final StockTransactionRepository transactions;

    public StockService(ProductRepository products, StockTransactionRepository transactions) {
        this.products = products;
        this.transactions = transactions;
    }

    @Transactional
    public StockTransaction record(StockRequest req) {
        Product product = products.findById(req.productId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        int newQty = req.type() == StockTransaction.Type.IN
                ? product.getQuantity() + req.quantity()
                : product.getQuantity() - req.quantity();

        if (newQty < 0) {
            throw new BadRequestException("Not enough stock. Available: " + product.getQuantity());
        }

        product.setQuantity(newQty);
        products.save(product);

        StockTransaction t = new StockTransaction();
        t.setProduct(product);
        t.setType(req.type());
        t.setQuantity(req.quantity());
        t.setNote(req.note());
        return transactions.save(t);
    }
}
