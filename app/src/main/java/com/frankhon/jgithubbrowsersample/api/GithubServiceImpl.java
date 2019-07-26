package com.frankhon.jgithubbrowsersample.api;

import androidx.lifecycle.LiveData;

import com.frankhon.jgithubbrowsersample.util.LiveDataCallAdapterFactory;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Frank_Hon on 7/25/2019.
 * E-mail: v-shhong@microsoft.com
 */
public class GithubServiceImpl {

    private static final String BASE_URL = "https://api.github.com/";

    private static volatile GithubServiceImpl INSTANCE;

    private GithubService mGithubService;

    private GithubServiceImpl() {
        mGithubService = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .build()
                .create(GithubService.class);
    }

    public static GithubServiceImpl getInstance() {
        if (INSTANCE == null) {
            synchronized (GithubServiceImpl.class) {
                if (INSTANCE == null) {
                    INSTANCE = new GithubServiceImpl();
                }
            }
        }

        return INSTANCE;
    }

    public LiveData<ApiResponse> getUser(String login) {
        return mGithubService.getUser(login);
    }

    public LiveData<ApiResponse> getRepos(String login) {
        return mGithubService.getRepos(login);
    }

    public LiveData<ApiResponse> searchRepos(String query) {
        return mGithubService.searchRepos(query);
    }

    public Call<RepoSearchResponse> searchRepos(String query, int page) {
        return mGithubService.searchRepos(query, page);
    }
}
