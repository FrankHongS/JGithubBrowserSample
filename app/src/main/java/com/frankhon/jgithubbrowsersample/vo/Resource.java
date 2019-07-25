package com.frankhon.jgithubbrowsersample.vo;

import com.frankhon.jgithubbrowsersample.util.RequestStatus;

/**
 * Created by Frank_Hon on 7/25/2019.
 * E-mail: v-shhong@microsoft.com
 */
public class Resource<T> {

    @RequestStatus.Status
    private int Status;

    private T data;

    private String message;

    public Resource(@RequestStatus.Status int status, T data, String message) {
        Status = status;
        this.data = data;
        this.message = message;
    }

    @RequestStatus.Status
    public int getStatus() {
        return Status;
    }

    public T getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }
}
