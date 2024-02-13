package com.microservice.user.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "material")
public class Material {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String description;
    private Double price;
    private Integer currentStock;
    private Integer stockMin;

    public Material() {}

    public Material(String name, String description, Double price, Integer currentStock, Integer stockMin) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.currentStock = currentStock;
        this.stockMin = stockMin;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Double getPrice() {
        return price;
    }
    public void setPrice(Double price) {
        this.price = price;
    }
    public Integer getCurrentStock() {
        return currentStock;
    }
    public void setCurrentStock(Integer currentStock) {
        this.currentStock = currentStock;
    }
    public Integer getStockMin() {
        return stockMin;
    }
    public void setStockMin(Integer stockMin) {
        this.stockMin = stockMin;
    }

    @Override
    public String toString() {
        return "Material [id=" + id + ", name=" + name + ", description=" + description + ", price=" + price
                + ", currentStock=" + currentStock + ", stockMin=" + stockMin + "]";
    }

}
