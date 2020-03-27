package com.habeebcycle.springcacheredis.service;

import com.habeebcycle.springcacheredis.model.Product;
import com.habeebcycle.springcacheredis.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> findAllProducts(){
        return productRepository.findAll();
    }

    public List<Product> findProductBetweenPrice(double min, double max){
        return productRepository.getProductsByPriceBetween(min, max);
    }

    public Optional<Product> findProduct(String id){
        return productRepository.findById(id);
    }

    public Product saveProduct(Product product){
        return productRepository.save(product);
    }

    public List<Product> saveProducts(List<Product> products){
        return productRepository.saveAll(products);
    }

    public void deleteProduct(String productId){
        findProduct(productId).ifPresent(productRepository::delete);
    }

    public void deleteProducts(List<Product> products){
        productRepository.deleteAll(products);
    }

    public void deleteAllProducts(){
        productRepository.deleteAll();
    }
}
