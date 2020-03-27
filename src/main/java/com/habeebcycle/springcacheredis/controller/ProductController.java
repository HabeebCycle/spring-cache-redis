package com.habeebcycle.springcacheredis.controller;

import com.habeebcycle.springcacheredis.exception.ProductNotFoundException;
import com.habeebcycle.springcacheredis.model.Product;
import com.habeebcycle.springcacheredis.service.ProductService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products")
    @Cacheable(value = "product", sync = true)
    public List<Product> getAllProducts(){
        System.out.println("getAllProducts");
        return productService.findAllProducts();
    }

    @GetMapping("/products/{productId}")
    @Cacheable(value = "product", key = "#productId", sync = true)
    public Product getProduct(@PathVariable String productId) throws ProductNotFoundException {
        System.out.println("getProduct");
        return productService.findProduct(productId)
                .orElseThrow(() ->
                        new ProductNotFoundException("No product found with the following Id :: " + productId));
    }

    @GetMapping("/products/range")
    @Cacheable(value = "product", key = "{#minPrice, #maxPrice}", sync = true)
    public List<Product> getProductBetweenPrice(@RequestParam(value = "minPrice") double minPrice, @RequestParam(value = "maxPrice") double maxPrice){
        System.out.println("getProductBetweenPrice");
        return productService.findProductBetweenPrice(minPrice, maxPrice);
    }

    @PostMapping("/products")
    @CachePut(value = "product")
    public Product postProduct(@Valid @RequestBody Product product){
        System.out.println("postProduct");
        return productService.saveProduct(product);
    }

    @PutMapping("/products")
    @CachePut(value = "product", key = "#product.id")
    public Product updateProduct(@Valid @RequestBody Product product){
        System.out.println("updateProduct");
        return productService.saveProduct(product);
    }

    @DeleteMapping("/products/{productId}")
    @CacheEvict(value = "product", key = "#productId")
    public String deleteProduct(@PathVariable String productId){
        System.out.println("deleteProduct");
        productService.deleteProduct(productId);
        return "Product Deleted";
    }

    @DeleteMapping("/products")
    @CacheEvict(value = "product", allEntries = true)
    public String deleteAllProducts(){
        System.out.println("deleteAllProducts");
        productService.deleteAllProducts();
        return "All Products Deleted";
    }

    @GetMapping("/products/clearCache")
    @CacheEvict(value = "product", allEntries = true)
    public String clearCache(){
        return "Cache Cleared";
    }
}
