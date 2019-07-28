package com.frankhon.jgithubbrowsersample.api;

import androidx.lifecycle.LiveData;

import com.frankhon.jgithubbrowsersample.vo.Repo;
import com.frankhon.jgithubbrowsersample.vo.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

import static com.frankhon.jgithubbrowsersample.api.ApiResponseUtil.ApiResponse;

/**
 * Created by Frank_Hon on 7/23/2019.
 * E-mail: v-shhong@microsoft.com
 * <p>
 * REST API access points
 */
public interface GithubService {

    @GET("users/{login}")
    LiveData<ApiResponse<User>> getUser(@Path("login") String login);

    @GET("users/{login}/repos")
    LiveData<ApiResponse<List<Repo>>> getRepos(@Path("login") String login);

    @GET("repos/{owner}/{name}")
    LiveData<ApiResponse<Repo>> getRepo(@Path("owner") String owner, @Path("name") String name);

    @GET("search/repositories")
    LiveData<ApiResponse<RepoSearchResponse>> searchRepos(@Query("q") String query);

    @GET("search/repositories")
    Call<RepoSearchResponse> searchRepos(@Query("q") String query, @Query("page") int page);
}
