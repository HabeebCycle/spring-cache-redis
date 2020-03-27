package com.habeebcycle.springcacheredis;

import com.habeebcycle.springcacheredis.model.Product;
import com.habeebcycle.springcacheredis.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDate;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static reactor.core.publisher.Mono.just;

@SpringBootTest(webEnvironment=RANDOM_PORT, properties = {"spring.data.mongodb.port: 0"})
class SpringCacheRedisApplicationTests {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ProductService productService;

    private LocalDate date1 = LocalDate.parse("2020-03-25");
    private LocalDate date2 = LocalDate.parse("2022-03-25");

    @BeforeEach
    void setUpDB(){
        productService.deleteAllProducts();
    }

    @Test
    void getProductById() {
        String productId = "xyz";
        postAndVerifyProduct(productId);

        WebTestClient.BodyContentSpec getAllProducts = getAndVerifyAllProducts();
        getAllProducts.jsonPath("$", hasSize(1));

        String savedProductId = productService.findAllProducts().get(0).getId();

        getAndVerifyProduct(savedProductId, OK).jsonPath("$.id").isEqualTo(savedProductId);
    }

    @Test
    void deleteProduct(){
        String productId1 = "xyz";
        String productId2 = "abc";
        postAndVerifyProduct(productId1);
        postAndVerifyProduct(productId2);

        WebTestClient.BodyContentSpec getAllProducts = getAndVerifyAllProducts();
        getAllProducts.jsonPath("$", hasSize(2));

        String savedProductId1 = getAllProducts.jsonPath("$.id[0]").toString();
        deleteAndVerifyProduct(savedProductId1, OK);

        getAllProducts = getAndVerifyAllProducts();
        getAllProducts.jsonPath("$", hasSize(1));

        String savedProductId2 = getAllProducts.jsonPath("$.id[0]").toString();
        deleteAndVerifyProduct(savedProductId2, OK);

        getAllProducts = getAndVerifyAllProducts();
        getAllProducts.jsonPath("$", hasSize(0));
    }

    @Test
    void deleteAllProducts(){
        String productId1 = "xyz";
        String productId2 = "abc";
        String productId3 = "ijk";
        postAndVerifyProduct(productId1);
        postAndVerifyProduct(productId2);
        postAndVerifyProduct(productId3);

        deleteAllAndVerifyProduct(OK);

        WebTestClient.BodyContentSpec getAllProducts = getAndVerifyAllProducts();
        getAllProducts.jsonPath("$").isEmpty();
    }

    private WebTestClient.BodyContentSpec getAndVerifyProduct(String productId, HttpStatus expectedStatus) {
        return webTestClient.get()
                .uri("/api/v1/products/" + productId)
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(expectedStatus)
                .expectHeader().contentType(APPLICATION_JSON)
                .expectBody();
    }

    private WebTestClient.BodyContentSpec getAndVerifyAllProducts() {
        return webTestClient.get()
                .uri("/api/v1/products")
                .accept(APPLICATION_JSON)
                .exchange()
                .expectHeader().contentType(APPLICATION_JSON)
                .expectBody();
    }

    private WebTestClient.BodyContentSpec postAndVerifyProduct(String productId) {
        Product product = new Product("Name " + productId, "Cat " + productId, "Desc " + productId, 1.0, 1.0, date1, date2);
        return webTestClient.post()
                .uri("/api/v1/products")
                .body(just(product), Product.class)
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.OK)
                .expectHeader().contentType(APPLICATION_JSON)
                .expectBody();
    }

    private WebTestClient.BodyContentSpec deleteAndVerifyProduct(String productId, HttpStatus expectedStatus) {
        return webTestClient.delete()
                .uri("/api/v1/products/" + productId)
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(expectedStatus)
                .expectBody();
    }

    private WebTestClient.BodyContentSpec deleteAllAndVerifyProduct(HttpStatus expectedStatus) {
        return webTestClient.delete()
                .uri("/api/v1/products")
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(expectedStatus)
                .expectBody();
    }

}
