package com.frankhon.jgithubbrowsersample.ui.repo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.frankhon.jgithubbrowsersample.AppExecutors;
import com.frankhon.jgithubbrowsersample.R;
import com.frankhon.jgithubbrowsersample.di.InjectorUtils;
import com.frankhon.jgithubbrowsersample.ui.common.LoadingFragment;
import com.frankhon.jgithubbrowsersample.vo.Contributor;
import com.frankhon.jgithubbrowsersample.vo.Repo;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Frank Hon on 2019-07-28 19:49.
 * E-mail: frank_hon@foxmail.com
 */
public class RepoFragment extends LoadingFragment {

    public static final String KEY_LOGIN = "REPO_LOGIN";
    public static final String KEY_NAME = "REPO_NAME";

    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.description)
    TextView description;
    @BindView(R.id.contributor_list)
    RecyclerView contributorList;

    private RepoViewModel repoViewModel;
    private ContributorAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.repo_fragment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        repoViewModel = ViewModelProviders.of(this, InjectorUtils.provideRepoViewModelFactory(getContext())).get(RepoViewModel.class);

        Bundle arguments = getArguments();
        if (arguments != null) {
            String owner = arguments.getString(KEY_LOGIN);
            String name = arguments.getString(KEY_NAME);
            repoViewModel.setId(owner, name);
        }

        repoViewModel.getRepo().observe(this, resource -> {
            if (resource != null) {
                processLoadingState(resource);

                Repo repo = resource.getData();
                if (repo != null) {
                    name.setText(repo.getName());
                    description.setText(repo.getDescription());
                } else {
                    name.setVisibility(View.GONE);
                    description.setVisibility(View.GONE);
                }
            }
        });

        setOnRetryClickListener(v -> repoViewModel.retry());

        initContributorList();
    }

    private void initContributorList() {
        adapter = new ContributorAdapter(AppExecutors.getInstance());
        adapter.setOnContributorClickListener(contributor->{
            Toast.makeText(getContext(), contributor.getLogin(), Toast.LENGTH_SHORT).show();
        });
        contributorList.setAdapter(adapter);
        contributorList.setLayoutManager(new LinearLayoutManager(getContext()));

        repoViewModel.getContributors().observe(this, listResource -> {
            if (listResource != null) {
                List<Contributor> contributors = listResource.getData();
                if (contributors != null) {
                    adapter.submitList(contributors);
                } else {
                    adapter.submitList(Collections.emptyList());
                }
            }
        });
    }
}