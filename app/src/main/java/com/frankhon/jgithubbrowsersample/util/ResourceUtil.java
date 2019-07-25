package com.frankhon.jgithubbrowsersample.util;

import com.frankhon.jgithubbrowsersample.vo.Resource;

/**
 * Created by Frank_Hon on 7/25/2019.
 * E-mail: v-shhong@microsoft.com
 */
public final class ResourceUtil {

    private ResourceUtil(){}

    public static <T> Resource<T> success(T data) {
        return new Resource<>(RequestStatus.SUCCESS, data, null);
    }

    public static <T> Resource<T> error(String message, T data) {
        return new Resource<>(RequestStatus.ERROR, data, message);
    }

    public static <T> Resource<T> loading(T data) {
        return new Resource<>(RequestStatus.LOADING, data, null);
    }

}
