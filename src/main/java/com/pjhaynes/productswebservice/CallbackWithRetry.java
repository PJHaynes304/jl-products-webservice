package com.pjhaynes.productswebservice;

import retrofit2.Call;
import retrofit2.Callback;

import javax.validation.constraints.NotNull;

public abstract class CallbackWithRetry<T> implements Callback<T> {
    private int retryCount = 0;

    private final String requestName;
    private final int retryAttempts;

    protected CallbackWithRetry(@NotNull String requestName, int retryAttempts) {
        this.requestName = requestName;
        this.retryAttempts = retryAttempts;
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        if (retryCount < retryAttempts) {
            System.out.println("Retrying " + requestName + "... (" + retryCount + " out of " + retryAttempts + ")");
            retry(call);
            retryCount += 1;
        } else {
            System.out.println("Failed request " + requestName + " after " + retryAttempts + " attempts");
        }
    }

    private void retry(Call<T> call) {
        call.clone().enqueue(this);
    }
}
