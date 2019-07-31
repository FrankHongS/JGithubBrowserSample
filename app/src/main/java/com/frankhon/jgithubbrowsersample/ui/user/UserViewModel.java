package com.frankhon.jgithubbrowsersample.ui.user;

import android.text.TextUtils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.frankhon.jgithubbrowsersample.repository.RepoRepository;
import com.frankhon.jgithubbrowsersample.repository.UserRepository;
import com.frankhon.jgithubbrowsersample.util.AbsentLiveData;
import com.frankhon.jgithubbrowsersample.vo.Repo;
import com.frankhon.jgithubbrowsersample.vo.Resource;
import com.frankhon.jgithubbrowsersample.vo.User;

import java.util.List;

/**
 * Created by Frank_Hon on 7/31/2019.
 * E-mail: v-shhong@microsoft.com
 */
public class UserViewModel extends ViewModel {

    private UserRepository userRepository;
    private RepoRepository repoRepository;

    private MutableLiveData<String> login = new MutableLiveData<>();

    public UserViewModel(UserRepository userRepository, RepoRepository repoRepository) {
        this.userRepository = userRepository;
        this.repoRepository = repoRepository;
    }

    public LiveData<Resource<List<Repo>>> getRepositories() {
        return Transformations.switchMap(login, loginStr -> {
            if (TextUtils.isEmpty(loginStr)) {
                return AbsentLiveData.create();
            } else {
                return repoRepository.loadRepos(loginStr);
            }
        });
    }

    public LiveData<Resource<User>> getUser() {
        return Transformations.switchMap(login, loginStr -> {
            if (TextUtils.isEmpty(loginStr)) {
                return AbsentLiveData.create();
            } else {
                return userRepository.loadUser(loginStr);
            }
        });
    }

    public void setLogin(String loginStr) {
        if (loginStr != null && !loginStr.equals(login.getValue())) {
            login.setValue(loginStr);
        }
    }

    public void retry() {
        if (!TextUtils.isEmpty(login.getValue())) {
            login.setValue(login.getValue());
        }
    }
}
