package com.pjhaynes.productswebservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAsync
public class ProductsWebserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductsWebserviceApplication.class, args);
    }
}
