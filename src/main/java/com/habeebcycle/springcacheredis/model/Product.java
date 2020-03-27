package com.habeebcycle.springcacheredis.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

@Document(collection = "product")
public class Product implements Serializable {

    @Id
    private String id;

    @Version
    private Integer version;

    @NotNull
    private String name;

    private String category;
    private String description;
    private double price;
    private double weight;
    private LocalDate production;
    private LocalDate expiry;

    public Product() {
    }

    public Product(@NotNull String name, String category, String description,
                   double price, double weight, LocalDate production, LocalDate expiry) {
        this.name = name;
        this.category = category;
        this.description = description;
        this.price = price;
        this.weight = weight;
        this.production = production;
        this.expiry = expiry;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
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

    public LocalDate getProduction() {
        return production;
    }

    public void setProduction(LocalDate production) {
        this.production = production;
    }

    public LocalDate getExpiry() {
        return expiry;
    }

    public void setExpiry(LocalDate expiry) {
        this.expiry = expiry;
    }
}
