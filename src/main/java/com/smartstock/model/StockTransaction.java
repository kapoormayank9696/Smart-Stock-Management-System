package com.smartstock.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class StockTransaction {
    public enum Type { IN, OUT }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Enumerated(EnumType.STRING)
    @Column(name = "movement_type", nullable = false)
    private Type type;

    @Column(nullable = false)
    private int quantity;

    private String note;

    private LocalDateTime createdAt = LocalDateTime.now();

    public StockTransaction() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
    public Type getType() { return type; }
    public void setType(Type type) { this.type = type; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
