package com.frankhon.jgithubbrowsersample.util;

import androidx.lifecycle.LiveData;

/**
 * Created by Frank_Hon on 7/25/2019.
 * E-mail: v-shhong@microsoft.com
 */
public final class AbsentLiveData<T> extends LiveData<T> {

    private AbsentLiveData() {
        postValue(null);
    }

    public static <R> LiveData<R> create(){
        return new AbsentLiveData<>();
    }
}
