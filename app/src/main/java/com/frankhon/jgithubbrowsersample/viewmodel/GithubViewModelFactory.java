package com.frankhon.jgithubbrowsersample.viewmodel;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.frankhon.jgithubbrowsersample.AppExecutors;
import com.frankhon.jgithubbrowsersample.api.GithubServiceImpl;
import com.frankhon.jgithubbrowsersample.db.GithubDb;
import com.frankhon.jgithubbrowsersample.repository.RepoRepository;
import com.frankhon.jgithubbrowsersample.ui.search.SearchViewModel;

/**
 * Created by Frank Hon on 2019-07-27 02:15.
 * E-mail: frank_hon@foxmail.com
 */
public class GithubViewModelFactory implements ViewModelProvider.Factory {

    private Context context;

    public GithubViewModelFactory(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new SearchViewModel(RepoRepository.getInstance(
                AppExecutors.getInstance(),
                GithubDb.getInstance(context),
                GithubServiceImpl.getInstance()
        ));
    }
}
