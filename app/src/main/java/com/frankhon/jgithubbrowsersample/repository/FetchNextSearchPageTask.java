package com.frankhon.jgithubbrowsersample.repository;

import androidx.lifecycle.MutableLiveData;

import com.frankhon.jgithubbrowsersample.api.ApiResponse;
import com.frankhon.jgithubbrowsersample.api.ApiResponse.ApiEmptyResponse;
import com.frankhon.jgithubbrowsersample.api.ApiResponse.ApiErrorResponse;
import com.frankhon.jgithubbrowsersample.api.ApiResponse.ApiSuccessResponse;
import com.frankhon.jgithubbrowsersample.api.GithubServiceImpl;
import com.frankhon.jgithubbrowsersample.api.RepoSearchResponse;
import com.frankhon.jgithubbrowsersample.db.GithubDb;
import com.frankhon.jgithubbrowsersample.util.ResourceUtil;
import com.frankhon.jgithubbrowsersample.vo.Repo;
import com.frankhon.jgithubbrowsersample.vo.RepoSearchResult;
import com.frankhon.jgithubbrowsersample.vo.Resource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

/**
 * Created by Frank_Hon on 7/26/2019.
 * E-mail: v-shhong@microsoft.com
 *
 * A task that reads the search result in the database and fetches the next page, if it has one.
 */
public class FetchNextSearchPageTask implements Runnable {

    private String query;
    private GithubServiceImpl githubService;
    private GithubDb db;

    private MutableLiveData<Resource<Boolean>> liveData = new MutableLiveData<>();

    public FetchNextSearchPageTask(String query, GithubServiceImpl githubService, GithubDb db) {
        this.query = query;
        this.githubService = githubService;
        this.db = db;
    }

    @Override
    public void run() {
        RepoSearchResult current = db.repoDao().findSearchResult(query);
        if (current == null) {
            liveData.postValue(null);
            return;
        }

        int nextPage = current.getNext();
        if (nextPage == 0) {
            liveData.postValue(ResourceUtil.success(false));
            return;
        }

        Resource<Boolean> newValue = null;

        try {
            Response<RepoSearchResponse> response = githubService.searchRepos(query, nextPage).execute();
            ApiResponse apiResponse = ApiResponse.create(response);
            if (apiResponse instanceof ApiSuccessResponse) {
                ApiSuccessResponse<RepoSearchResponse> apiSuccessResponse = (ApiSuccessResponse<RepoSearchResponse>) apiResponse;
                List<Integer> ids = new ArrayList<>(current.getRepoIds());
                if (apiSuccessResponse.getBody() != null && apiSuccessResponse.getBody().getItems() != null) {
                    for (Repo repo : apiSuccessResponse.getBody().getItems()) {
                        ids.add(repo.getId());
                    }

                    RepoSearchResult merged = new RepoSearchResult(query, ids, apiSuccessResponse.getBody().getTotal(),
                            apiSuccessResponse.getNextPage());

                    db.runInTransaction(() -> {
                        db.repoDao().insert(merged);
                        db.repoDao().insertRepos(apiSuccessResponse.getBody().getItems());
                    });
                }

                newValue = ResourceUtil.success(apiSuccessResponse.getNextPage() != 0);
            } else if (apiResponse instanceof ApiEmptyResponse) {
                newValue = ResourceUtil.success(false);
            } else if (apiResponse instanceof ApiErrorResponse) {
                newValue = ResourceUtil.error(((ApiErrorResponse) apiResponse).getErrorMessage(), true);
            }
        } catch (IOException e) {
            newValue = ResourceUtil.error(e.getMessage(), true);
        }

        liveData.postValue(newValue);
    }

    public MutableLiveData<Resource<Boolean>> getLiveData() {
        return liveData;
    }
}
