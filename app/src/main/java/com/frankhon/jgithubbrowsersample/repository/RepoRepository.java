package com.frankhon.jgithubbrowsersample.repository;

import androidx.lifecycle.LiveData;

import com.frankhon.jgithubbrowsersample.AppExecutors;
import com.frankhon.jgithubbrowsersample.api.ApiResponse;
import com.frankhon.jgithubbrowsersample.api.GithubServiceImpl;
import com.frankhon.jgithubbrowsersample.api.RepoSearchResponse;
import com.frankhon.jgithubbrowsersample.db.GithubDb;
import com.frankhon.jgithubbrowsersample.db.RepoDao;
import com.frankhon.jgithubbrowsersample.util.RateLimiter;
import com.frankhon.jgithubbrowsersample.vo.Repo;
import com.frankhon.jgithubbrowsersample.vo.Resource;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Frank_Hon on 7/23/2019.
 * E-mail: v-shhong@microsoft.com
 */
public final class RepoRepository {

    private final AppExecutors appExecutors;
    private final GithubDb db;
    private final RepoDao repoDao;
    private final GithubServiceImpl githubService;

    private final RateLimiter<String> repoListRateLimit = new RateLimiter<>(10, TimeUnit.MINUTES);

    public RepoRepository(AppExecutors appExecutors, GithubDb db, RepoDao repoDao, GithubServiceImpl githubService) {
        this.appExecutors = appExecutors;
        this.db = db;
        this.repoDao = repoDao;
        this.githubService = githubService;
    }

    public LiveData<Resource<List<Repo>>> loadRepos(String owner){
        return new NetworkBoundResource<List<Repo>,List<Repo>>(appExecutors){
            @Override
            protected void saveCallResult(List<Repo> item) {
                repoDao.insertRepos(item);
            }

            @Override
            protected boolean shouldFetch(List<Repo> data) {
                return data==null||data.isEmpty()||repoListRateLimit.shouldFetch(owner);
            }

            @Override
            protected LiveData<List<Repo>> loadFromDb() {
                return repoDao.loadRepositories(owner);
            }

            @Override
            protected LiveData<ApiResponse> createCall() {
                return githubService.getRepos(owner);
            }

            @Override
            protected void onFetchFailed() {
                repoListRateLimit.reset(owner);
            }
        }.asLiveData();
    }

    public LiveData<Resource<List<Repo>>> search(String query){
        return new NetworkBoundResource<List<Repo>, RepoSearchResponse>(appExecutors){
            @Override
            protected void saveCallResult(RepoSearchResponse item) {

                if (item==null){
                    return;
                }

                List<Integer> reposIds=new ArrayList<>();
                for(Repo repo:item.getItems()){
                    reposIds.add(repo.getId());
                }



            }

            @Override
            protected boolean shouldFetch(List<Repo> data) {
                return false;
            }

            @Override
            protected LiveData<List<Repo>> loadFromDb() {
                return null;
            }

            @Override
            protected LiveData<ApiResponse> createCall() {
                return null;
            }
        }.asLiveData();
    }
}
