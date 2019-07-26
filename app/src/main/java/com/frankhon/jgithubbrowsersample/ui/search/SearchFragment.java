package com.frankhon.jgithubbrowsersample.ui.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.frankhon.jgithubbrowsersample.AppExecutors;
import com.frankhon.jgithubbrowsersample.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Frank_Hon on 7/22/2019.
 * E-mail: v-shhong@microsoft.com
 */
public class SearchFragment extends Fragment {

    @BindView(R.id.repo_list)
    RecyclerView repoList;

    private AppExecutors appExecutors;
    private SearchViewModel searchViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_fragment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        searchViewModel= ViewModelProviders.of(this).get(SearchViewModel.class);
    }

    private void initRecyclerView(){
        repoList.setLayoutManager(new LinearLayoutManager(getContext()));
        repoList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if(recyclerView.getLayoutManager()!=null){
                    LinearLayoutManager layoutManager= (LinearLayoutManager) recyclerView.getLayoutManager();
                    int lastPosition=layoutManager.findLastVisibleItemPosition();
//                    if (lastPosition==)
                }
            }
        });
    }
}
