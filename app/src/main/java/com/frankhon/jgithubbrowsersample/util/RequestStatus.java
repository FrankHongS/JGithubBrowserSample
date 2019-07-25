package com.frankhon.jgithubbrowsersample.util;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Frank_Hon on 7/25/2019.
 * E-mail: v-shhong@microsoft.com
 */
public interface RequestStatus {

    int SUCCESS = 0;
    int ERROR = 1;
    int LOADING = 2;

    @IntDef({SUCCESS,ERROR,LOADING})
    @Retention(RetentionPolicy.SOURCE)
    @interface Status{}
}
