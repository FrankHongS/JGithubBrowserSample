package com.frankhon.jgithubbrowsersample.ui.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.frankhon.jgithubbrowsersample.AppExecutors;
import com.frankhon.jgithubbrowsersample.R;
import com.frankhon.jgithubbrowsersample.ui.common.RepoListAdapter;
import com.frankhon.jgithubbrowsersample.ui.common.RepoListAdapter$RepoViewHolder_ViewBinding;
import com.frankhon.jgithubbrowsersample.vo.Repo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Frank Hon on 2019-07-28 13:30.
 * E-mail: frank_hon@foxmail.com
 */
public class TestFragment extends Fragment {

    @BindView(R.id.test_list)
    RecyclerView list;
    @BindView(R.id.test_refresh)
    Button refresh;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.test_layout,container,false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RepoListAdapter adapter=new RepoListAdapter(AppExecutors.getInstance());
        list.setAdapter(adapter);
        list.setLayoutManager(new LinearLayoutManager(getContext()));

        List<Repo> repoList=generate();
        adapter.submitList(repoList);

        refresh.setOnClickListener(v->{
            adapter.submitList(update());
        });
    }

    private List<Repo> generate(){
        List<Repo> repoList=new ArrayList<>();

        repoList.add(new Repo(0,"a","ab","abc",new Repo.Owner("abcd","http://abc"),1));
        repoList.add(new Repo(1,"b","abb","abcd",new Repo.Owner("abcdef","http://abceg"),2));
        repoList.add(new Repo(2,"c","abc","abcde",new Repo.Owner("abcdefgg","http://abcegge"),3));

        return repoList;
    }

    private List<Repo> update(){
        List<Repo> repoList=new ArrayList<>();

        repoList.add(new Repo(0,"a","ab","abc",new Repo.Owner("abcd","http://abc"),1));
        repoList.add(new Repo(1,"b","abb","abcd",new Repo.Owner("abcdef","http://abceg"),2));
        repoList.add(new Repo(2,"c","abc","abcde",new Repo.Owner("abcdefgg","http://abcegge"),3));

        repoList.add(new Repo(4,"z","za","zac",new Repo.Owner("zadaf","http://zrer"),6));
        return repoList;
    }
}
