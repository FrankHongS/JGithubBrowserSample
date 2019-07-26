package com.frankhon.jgithubbrowsersample.ui.search;

import android.text.TextUtils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.frankhon.jgithubbrowsersample.repository.RepoRepository;
import com.frankhon.jgithubbrowsersample.util.AbsentLiveData;
import com.frankhon.jgithubbrowsersample.util.RequestStatus;
import com.frankhon.jgithubbrowsersample.vo.Repo;
import com.frankhon.jgithubbrowsersample.vo.Resource;

import java.util.List;
import java.util.Locale;

/**
 * Created by Frank_Hon on 7/23/2019.
 * E-mail: v-shhong@microsoft.com
 */
public class SearchViewModel extends ViewModel {

    private final MutableLiveData<String> query;
    private NextPageHandler nextPageHandler;

    private LiveData<Resource<List<Repo>>> results;

    private LiveData<LoadMoreState> loadMoreState;

    public SearchViewModel(RepoRepository repoRepository) {
        this.query = new MutableLiveData<>();
        this.nextPageHandler = new NextPageHandler(repoRepository);

        results = Transformations
                .switchMap(query, search -> {
                    if (TextUtils.isEmpty(search)) {
                        return AbsentLiveData.create();
                    } else {
                        return repoRepository.search(search);
                    }
                });
    }

    public void setQuery(String originalInput) {
        String input = originalInput.toLowerCase(Locale.getDefault()).trim();
        if (input.equals(query.getValue())) {
            return;
        }

        nextPageHandler.reset();
        query.setValue(input);
    }

    public void loadNextPage() {
        String value = query.getValue();
        if (!TextUtils.isEmpty(value)) {
            nextPageHandler.queryNextPage(value);
        }
    }

    public void refresh() {
        String value = query.getValue();
        if (value != null) {
            query.setValue(value);
        }
    }

    public LiveData<LoadMoreState> getLoadMoreState() {
        return nextPageHandler.loadMoreState;
    }

    private class LoadMoreState {
        private boolean isRunning;
        private String errorMessage;

        private boolean handledError = false;

        LoadMoreState(boolean isRunning, String errorMessage) {
            this.isRunning = isRunning;
            this.errorMessage = errorMessage;
        }

        public String getErrorMessage() {
            if (handledError) {
                return null;
            }
            handledError = true;
            return errorMessage;
        }

        public boolean isRunning() {
            return isRunning;
        }
    }

    class NextPageHandler implements Observer<Resource<Boolean>> {

        private RepoRepository repository;

        private LiveData<Resource<Boolean>> nextPageLiveData = null;
        private MutableLiveData<LoadMoreState> loadMoreState = new MutableLiveData<>();
        private String query = "";
        private boolean hasMore = false;

        public NextPageHandler(RepoRepository repository) {
            this.repository = repository;
        }

        private void queryNextPage(String query) {
            if (query == null || this.query.equals(query)) {
                return;
            }
            unregister();
            this.query = query;
            nextPageLiveData = repository.searchNextPage(query);
            loadMoreState.setValue(new LoadMoreState(true, null));

            if (nextPageLiveData != null) {
                nextPageLiveData.observeForever(this);
            }
        }

        @Override
        public void onChanged(Resource<Boolean> result) {
            if (result == null) {
                reset();
            } else {
                switch (result.getStatus()) {
                    case RequestStatus.SUCCESS:
                        hasMore = result.getData();
                        unregister();
                        loadMoreState.setValue(new LoadMoreState(false, null));
                        break;
                    case RequestStatus.ERROR:
                        hasMore = true;
                        unregister();
                        loadMoreState.setValue(new LoadMoreState(false, result.getMessage()));
                        break;
                    case RequestStatus.LOADING:
                        // ignore
                        break;
                }
            }
        }

        public boolean isHasMore() {
            return hasMore;
        }

        private void unregister() {
            if (nextPageLiveData != null) {
                nextPageLiveData.removeObserver(this);
                nextPageLiveData = null;
            }
            if (hasMore) {
                query = "";
            }
        }

        private void reset() {
            unregister();
            hasMore = true;
            loadMoreState.setValue(new LoadMoreState(false, null));
        }
    }
}
