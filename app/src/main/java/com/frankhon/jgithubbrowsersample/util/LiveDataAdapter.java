package com.frankhon.jgithubbrowsersample.util;

import androidx.lifecycle.LiveData;

import com.frankhon.jgithubbrowsersample.api.ApiResponseUtil;

import java.lang.reflect.Type;
import java.util.concurrent.atomic.AtomicBoolean;

import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Frank_Hon on 7/25/2019.
 * E-mail: v-shhong@microsoft.com
 * <p>
 * A Retrofit adapter that converts the Call into a LiveData of ApiResponse.
 */
public class LiveDataAdapter<R> implements CallAdapter<R, LiveData<ApiResponseUtil.ApiResponse<R>>> {

    private Type responseType;

    LiveDataAdapter(Type responseType) {
        this.responseType = responseType;
    }

    @Override
    public Type responseType() {
        return responseType;
    }

    @Override
    public LiveData<ApiResponseUtil.ApiResponse<R>> adapt(final Call<R> call) {
        return new LiveData<ApiResponseUtil.ApiResponse<R>>() {

            private AtomicBoolean started = new AtomicBoolean(false);

            @Override
            protected void onActive() {
                super.onActive();
                if (started.compareAndSet(false, true)) {
                    call.enqueue(new Callback<R>() {
                        @Override
                        public void onResponse(Call<R> call, Response<R> response) {
                            postValue(ApiResponseUtil.create(response));
                        }

                        @Override
                        public void onFailure(Call<R> call, Throwable throwable) {
                            postValue(ApiResponseUtil.create(throwable));
                        }
                    });
                }
            }
        };
    }
}
