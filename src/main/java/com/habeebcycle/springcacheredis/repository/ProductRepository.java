package com.habeebcycle.springcacheredis.repository;

import com.habeebcycle.springcacheredis.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {
    @Query("{'price' : {$gte : ?0, $lte : ?1}}")
    List<Product> getProductsByPriceBetween(double minPrice, double maxPrice);
}
