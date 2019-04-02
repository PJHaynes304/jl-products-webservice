package com.pjhaynes.productswebservice;

import com.pjhaynes.productswebservice.api.JLService;
import com.pjhaynes.productswebservice.model.Product;
import com.pjhaynes.productswebservice.response.QueryResponse;
import com.pjhaynes.productswebservice.utils.NullOnEmtpyConverterFactory;
import okhttp3.OkHttpClient;
import org.springframework.stereotype.Component;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.pjhaynes.productswebservice.utils.Constants.*;

@Component
public class ReducedPriceProductService {

    private List<Product> allProducts;
    private List<Product> discountedProducts = new ArrayList<>();

    private OkHttpClient okHttpClient = new OkHttpClient
            .Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build();

    private Retrofit jlRetrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL_TO_OVERRIDE)
            .client(okHttpClient)
            .addConverterFactory(new NullOnEmtpyConverterFactory())
            .addConverterFactory(JacksonConverterFactory.create())
            .build();

    private JLService jlService = jlRetrofit.create(JLService.class);


    public Collection<Product> getReducedPriceProducts(String labelType) throws IOException {

        final Call<QueryResponse> call = jlService.products(JL_API_URL);

        Response<QueryResponse> response = call.execute();

        if (!response.isSuccessful()) {
            throw new IOException(response.errorBody() != null
                    ? response.errorBody().string() : "Unknown error");
        }
        QueryResponse productsResponse = response.body();
        allProducts = productsResponse.getProducts();

        allProducts.forEach(product -> {
            product.setNowPrice();
            product.setDiscount();
        });
        discountedProducts = allProducts.stream()
                .filter(product -> product.getPrice().getNow() < product.getPrice().getWas() && product.getPrice().getNow() != NO_PRICE)
                .sorted(Comparator.comparing(Product::getDiscount).reversed())
                .collect(Collectors.toList());
        discountedProducts.forEach(product -> {
            product.setPercentageDiscount();
            product.setPriceLabel(labelType);

        });
        return discountedProducts;
    }
}
