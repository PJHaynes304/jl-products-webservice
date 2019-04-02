package com.pjhaynes.productswebservice.api;

import com.pjhaynes.productswebservice.response.QueryResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;


public interface JLService {

    @GET
    Call<QueryResponse> products(@Url String apiUrl);
}
