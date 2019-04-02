package com.pjhaynes.productswebservice;

import com.pjhaynes.productswebservice.api.JlService;
import com.pjhaynes.productswebservice.model.Product;
import com.pjhaynes.productswebservice.response.QueryResponse;
import com.pjhaynes.productswebservice.utils.NullOnEmtpyConverterFactory;
import okhttp3.OkHttpClient;
import org.springframework.stereotype.Component;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import javax.annotation.PostConstruct;
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

    private JlService jlSerice = jlRetrofit.create(JlService.class);

    @PostConstruct
    public void getReducedPriceProducts() {
        final Call<QueryResponse> call = jlSerice.products(JL_API_URL);

        call.enqueue(new CallbackWithRetry<QueryResponse>(JL_API_REQUEST_NAME, RETRY_ATTEMPTS) {
            @Override
            public void onResponse(Call<QueryResponse> call, Response<QueryResponse> response) {
                QueryResponse result = response.body();
                allProducts = result.getProducts();
            }
            @Override
            public void onFailure(Call<QueryResponse> call, Throwable t) {
                System.out.println(" Call Failed: " + t.getMessage());
            }
        });
    }



    public void filterAndSortProducts() {
        //First calculate discounts so products can be sorted
        allProducts.forEach(product -> {
            product.setNowPrice();
            product.setDiscount();
        });
        discountedProducts = allProducts.stream()
                .filter(product -> product.getPrice().getNow() < product.getPrice().getWas() && product.getPrice().getNow() != NO_PRICE)
                .sorted(Comparator.comparing(Product::getDiscount).reversed())
                .collect(Collectors.toList());
    }

    public void setProductPriceLabel(String labelType) {
        discountedProducts.forEach(product -> {
            product.setPercentageDiscount();
            product.setPriceLabel(labelType);
        });
    }

    public Collection<Product> getDiscountedProducts() {
        return discountedProducts;
    }
}
