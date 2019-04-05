package com.pjhaynes.productswebservice;

import com.pjhaynes.productswebservice.model.Product;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.*;

import static com.pjhaynes.productswebservice.utils.Constants.*;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
public class ReducedPriceProductServiceTest {

    @MockBean
    private ReducedPriceProductService service;

    @Before
    public void setUp() throws Exception {
        Product.ColorSwatch colorSwatch2 = new Product.ColorSwatch("Sky Blue", "Blue", "2222");
        Product.ColorSwatch colorSwatch3 = new Product.ColorSwatch("Slayer Green", "Green", "3333");


        List<Product.ColorSwatch> colorSwatches2 = Collections.singletonList(new Product.ColorSwatch("Jet Black", "Black", "1111"));
        List<Product.ColorSwatch> colorSwatches3 = new ArrayList<>(Arrays.asList(colorSwatch2, colorSwatch3));

        Product product1 = new Product();
        Product.Price price1 = new Product.Price(19999, 0, 0, 15999, "GBP");
        product1.setPrice(price1);
        product1.setProductId("11111111");
        product1.setTitle("Discounted Product, with non integer Was and Now prices");
        product1.setDiscount();
        product1.setNowPrice();
        product1.setPercentageDiscount();
        product1.setPriceLabel(SHOW_WAS_NOW);

        Product product2 = new Product();
        Product.Price price2 = new Product.Price(19999, 18999, 0, 15999, "GBP");
        product2.setPrice(price2);
        product2.setProductId("22222222");
        product2.setColorSwatches(colorSwatches2);
        product2.setTitle("Discounted Product, with non integer Was, Then1 and Now prices");
        product2.setDiscount();
        product2.setNowPrice();
        product2.setPercentageDiscount();
        product2.setPriceLabel(SHOW_WAS_THEN_NOW);

        Product product3 = new Product();
        Product.Price price3 = new Product.Price(1999, 1899, 1799, 1599, "GBP");
        product3.setPrice(price3);
        product3.setProductId("33333333");
        product3.setColorSwatches(colorSwatches3);
        product3.setTitle("Discounted Product, with non integer Was, Then1, Then2 and Now prices");
        product3.setDiscount();
        product3.setNowPrice();
        product3.setPercentageDiscount();
        product3.setPriceLabel(SHOW_PERC_DISCOUNT);

        Product product4 = new Product();
        Product.Price price4 = new Product.Price(2000, 1900, 1800, 1700, "GBP");
        product4.setPrice(price4);
        product4.setProductId("44444444");
        product4.setTitle("Discounted Product, with integer Was, Then1, Then2 and Now prices");
        product4.setDiscount();
        product4.setNowPrice();
        product4.setPercentageDiscount();
        product4.setPriceLabel(SHOW_PERC_DISCOUNT);

        Product product5 = new Product();
        Product.Price price5 = new Product.Price(1000, 800, 700, 600, "GBP");
        product5.setPrice(price5);
        product5.setProductId("55555555");
        product5.setTitle("Discounted Product, with £10 Was, and sub £10 integer Then1, Then2 and Now prices");
        product5.setDiscount();
        product5.setNowPrice();
        product5.setPercentageDiscount();
        product5.setPriceLabel(SHOW_PERC_DISCOUNT);

        List<Product> discountedProducts = new ArrayList<>();
        discountedProducts.add(product1);
        discountedProducts.add(product2);
        discountedProducts.add(product3);
        discountedProducts.add(product4);
        discountedProducts.add(product5);

        Mockito.when(service.getReducedPriceProducts(TEST_PARAM))
                .thenReturn(discountedProducts);
    }

    @Test
    public void testProductId() throws IOException {
        Collection<Product> found = service.getReducedPriceProducts(TEST_PARAM);
        Product foundProduct1 = (Product) found.toArray()[0];
        Product foundProduct2 = (Product) found.toArray()[1];
        Product foundProduct3 = (Product) found.toArray()[2];

        assertEquals("11111111", foundProduct1.getProductId());
        assertEquals("22222222", foundProduct2.getProductId());
        assertEquals("33333333", foundProduct3.getProductId());

    }

    @Test
    public void testTitle() throws IOException {
        Collection<Product> found = service.getReducedPriceProducts(TEST_PARAM);
        Product foundProduct1 = (Product) found.toArray()[0];
        Product foundProduct2 = (Product) found.toArray()[1];
        Product foundProduct3 = (Product) found.toArray()[2];

        assertEquals("Discounted Product, with non integer Was and Now prices", foundProduct1.getTitle());
        assertEquals("Discounted Product, with non integer Was, Then1 and Now prices", foundProduct2.getTitle());
        assertEquals("Discounted Product, with non integer Was, Then1, Then2 and Now prices", foundProduct3.getTitle());
    }

    @Test
    public void testRbgColor() throws IOException {
        Collection<Product> found = service.getReducedPriceProducts(TEST_PARAM);
        Product foundProduct1 = (Product) found.toArray()[0];
        Product foundProduct2 = (Product) found.toArray()[1];
        Product foundProduct3 = (Product) found.toArray()[2];

        assertEquals(null, foundProduct1.getColorSwatches());
        assertEquals("000000", foundProduct2.getColorSwatches().get(0).getRgbColor());
        assertEquals("0000FF", foundProduct3.getColorSwatches().get(0).getRgbColor());
        assertEquals("008000", foundProduct3.getColorSwatches().get(1).getRgbColor());
    }

    @Test
    public void testNowPrice() throws IOException {
        Collection<Product> found = service.getReducedPriceProducts(TEST_PARAM);
        Product foundProduct1 = (Product) found.toArray()[0];
        Product foundProduct2 = (Product) found.toArray()[1];
        Product foundProduct3 = (Product) found.toArray()[2];

        assertEquals("£159.99", foundProduct1.getNowPrice());
        assertEquals("£159.99", foundProduct2.getNowPrice());
        assertEquals("£15.99", foundProduct3.getNowPrice());
    }

    @Test
    public void testDiscountAmount() throws IOException {
        Collection<Product> found = service.getReducedPriceProducts(TEST_PARAM);
        Product foundProduct1 = (Product) found.toArray()[0];
        Product foundProduct2 = (Product) found.toArray()[1];
        Product foundProduct3 = (Product) found.toArray()[2];

        assertEquals(4000, foundProduct1.getDiscount());
        assertEquals(4000, foundProduct2.getDiscount());
        assertEquals(400, foundProduct3.getDiscount());
    }

    @Test
    public void testDiscountPercentage() throws IOException {
        Collection<Product> found = service.getReducedPriceProducts(TEST_PARAM);
        Product foundProduct1 = (Product) found.toArray()[0];
        Product foundProduct2 = (Product) found.toArray()[1];
        Product foundProduct3 = (Product) found.toArray()[2];

        assertEquals(20, foundProduct1.getPercentageDiscount());
        assertEquals(20, foundProduct2.getPercentageDiscount());
        assertEquals(20, foundProduct3.getPercentageDiscount());
    }

    @Test
    public void testPriceLabel() throws IOException {
        Collection<Product> found = service.getReducedPriceProducts(TEST_PARAM);
        Product foundProduct1 = (Product) found.toArray()[0];
        Product foundProduct2 = (Product) found.toArray()[1];
        Product foundProduct3 = (Product) found.toArray()[2];

        assertEquals("Was £199.99, now £159.99", foundProduct1.getPriceLabel());
        assertEquals("Was £199.99, then £189.99, now £159.99", foundProduct2.getPriceLabel());
        assertEquals("20% off - now £15.99", foundProduct3.getPriceLabel());
    }

    @Test
    public void testPriceFormatting() throws IOException {
        Collection<Product> found = service.getReducedPriceProducts(TEST_PARAM);
        Product foundProduct4 = (Product) found.toArray()[3];
        Product foundProduct5 = (Product) found.toArray()[4];

        assertEquals("£20", foundProduct4.getFormattedPrice(foundProduct4.getPrice().getWas()));
        assertEquals("£19", foundProduct4.getFormattedPrice(foundProduct4.getPrice().getThen1()));
        assertEquals("£18", foundProduct4.getFormattedPrice(foundProduct4.getPrice().getThen2()));
        assertEquals("£17", foundProduct4.getFormattedPrice(foundProduct4.getPrice().getNow()));

        assertEquals("£10", foundProduct5.getFormattedPrice(foundProduct5.getPrice().getWas()));
        assertEquals("£8.00", foundProduct5.getFormattedPrice(foundProduct5.getPrice().getThen1()));
        assertEquals("£7.00", foundProduct5.getFormattedPrice(foundProduct5.getPrice().getThen2()));
        assertEquals("£6.00", foundProduct5.getFormattedPrice(foundProduct5.getPrice().getNow()));


    }
}