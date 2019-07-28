package com.frankhon.jgithubbrowsersample.util;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.frankhon.jgithubbrowsersample.api.ApiResponseUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import retrofit2.CallAdapter;
import retrofit2.CallAdapter.Factory;
import retrofit2.Retrofit;

/**
 * Created by Frank_Hon on 7/25/2019.
 * E-mail: v-shhong@microsoft.com
 */
public class LiveDataCallAdapterFactory extends Factory {

    @Nullable
    @Override
    public CallAdapter<?, ?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
        if (getRawType(returnType) != LiveData.class) {
            return null;
        }

        Type observableType = getParameterUpperBound(0, (ParameterizedType) returnType);
        Class rawObservableType = getRawType(observableType);
        if (rawObservableType != ApiResponseUtil.ApiResponse.class) {
            throw new IllegalArgumentException("type must be a resource");
        }

        if (!(observableType instanceof ParameterizedType)) {
            throw new IllegalArgumentException("resource must be a parameterized");
        }

        Type bodyType = getParameterUpperBound(0, (ParameterizedType) observableType);
        return new LiveDataAdapter<>(bodyType);
    }
}
