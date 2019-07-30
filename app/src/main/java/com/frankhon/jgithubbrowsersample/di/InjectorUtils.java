package com.frankhon.jgithubbrowsersample.di;

import android.content.Context;

import com.frankhon.jgithubbrowsersample.AppExecutors;
import com.frankhon.jgithubbrowsersample.api.GithubServiceImpl;
import com.frankhon.jgithubbrowsersample.db.GithubDb;
import com.frankhon.jgithubbrowsersample.repository.RepoRepository;
import com.frankhon.jgithubbrowsersample.viewmodel.RepoViewModelFactory;
import com.frankhon.jgithubbrowsersample.viewmodel.SearchViewModelFactory;

/**
 * Created by Frank Hon on 2019-07-28 10:40.
 * E-mail: frank_hon@foxmail.com
 */
public final class InjectorUtils {

    private InjectorUtils() {
    }

    private static RepoRepository provideRepoRepository(Context context) {
        return RepoRepository.getInstance(
                AppExecutors.getInstance(),
                GithubDb.getInstance(context),
                GithubServiceImpl.getInstance());
    }

    public static SearchViewModelFactory provideSearchViewModelFactory(Context context) {
        return new SearchViewModelFactory(provideRepoRepository(context));
    }

    public static RepoViewModelFactory provideRepoViewModelFactory(Context context) {
        return new RepoViewModelFactory(provideRepoRepository(context));
    }

}
