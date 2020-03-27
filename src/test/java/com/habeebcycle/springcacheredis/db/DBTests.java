package com.habeebcycle.springcacheredis.db;

import com.habeebcycle.springcacheredis.model.Product;
import com.habeebcycle.springcacheredis.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.LongStream.rangeClosed;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.data.domain.Sort.Direction.ASC;

@DataMongoTest
class DBTests {

    @Autowired
    private ProductRepository productRepository;

    private Product savedProduct;

    private LocalDate date1 = LocalDate.parse("2020-03-25");
    private LocalDate date2 = LocalDate.parse("2022-03-25");

    @BeforeEach
    void setUpDB(){
        productRepository.deleteAll();

        Product product = new Product();
        product.setName("Panadol");
        product.setCategory("Paracetamol");
        product.setDescription("An analgesic drug for headache");
        product.setPrice(18.50);
        product.setWeight(4.50);
        product.setProduction(date1);
        product.setExpiry(date2);

        savedProduct = productRepository.save(product);

        assertEqualsProduct(product, savedProduct);
    }

    @Test
    void createProduct(){
        Product newProduct = new Product("Cold Flu", "Category", "Description",
                1.0, 1.0, date1, date2);
        productRepository.save(newProduct);
        Product foundProduct = productRepository.findById(newProduct.getId()).get();
        assertEqualsProduct(newProduct, foundProduct);

        assertEquals(2, productRepository.count());
    }

    @Test
    void updateProduct(){
        savedProduct.setName("Panadol Extra");
        savedProduct.setPrice(12.50);

        productRepository.save(savedProduct);

        Product foundProduct = productRepository.findById(savedProduct.getId()).get();
        assertEquals(1, productRepository.count());
        assertEquals(1, (long)foundProduct.getVersion());
        assertEquals("Panadol Extra", foundProduct.getName());
        assertEquals(12.50, foundProduct.getPrice());
    }

    @Test
    void deleteProduct(){
        productRepository.delete(savedProduct);
        assertFalse(productRepository.existsById(savedProduct.getId()));
    }

    @Test
    void deleteAllProduct(){
        Product newProduct = new Product("Paracetamol", "Category 2", "Description 2",
                2.0, 2.0, date1, date2);
        productRepository.save(newProduct);

        assertEquals(2, productRepository.count());

        productRepository.deleteAll();

        assertEquals(0, productRepository.count());
    }

    @Test
    void getByProductId(){
        Optional<Product> foundEntity = productRepository.findById(savedProduct.getId());

        assertTrue(foundEntity.isPresent());
        assertEqualsProduct(savedProduct, foundEntity.get());
    }

    @Test
    void paging() {

        productRepository.deleteAll();

        List<Product> newProducts = rangeClosed(1001, 1010)
                .mapToObj(i -> new Product("name " + i, "Cat "+ i, "Desc "+ i,
                        i*2.0, i*1.0, date1, date2))
                .collect(Collectors.toList());
        productRepository.saveAll(newProducts);

        Pageable nextPage = PageRequest.of(0, 4, ASC, "productId");
        nextPage = testNextPage(nextPage, "[name 1001, name 1002, name 1003, name 1004]", true);
        nextPage = testNextPage(nextPage, "[name 1005, name 1006, name 1007, name 1008]", true);
        nextPage = testNextPage(nextPage, "[name 1009, name 1010]", false);
    }

    private Pageable testNextPage(Pageable nextPage, String expectedProductNames, boolean expectsNextPage) {
        Page<Product> productPage = productRepository.findAll(nextPage);
        assertEquals(expectedProductNames, productPage.getContent().stream().map(Product::getName).collect(Collectors.toList()).toString());
        assertEquals(expectsNextPage, productPage.hasNext());
        return productPage.nextPageable();
    }


    private void assertEqualsProduct(Product expectedProduct, Product actualProduct) {
        assertEquals(expectedProduct.getId(),               actualProduct.getId());
        assertEquals(expectedProduct.getName(),          actualProduct.getName());
        assertEquals(expectedProduct.getCategory(),        actualProduct.getCategory());
        assertEquals(expectedProduct.getPrice(),           actualProduct.getPrice());
        assertEquals(expectedProduct.getWeight(),           actualProduct.getWeight());
        assertEquals(expectedProduct.getProduction(),       actualProduct.getProduction());
        assertEquals(expectedProduct.getExpiry(),           actualProduct.getExpiry());
    }
}
