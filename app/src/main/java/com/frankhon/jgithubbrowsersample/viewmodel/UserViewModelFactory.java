package com.frankhon.jgithubbrowsersample.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.frankhon.jgithubbrowsersample.repository.RepoRepository;
import com.frankhon.jgithubbrowsersample.repository.UserRepository;
import com.frankhon.jgithubbrowsersample.ui.user.UserViewModel;

/**
 * Created by Frank_Hon on 7/31/2019.
 * E-mail: v-shhong@microsoft.com
 */
public class UserViewModelFactory implements ViewModelProvider.Factory {

    private UserRepository userRepository;
    private RepoRepository repoRepository;

    public UserViewModelFactory(UserRepository userRepository, RepoRepository repoRepository) {
        this.userRepository = userRepository;
        this.repoRepository = repoRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new UserViewModel(userRepository, repoRepository);
    }
}
