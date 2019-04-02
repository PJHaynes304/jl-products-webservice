package com.pjhaynes.productswebservice.model;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.Currency;
import java.util.List;

import static com.pjhaynes.productswebservice.utils.Constants.*;


@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"productId", "title", "colorSwatches", "nowPrice", "priceLabel"})
public class Product {

    @JsonProperty("productId")
    private String productId;
    @JsonProperty("title")
    private String title;
    @JsonProperty("colorSwatches")
    private List<ColorSwatch> colorSwatches;
    @JsonProperty(value = "price", access = JsonProperty.Access.WRITE_ONLY)
    @JsonDeserialize(using = CustomPriceDeserializer.class)
    private Price price;

    private int discount;

    private int percentageDiscount;

    private String nowPrice;

    private String priceLabel;

    public String getProductId() { return productId; }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getTitle() { return title; }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ColorSwatch> getColorSwatches() { return colorSwatches; }

    public void setColorSwatches(List<ColorSwatch> colorSwatches) {
        this.colorSwatches = colorSwatches;
    }

    public Price getPrice() { return price; }

    public void setPrice(Price price) {
        this.price = price;
    }

    public String getNowPrice() { return nowPrice; }

    public void setNowPrice() {
        this.nowPrice = getFormattedPrice(getPrice().getNow());
    }

    public String getPriceLabel() { return priceLabel; }

    public void setPriceLabel(String labelType) {
        String formattedPriceNow = getFormattedPrice(getPrice().getNow());
        String formattedPriceWas = getFormattedPrice(getPrice().getWas());
        String formattedPriceThen1 = getFormattedPrice(getPrice().getThen1());
        String formattedPriceThen2 = getFormattedPrice(getPrice().getThen2());
        StringBuilder priceLabel = new StringBuilder();

        switch (labelType) {
            case SHOW_WAS_NOW:
                //implement ShowWasNow price label
                priceLabel.append("Was ");
                priceLabel.append(formattedPriceWas);
                priceLabel.append(", now ");
                priceLabel.append(formattedPriceNow);
                break;
            case SHOW_WAS_THEN_NOW:
                //implement ShowWasThenNow price label
                priceLabel.append("Was ");
                priceLabel.append(formattedPriceWas);
                if (getPrice().getThen2() != 0) {
                    priceLabel.append(", then ");
                    priceLabel.append(formattedPriceThen2);
                } else if (getPrice().getThen1() != 0) {
                    priceLabel.append(", then ");
                    priceLabel.append(formattedPriceThen1);
                }
                priceLabel.append(", now ");
                priceLabel.append(formattedPriceNow);
                break;
            case SHOW_PERC_DISCOUNT:
                //implement ShowPercDscount price label
                priceLabel.append(getPercentageDiscount());
                priceLabel.append("% off - now ");
                priceLabel.append(formattedPriceNow);
                break;
        }
        this.priceLabel = priceLabel.toString();
    }

    public String getFormattedPrice(int price) {

        Currency currency = Currency.getInstance(getPrice().getCurrency());
        //Convert from pence to pounds
        double decimalPrice = (double) price/100;
        //Format price to 2 decimal places
        String decimalPriceString = DECIMAL_FORMAT.format(decimalPrice);
        StringBuilder nowPrice = new StringBuilder();
        nowPrice.append(currency.getSymbol());

        if (price < TEN_POUNDS_IN_PENCE) {
            //Show decimal value for all prices under £10
            nowPrice.append(decimalPriceString);
        } else if (price % 100 != 0) {
            //show decimal value for all non integer prices
            nowPrice.append(decimalPriceString);
        } else {
            //Integer price is simply divided by pence to convert to pounds.
            nowPrice.append(price/100);
        }
        return nowPrice.toString();
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonPropertyOrder({"color","rgbColor", "skuId"})
    public static class ColorSwatch {
        @JsonProperty("color")
        private String color;
        @JsonProperty(value = "basicColor", access = JsonProperty.Access.WRITE_ONLY)
        private String basicColor;
        @JsonProperty("skuId")
        private String skuId;

        public ColorSwatch() {}

        public ColorSwatch(String color, String basicColor, String skuId) {
            this.color = color;
            this.basicColor = basicColor;
            this.skuId = skuId;
        }

        public String getColor() { return color; }

        public String getRgbColor() {
            return BASIC_COLOR_RGB_TABLE.get(basicColor);
        }

        public String getBasicColor() { return basicColor; }

        public String getSkuId() { return skuId; }

        public void setBasicColor(String basicColor) {
            this.basicColor = basicColor;
        }
    }

    @JsonIgnore
    public int getDiscount() {
        return discount;
    }

    @JsonIgnore
    public int getPercentageDiscount() {
        return percentageDiscount;
    }
    public void setDiscount() {
        if (getPrice().getWas() == NO_PRICE || getPrice().getNow() == NO_PRICE) {
            this.discount = NO_PRICE;
        } else {
            this.discount = getPrice().getWas() - getPrice().getNow();
        }
    }

    public void setPercentageDiscount() {
        this.percentageDiscount = (int)((getDiscount() * 100.0f) / getPrice().getWas());
    }

    public static class Price {
        private int was;
        private int then1;
        private int then2;
        private int now;
        private String currency;

        @JsonIgnore
        public Price(int was, int then1, int then2, int now, String currency) {
            this.was = was;
            this.then1 = then1;
            this.then2 = then2;
            this.now = now;
            this.currency = currency;
        }


        public int getWas () {
            return was;
        }

        public int getThen1 () {
            return then1;
        }

        public int getThen2 () {
            return then2;
        }

        public int getNow () {
            return now;
        }

        public String getCurrency () {
            return currency;
        }

        public void setWas(int was) {
            this.was = was;
        }

        public void setThen1(int then1) {
            this.then1 = then1;
        }

        public void setThen2(int then2) {
            this.then2 = then2;
        }

        public void setNow(int now) {
            this.now = now;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }
    }

    public static class CustomPriceDeserializer extends StdDeserializer<Price> {
        public CustomPriceDeserializer() { super(Price.class);

        }
        @Override
        public Price deserialize(JsonParser jp, DeserializationContext ctxt)
                throws IOException, JsonProcessingException {


            JsonNode node = jp.getCodec().readTree(jp);
            final int now;
            final int was;
            final int then1;
            final int then2;
            if (node.get("now").asText().equals("")) {
                now = NO_PRICE;
            } else {
                now = (node.get("now").asInt() * 100);
            }
            if (node.get("was").asText().equals("")) {
                was = NO_PRICE;
            } else {
                was = (node.get("was").asInt() * 100);
            }
            if (node.get("then1").asText().equals("")) {
                then1 = NO_PRICE;
            } else {
                then1 = (node.get("then1").asInt() * 100);
            }
            if (node.get("then2").asText().equals("")) {
                then2 = NO_PRICE;
            } else {
                then2 = (node.get("then2").asInt() * 100);
            }
            final String currency = node.get("currency").asText();
            return new Price(was, then1, then2, now, currency);
        }
    }
}
