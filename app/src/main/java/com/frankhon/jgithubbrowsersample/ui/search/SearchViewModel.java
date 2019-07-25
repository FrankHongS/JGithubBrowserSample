package com.frankhon.jgithubbrowsersample.ui.search;

import android.text.TextUtils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.frankhon.jgithubbrowsersample.repository.RepoRepository;
import com.frankhon.jgithubbrowsersample.util.AbsentLiveData;
import com.frankhon.jgithubbrowsersample.vo.Repo;
import com.frankhon.jgithubbrowsersample.vo.Resource;

import java.util.List;

/**
 * Created by Frank_Hon on 7/23/2019.
 * E-mail: v-shhong@microsoft.com
 */
public class SearchViewModel extends ViewModel {

    private final LiveData<String> query;

    private LiveData<Resource<List<Repo>>> results;

    public SearchViewModel(RepoRepository repoRepository) {
        this.query = new MutableLiveData<>();

        results= Transformations
                .switchMap(query,search->{
                    if(TextUtils.isEmpty(search)){
                        return AbsentLiveData.create();
                    }else {
                        repoRepository.
                    }
                });
    }
}
