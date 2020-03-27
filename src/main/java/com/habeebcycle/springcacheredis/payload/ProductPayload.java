package com.habeebcycle.springcacheredis.payload;

import com.habeebcycle.springcacheredis.model.Product;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class ProductPayload {

    private String pId;


    private String name;

    private String category;
    private String description;
    private double price;
    private double weight;

    private String production;
    private String expiry;

    public ProductPayload() {
    }

    public ProductPayload(String name, String category, String description, double price, double weight, String production, String expiry) {
        this.name = name;
        this.category = category;
        this.description = description;
        this.price = price;
        this.weight = weight;
        this.production = production;
        this.expiry = expiry;
    }

    public String getPId() {
        return pId;
    }

    public void setPId(String pId) {
        this.pId = pId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getProduction() {
        return production;
    }

    public void setProduction(String production) {
        this.production = production;
    }

    public String getExpiry() {
        return expiry;
    }

    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }

    public Product convertToProduct(){
        LocalDate productionDate = LocalDate.parse(this.production);
        LocalDate expiryDate = LocalDate.parse(this.expiry);
        return new Product(name, category, description, price, weight, productionDate, expiryDate);
    }
}
