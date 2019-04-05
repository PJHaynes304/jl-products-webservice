package com.pjhaynes.productswebservice.controllers;

import com.pjhaynes.productswebservice.ReducedPriceProductService;
import com.pjhaynes.productswebservice.model.Product;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static com.pjhaynes.productswebservice.utils.Constants.*;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(ProductController.class)

public class ProductControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ReducedPriceProductService service;

    @Test
    public void showWasNowPriceLabelProduct() throws Exception {
        Product product = new Product();
        Product.Price price = new Product.Price(19999, 18999, 17999, 15999, "GBP");
        product.setPrice(price);
        product.setProductId("555666777");
        product.setTitle("Discounted Product, with non integer Was Then1 Then2 and Now prices");
        product.setDiscount();
        product.setNowPrice();
        product.setPercentageDiscount();
        product.setPriceLabel(SHOW_WAS_NOW);

        List<Product> discountedProducts = Collections.singletonList(product);

        when(service.getReducedPriceProducts(SHOW_WAS_NOW)).thenReturn(discountedProducts);

        mvc.perform(get("/ShowDiscountedProducts")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title").value(product.getTitle()))
                .andExpect(jsonPath("$[0].productId").value(product.getProductId()))
                .andExpect(jsonPath("$[0].nowPrice").value(product.getNowPrice()))
                .andExpect(jsonPath("$[0].priceLabel").value(product.getPriceLabel()));
    }

    @Test
    public void showWasThenNowPriceLabelProduct() throws Exception {
        Product product = new Product();
        Product.Price price = new Product.Price(19999, 18999, 17999, 15999, "GBP");
        product.setPrice(price);
        product.setProductId("555666777");
        product.setTitle("Discounted Product, with non integer Was Then1 Then2 and Now prices");
        product.setDiscount();
        product.setNowPrice();
        product.setPercentageDiscount();
        product.setPriceLabel(SHOW_WAS_THEN_NOW);

        List<Product> discountedProducts = Collections.singletonList(product);

        when(service.getReducedPriceProducts(SHOW_WAS_NOW)).thenReturn(discountedProducts);

        mvc.perform(get("/ShowDiscountedProducts")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title").value(product.getTitle()))
                .andExpect(jsonPath("$[0].productId").value(product.getProductId()))
                .andExpect(jsonPath("$[0].nowPrice").value(product.getNowPrice()))
                .andExpect(jsonPath("$[0].priceLabel").value(product.getPriceLabel()));
    }

    @Test
    public void showPercDscountPriceLabelProduct() throws Exception {
        Product product = new Product();
        Product.Price price = new Product.Price(19999, 18999, 17999, 15999, "GBP");
        product.setPrice(price);
        product.setProductId("555666777");
        product.setTitle("Discounted Product, with non integer Was Then1 Then2 and Now prices");
        product.setDiscount();
        product.setNowPrice();
        product.setPercentageDiscount();
        product.setPriceLabel(SHOW_PERC_DISCOUNT);

        List<Product> discountedProducts = Collections.singletonList(product);

        when(service.getReducedPriceProducts(SHOW_WAS_NOW)).thenReturn(discountedProducts);

        mvc.perform(get("/ShowDiscountedProducts")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title").value(product.getTitle()))
                .andExpect(jsonPath("$[0].productId").value(product.getProductId()))
                .andExpect(jsonPath("$[0].nowPrice").value(product.getNowPrice()))
                .andExpect(jsonPath("$[0].priceLabel").value(product.getPriceLabel()));
    }
}