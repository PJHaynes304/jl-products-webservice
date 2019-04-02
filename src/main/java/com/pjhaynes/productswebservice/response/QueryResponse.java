package com.pjhaynes.productswebservice.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pjhaynes.productswebservice.model.Product;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)

public class QueryResponse {

    @JsonProperty("products")
    private List<Product> products;

    @JsonProperty("products")
    public List<Product> getProducts() { return products; }
}
