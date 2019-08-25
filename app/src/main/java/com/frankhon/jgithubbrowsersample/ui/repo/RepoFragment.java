package com.frankhon.jgithubbrowsersample.ui.repo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.SharedElementCallback;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.FragmentNavigator;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.TransitionInflater;
import androidx.transition.TransitionSet;

import com.frankhon.jgithubbrowsersample.AppExecutors;
import com.frankhon.jgithubbrowsersample.MainActivity;
import com.frankhon.jgithubbrowsersample.R;
import com.frankhon.jgithubbrowsersample.di.InjectorUtils;
import com.frankhon.jgithubbrowsersample.ui.common.LoadingFragment;
import com.frankhon.jgithubbrowsersample.ui.user.UserFragment;
import com.frankhon.jgithubbrowsersample.vo.Contributor;
import com.frankhon.jgithubbrowsersample.vo.Repo;

import java.util.Collections;
import java.util.List;
import java.util.Map;

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
        Log.d("shuai", "onCreateView: ");
        View view = inflater.inflate(R.layout.repo_fragment, container, false);
        ButterKnife.bind(this, view);

        setSharedElementReturnTransition(TransitionInflater.from(getContext()).inflateTransition(R.transition.move));

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d("shuai", "onViewCreated: ");

        repoViewModel = ViewModelProviders.of(this, InjectorUtils.provideRepoViewModelFactory(getContext())).get(RepoViewModel.class);

        Bundle arguments = getArguments();
        if (arguments != null) {
            String owner = arguments.getString(KEY_LOGIN);
            String name = arguments.getString(KEY_NAME);
            repoViewModel.setId(owner, name);
        }

        repoViewModel.getRepo().observe(this, resource -> {
            if (resource != null) {
                Repo repo = resource.getData();
                if (repo != null) {
                    name.setText(repo.getName());
                    description.setText(repo.getDescription());
                }
            }
        });

        setOnRetryClickListener(v -> repoViewModel.retry());

        initContributorList();

    }

    private void initContributorList() {
        adapter = new ContributorAdapter(AppExecutors.getInstance());
        adapter.setOnContributorClickListener((contributor, avatar) -> {

            Bundle args = new Bundle();
            args.putString(UserFragment.KEY_LOGIN, contributor.getLogin());
            args.putString(UserFragment.KEY_AVATAR, contributor.getImageUrl());

            FragmentNavigator.Extras extras = new FragmentNavigator.Extras
                    .Builder()
                    .addSharedElement(avatar, contributor.getLogin())
                    .build();

            NavHostFragment.findNavController(this)
                    .navigate(
                            R.id.userFragment,
                            args,
                            null,
                            extras
                    );
        });
        contributorList.setAdapter(adapter);
        contributorList.setLayoutManager(new LinearLayoutManager(getContext()));

        postponeEnterTransition();
        contributorList.getViewTreeObserver().addOnPreDrawListener(() -> {
            startPostponedEnterTransition();
            return true;
        });

        repoViewModel.getContributors().observe(this, listResource -> {
            if (listResource != null) {
                processLoadingState(listResource);
                Log.d("shuai", "listResource status: "+listResource.getStatus());
                List<Contributor> contributors = listResource.getData();
                if (contributors != null) {
                    Log.d("shuai", "onViewCreated: " + contributors.size());
                    adapter.submitList(contributors);
                } else {
                    Log.d("shuai", "onViewCreated: null");
                    adapter.submitList(Collections.emptyList());
                }
            }
        });
    }
}
