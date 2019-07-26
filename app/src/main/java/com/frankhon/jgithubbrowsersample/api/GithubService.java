package com.frankhon.jgithubbrowsersample.api;

import androidx.lifecycle.LiveData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Frank_Hon on 7/23/2019.
 * E-mail: v-shhong@microsoft.com
 * <p>
 * REST API access points
 */
public interface GithubService {

    @GET("users/{login}")
    LiveData<ApiResponse> getUser(@Path("login") String login);

    @GET("users/{login}/repos")
    LiveData<ApiResponse> getRepos(@Path("login") String login);

    @GET("search/repositories")
    LiveData<ApiResponse> searchRepos(@Query("q") String query);

    @GET("search/repositories")
    Call<RepoSearchResponse> searchRepos(@Query("q") String query, @Query("page") int page);
}
