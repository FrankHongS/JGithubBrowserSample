package com.frankhon.jgithubbrowsersample.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.frankhon.jgithubbrowsersample.AppExecutors;
import com.frankhon.jgithubbrowsersample.api.ApiResponseUtil;
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

    NetworkBoundResource(AppExecutors appExecutors) {
        this.appExecutors = appExecutors;

        Log.d("Hon", "NetworkBoundResource: init");
        result.setValue(ResourceUtil.loading(null));
        LiveData<ResultType> dbSource = loadFromDb();
        result.addSource(dbSource, data -> {
            Log.d("Hon", "loadFromDb changed: ");
            result.removeSource(dbSource);
            if (shouldFetch(data)) {
                Log.d("Hon", "shouldFetch: true");
                fetchFromNetwork(dbSource);
            } else {
                Log.d("Hon", "shouldFetch: false");
                result.addSource(dbSource, newData -> {
                    Log.d("Hon", "NetworkBoundResource: 5");
                    setValue(ResourceUtil.success(newData));
                });
            }
        });
    }

    private void setValue(Resource<ResultType> newValue) {
        Log.d("Hon", "setValue: "+newValue.getData());
        if (result.getValue() != newValue) {
            result.setValue(newValue);
        }
    }

    private void fetchFromNetwork(LiveData<ResultType> dbSource) {
        LiveData<ApiResponseUtil.ApiResponse<RequestType>> apiResponse = createCall();
        // we re-attach dbSource as a new source, it will dispatch its latest value quickly
        result.addSource(dbSource, newData -> {
            Log.d("Hon", "fetchFromNetwork: 1");
            setValue(ResourceUtil.loading(newData));
        });
        result.addSource(apiResponse, response -> {
            result.removeSource(apiResponse);
            result.removeSource(dbSource);

            if (response instanceof ApiResponseUtil.ApiSuccessResponse) {
                appExecutors.getDiskIO().execute(() -> {
                    saveCallResult(processResponse((ApiResponseUtil.ApiSuccessResponse<RequestType>) response));
                    appExecutors.getMainThread().execute(() ->
                            result.addSource(loadFromDb(),
                                    newData -> {
                                        Log.d("Hon", "fetchFromNetwork: 2");
                                        setValue(ResourceUtil.success(newData));
                                    })
                    );
                });
            } else if (response instanceof ApiResponseUtil.ApiEmptyResponse) {
                appExecutors.getMainThread().execute(() -> {
                    // reload from disk whatever we had
                    result.addSource(loadFromDb(), newData -> {
                        Log.d("Hon", "fetchFromNetwork: 3");
                        setValue(ResourceUtil.success(newData));
                    });
                });
            } else if (response instanceof ApiResponseUtil.ApiErrorResponse) {
                onFetchFailed();
                result.addSource(dbSource, newData -> {
                    Log.d("Hon", "fetchFromNetwork: 4");
                    setValue(ResourceUtil.error(((ApiResponseUtil.ApiErrorResponse) response).getErrorMessage(),
                            newData));
                        }
                );
            }
        });
    }

    protected LiveData<Resource<ResultType>> asLiveData() {
        return result;
    }

    protected void onFetchFailed() {
    }

    protected RequestType processResponse(ApiResponseUtil.ApiSuccessResponse<RequestType> response) {
        return response.getBody();
    }

    protected abstract void saveCallResult(RequestType item);

    protected abstract boolean shouldFetch(ResultType data);

    protected abstract LiveData<ResultType> loadFromDb();

    protected abstract LiveData<ApiResponseUtil.ApiResponse<RequestType>> createCall();
}
