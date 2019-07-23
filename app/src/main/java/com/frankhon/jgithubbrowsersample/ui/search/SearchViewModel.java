package com.frankhon.jgithubbrowsersample.ui.search;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

/**
 * Created by Frank_Hon on 7/23/2019.
 * E-mail: v-shhong@microsoft.com
 */
public class SearchViewModel extends ViewModel {

    private final LiveData<String> query;

    public SearchViewModel(LiveData<String> query) {
        this.query = query;
    }
}
