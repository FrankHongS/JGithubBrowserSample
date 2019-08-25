package com.frankhon.jgithubbrowsersample.ui.user;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.SharedElementCallback;
import androidx.core.view.ViewCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.TransitionInflater;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.frankhon.jgithubbrowsersample.AppExecutors;
import com.frankhon.jgithubbrowsersample.R;
import com.frankhon.jgithubbrowsersample.di.InjectorUtils;
import com.frankhon.jgithubbrowsersample.ui.common.LoadingFragment;
import com.frankhon.jgithubbrowsersample.ui.common.RepoListAdapter;
import com.frankhon.jgithubbrowsersample.ui.repo.RepoFragment;
import com.frankhon.jgithubbrowsersample.vo.User;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Frank Hon on 2019-07-31 00:56.
 * E-mail: frank_hon@foxmail.com
 */
public class UserFragment extends LoadingFragment {

    public static final String KEY_LOGIN = "CONTRIBUTOR_LOGIN";
    public static final String KEY_AVATAR = "CONTRIBUTOR_AVATAR";

    @BindView(R.id.repo_list)
    RecyclerView repoList;
    @BindView(R.id.avatar)
    ImageView avatar;
    @BindView(R.id.name)
    TextView name;

    private UserViewModel userViewModel;
    private RepoListAdapter adapter;

    private Handler handler = new Handler(Looper.getMainLooper());

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_fragment, container, false);
        ButterKnife.bind(this, view);

        setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(R.transition.move));

        if (savedInstanceState == null) {
            postponeEnterTransition();
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userViewModel = ViewModelProviders
                .of(this, InjectorUtils.provideUserViewModelFactory(getContext()))
                .get(UserViewModel.class);

        Bundle arguments = getArguments();
        if (arguments != null) {
            String login = arguments.getString(KEY_LOGIN);
            userViewModel.setLogin(login);

            String avatarUrl = arguments.getString(KEY_AVATAR);

            ViewCompat.setTransitionName(avatar, login);

            // When the image is loaded, set the image request listener to start the transaction
            Glide.with(this)
                    .load(avatarUrl)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            startPostponedEnterTransition();
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            startPostponedEnterTransition();
                            return false;
                        }
                    })
                    .into(avatar);

            handler.postDelayed(this::startPostponedEnterTransition, 1000);

        }

        userViewModel.getUser().observe(this, userResource -> {
            if (userResource != null) {
                User user = userResource.getData();
                if (user != null) {
                    name.setText(TextUtils.isEmpty(user.getName()) ? user.getLogin() : user.getName());
                }
            }
        });

        setOnRetryClickListener(v -> userViewModel.retry());

        initRepoList();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

    private void initRepoList() {
        adapter = new RepoListAdapter(AppExecutors.getInstance());
        adapter.setOnRepoClickListener(repo -> {
            Bundle args = new Bundle();
            args.putString(RepoFragment.KEY_LOGIN, repo.getOwner().getLogin());
            args.putString(RepoFragment.KEY_NAME, repo.getName());
            NavHostFragment.findNavController(this)
                    .navigate(
                            R.id.repoFragment,
                            args,
                            null
                    );
        });

        repoList.setLayoutManager(new LinearLayoutManager(getContext()));
        repoList.setAdapter(adapter);

        userViewModel.getRepositories().observe(this, reposRes -> {
            if (reposRes != null) {
                processLoadingState(reposRes);

                adapter.submitList(reposRes.getData());
            }
        });
    }
}
