package com.pjhaynes.productswebservice.utils;

import java.text.DecimalFormat;
import java.util.HashMap;

public class Constants {

    public final static String JL_API_URL = "https://jl-nonprod-syst.apigee.net/v1/categories/600001506/products?key=2ALHCAAs6ikGRBoy6eTHA58RaG097Fma";
    public final static String BASE_URL_TO_OVERRIDE = "http://example.com";
    public final static int NO_PRICE = 0;
    public final static int TEN_POUNDS_IN_PENCE = 1000;
    public final static DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.00");
    public final static String SHOW_WAS_NOW = "ShowWasNow";
    public final static String SHOW_WAS_THEN_NOW = "ShowWasThenNow";
    public final static String SHOW_PERC_DISCOUNT = "ShowPercDscount";
    public final static String TEST_PARAM = "testParamString";

    public final static HashMap<String, String> BASIC_COLOR_RGB_TABLE = new HashMap<String, String>() {{
        put("Black", "000000");
        put("White", "FFFFFF");
        put("Red", "FF0000");
        put("Lime", "00FF00");
        put("Blue", "0000FF");
        put("Yellow", "FFFF00");
        put("Cyan", "00FFFF");
        put("Aqua", "00FFFF");
        put("Magenta", "FF00FF");
        put("Fuschia", "FF00FF");
        put("Silver", "C0C0C0");
        put("Grey", "808080");
        put("Maroon", "800000");
        put("Olive", "808000");
        put("Green", "008000");
        put("Purple", "800080");
        put("Teal", "008080");
        put("Navy", "000080");
        put("Orange", "FFA500");
        put("Pink",	"FFC0CB");
        put(null, null);
    }
    };
}
