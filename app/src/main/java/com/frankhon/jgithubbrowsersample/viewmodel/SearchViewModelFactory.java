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
public class SearchViewModelFactory implements ViewModelProvider.Factory {

    private RepoRepository repository;

    public SearchViewModelFactory(RepoRepository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new SearchViewModel(repository);
    }
}
