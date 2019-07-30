package com.frankhon.jgithubbrowsersample.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.frankhon.jgithubbrowsersample.repository.RepoRepository;
import com.frankhon.jgithubbrowsersample.ui.repo.RepoViewModel;

/**
 * Created by Frank_Hon on 7/30/2019.
 * E-mail: v-shhong@microsoft.com
 */
public class RepoViewModelFactory implements ViewModelProvider.Factory {

    private RepoRepository repository;

    public RepoViewModelFactory(RepoRepository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new RepoViewModel(repository);
    }
}
