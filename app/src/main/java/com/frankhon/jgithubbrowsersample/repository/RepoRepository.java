package com.frankhon.jgithubbrowsersample.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.frankhon.jgithubbrowsersample.AppExecutors;
import com.frankhon.jgithubbrowsersample.api.ApiResponseUtil;
import com.frankhon.jgithubbrowsersample.api.GithubServiceImpl;
import com.frankhon.jgithubbrowsersample.api.RepoSearchResponse;
import com.frankhon.jgithubbrowsersample.db.GithubDb;
import com.frankhon.jgithubbrowsersample.db.RepoDao;
import com.frankhon.jgithubbrowsersample.util.AbsentLiveData;
import com.frankhon.jgithubbrowsersample.util.RateLimiter;
import com.frankhon.jgithubbrowsersample.vo.Contributor;
import com.frankhon.jgithubbrowsersample.vo.Repo;
import com.frankhon.jgithubbrowsersample.vo.RepoSearchResult;
import com.frankhon.jgithubbrowsersample.vo.Resource;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Frank_Hon on 7/23/2019.
 * E-mail: v-shhong@microsoft.com
 */
public final class RepoRepository {

    private static volatile RepoRepository INSTANCE;

    private final AppExecutors appExecutors;
    private final GithubDb db;
    private final RepoDao repoDao;
    private final GithubServiceImpl githubService;

    private final RateLimiter<String> repoListRateLimit = new RateLimiter<>(10, TimeUnit.MINUTES);

    private RepoRepository(AppExecutors appExecutors, GithubDb db, GithubServiceImpl githubService) {
        this.appExecutors = appExecutors;
        this.db = db;
        this.githubService = githubService;

        this.repoDao = db.repoDao();
    }

    public static RepoRepository getInstance(AppExecutors appExecutors, GithubDb db, GithubServiceImpl githubService) {
        if (INSTANCE == null) {
            synchronized (RepoRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new RepoRepository(appExecutors, db, githubService);
                }
            }
        }

        return INSTANCE;
    }

    public LiveData<Resource<List<Repo>>> loadRepos(String owner) {
        return new NetworkBoundResource<List<Repo>, List<Repo>>(appExecutors) {
            @Override
            protected void saveCallResult(List<Repo> item) {
                repoDao.insertRepos(item);
            }

            @Override
            protected boolean shouldFetch(List<Repo> data) {
                return data == null || data.isEmpty() || repoListRateLimit.shouldFetch(owner);
            }

            @Override
            protected LiveData<List<Repo>> loadFromDb() {
                return repoDao.loadRepositories(owner);
            }

            @Override
            protected LiveData<ApiResponseUtil.ApiResponse<List<Repo>>> createCall() {
                return githubService.getRepos(owner);
            }

            @Override
            protected void onFetchFailed() {
                repoListRateLimit.reset(owner);
            }
        }.asLiveData();
    }

    public LiveData<Resource<Repo>> loadRepo(String owner, String name) {
        return new NetworkBoundResource<Repo, Repo>(appExecutors) {
            @Override
            protected void saveCallResult(Repo item) {
                repoDao.insert(item);
            }

            @Override
            protected boolean shouldFetch(Repo data) {
                return data == null;
            }

            @Override
            protected LiveData<Repo> loadFromDb() {
                return repoDao.load(owner, name);
            }

            @Override
            protected LiveData<ApiResponseUtil.ApiResponse<Repo>> createCall() {
                return githubService.getRepo(owner, name);
            }
        }.asLiveData();
    }

    public LiveData<Resource<List<Contributor>>> loadContributors(String owner, String name) {
        // todo
        return null;
    }

    public LiveData<Resource<Boolean>> searchNextPage(String query) {
        FetchNextSearchPageTask fetchNextSearchPageTask = new FetchNextSearchPageTask(
                query,
                githubService,
                db
        );
        appExecutors.getNetworkIO().execute(fetchNextSearchPageTask);
        return fetchNextSearchPageTask.getLiveData();
    }

    public LiveData<Resource<List<Repo>>> search(String query) {
        return new NetworkBoundResource<List<Repo>, RepoSearchResponse>(appExecutors) {
            @Override
            protected void saveCallResult(RepoSearchResponse item) {

                if (item == null) {
                    return;
                }

                List<Integer> reposIds = new ArrayList<>();
                for (Repo repo : item.getItems()) {
                    reposIds.add(repo.getId());
                }
                RepoSearchResult repoSearchResult = new RepoSearchResult(
                        query,
                        reposIds,
                        item.getTotal(),
                        item.getNextPage()
                );
                db.runInTransaction(() -> {
                    repoDao.insertRepos(item.getItems());
                    repoDao.insert(repoSearchResult);
                });

            }

            @Override
            protected boolean shouldFetch(List<Repo> data) {
                return data == null;
            }

            @Override
            protected LiveData<List<Repo>> loadFromDb() {
                Log.d("Hon", "loadFromDb: ");
                return Transformations.switchMap(repoDao.search(query), searchData -> {
                    if (searchData == null) {
                        return AbsentLiveData.create();
                    } else {
                        return repoDao.loadOrdered(searchData.getRepoIds());
                    }
                });
            }

            @Override
            protected LiveData<ApiResponseUtil.ApiResponse<RepoSearchResponse>> createCall() {
                return githubService.searchRepos(query);
            }

            @Override
            protected RepoSearchResponse processResponse(ApiResponseUtil.ApiSuccessResponse<RepoSearchResponse> response) {
                RepoSearchResponse body = response.getBody();
                body.setNextPage(response.getNextPage());
                return body;
            }
        }.asLiveData();
    }
}
