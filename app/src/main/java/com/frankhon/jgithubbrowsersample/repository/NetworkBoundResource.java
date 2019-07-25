package com.frankhon.jgithubbrowsersample.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.frankhon.jgithubbrowsersample.AppExecutors;
import com.frankhon.jgithubbrowsersample.api.ApiResponse;
import com.frankhon.jgithubbrowsersample.util.ResourceUtil;
import com.frankhon.jgithubbrowsersample.vo.Resource;

/**
 * Created by Frank_Hon on 7/25/2019.
 * E-mail: v-shhong@microsoft.com
 * <p>
 * A generic class that can provide a resource backed by both the sqlite database and the network.
 */
public abstract class NetworkBoundResource<ResultType, RequestType> {

    private final AppExecutors appExecutors;
    private final MediatorLiveData<Resource<ResultType>> result = new MediatorLiveData<>();

    public NetworkBoundResource(AppExecutors appExecutors) {
        this.appExecutors = appExecutors;

        result.setValue(ResourceUtil.loading(null));
        LiveData<ResultType> dbSource = loadFromDb();
        result.addSource(dbSource, data -> {
            result.removeSource(dbSource);
            if (shouldFetch(data)) {
                fetchFromNetwork(dbSource);
            } else {
                result.addSource(dbSource, newData -> setValue(ResourceUtil.success(newData)));
            }
        });
    }

    private void setValue(Resource<ResultType> newValue) {
        if (result.getValue() != newValue) {
            result.setValue(newValue);
        }
    }

    private void fetchFromNetwork(LiveData<ResultType> dbSource) {
        LiveData<ApiResponse> apiResponse = createCall();
        // we re-attach dbSource as a new source, it will dispatch its latest value quickly
        result.addSource(dbSource, newData -> {
            setValue(ResourceUtil.loading(newData));
        });
        result.addSource(apiResponse, response -> {
            result.removeSource(apiResponse);
            result.removeSource(dbSource);

            if (response instanceof ApiResponse.ApiSuccessResponse) {
                appExecutors.getDiskIO().execute(() -> {
                    saveCallResult(processResponse((ApiResponse.ApiSuccessResponse<RequestType>) response));
                    appExecutors.getMainThread().execute(() ->
                            result.addSource(loadFromDb(),
                                    newData -> setValue(ResourceUtil.success(newData)))
                    );
                });
            } else if (response instanceof ApiResponse.ApiEmptyResponse) {
                appExecutors.getMainThread().execute(() -> {
                    // reload from disk whatever we had
                    result.addSource(loadFromDb(), newData -> setValue(ResourceUtil.success(newData)));
                });
            } else if (response instanceof ApiResponse.ApiErrorResponse) {
                onFetchFailed();
                result.addSource(dbSource, newData -> setValue(ResourceUtil.error(((ApiResponse.ApiErrorResponse) response).getErrorMessage(),
                        newData))
                );
            }
        });
    }

    protected LiveData<Resource<ResultType>> asLiveData(){
        return result;
    }

    protected void onFetchFailed() {
    }

    protected RequestType processResponse(ApiResponse.ApiSuccessResponse<RequestType> response) {
        return response.getBody();
    }

    protected abstract void saveCallResult(RequestType item);

    protected abstract boolean shouldFetch(ResultType data);

    protected abstract LiveData<ResultType> loadFromDb();

    protected abstract LiveData<ApiResponse> createCall();
}
