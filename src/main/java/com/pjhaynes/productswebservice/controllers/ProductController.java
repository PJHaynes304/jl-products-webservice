package com.pjhaynes.productswebservice.controllers;

import com.pjhaynes.productswebservice.ReducedPriceProductService;
import com.pjhaynes.productswebservice.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.Collection;

import static com.pjhaynes.productswebservice.utils.Constants.SHOW_WAS_NOW;

@Controller
public class ProductController {

    private final ReducedPriceProductService reducedPriceProductService;

    @Autowired
    public ProductController(ReducedPriceProductService reducedPriceProductService) {
        this.reducedPriceProductService = reducedPriceProductService;
    }

    @RequestMapping(value = "/ShowDiscountedProducts", method = RequestMethod.GET)
    public @ResponseBody Collection<Product> showDiscountedProducts (@RequestParam(value = "labelType", required = false) String labelType) throws IOException {

        if (labelType != null) {
            return reducedPriceProductService.getReducedPriceProducts(labelType);
        } else {
            return reducedPriceProductService.getReducedPriceProducts(SHOW_WAS_NOW);
        }
    }
}

