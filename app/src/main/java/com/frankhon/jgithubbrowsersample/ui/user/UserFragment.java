package com.frankhon.jgithubbrowsersample.ui.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.frankhon.jgithubbrowsersample.R;
import com.frankhon.jgithubbrowsersample.di.InjectorUtils;
import com.frankhon.jgithubbrowsersample.ui.common.LoadingFragment;
import com.frankhon.jgithubbrowsersample.ui.common.RepoListAdapter;

import butterknife.ButterKnife;

/**
 * Created by Frank Hon on 2019-07-31 00:56.
 * E-mail: frank_hon@foxmail.com
 */
public class UserFragment extends LoadingFragment {

    public static final String KEY_LOGIN = "CONTRIBUTOR_LOGIN";
    public static final String KEY_AVATAR = "CONTRIBUTOR_AVATAR";

    private UserViewModel userViewModel;
    private RepoListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_fragment, container, false);
        ButterKnife.bind(this, view);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userViewModel = ViewModelProviders
                .of(this, InjectorUtils.provideUserViewModelFactory(getContext()))
                .get(UserViewModel.class);

        setOnRetryClickListener(v -> userViewModel.retry());
    }
}
