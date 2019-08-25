package com.frankhon.jgithubbrowsersample.ui.search;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.frankhon.jgithubbrowsersample.AppExecutors;
import com.frankhon.jgithubbrowsersample.R;
import com.frankhon.jgithubbrowsersample.di.InjectorUtils;
import com.frankhon.jgithubbrowsersample.ui.common.LoadingFragment;
import com.frankhon.jgithubbrowsersample.ui.common.RepoListAdapter;
import com.frankhon.jgithubbrowsersample.ui.repo.RepoFragment;
import com.frankhon.jgithubbrowsersample.util.Util;
import com.google.android.material.snackbar.Snackbar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Frank_Hon on 7/22/2019.
 * E-mail: v-shhong@microsoft.com
 */
public class SearchFragment extends LoadingFragment {

    @BindView(R.id.repo_list)
    RecyclerView repoList;
    @BindView(R.id.load_more_bar)
    ProgressBar loadMoreBar;
    @BindView(R.id.input)
    EditText input;

    private SearchViewModel searchViewModel;

    private RepoListAdapter adapter;

    private boolean shouldLoadingMore;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_fragment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        searchViewModel = ViewModelProviders.of(this, InjectorUtils.provideSearchViewModelFactory(getContext())).get(SearchViewModel.class);

        initRecyclerView();
        initSearchInputListener();

        setOnRetryClickListener(v -> searchViewModel.refresh());
    }

    private void initSearchInputListener() {
        input.setOnEditorActionListener((view, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                doSearch(view);
                return true;
            } else {
                return false;
            }
        });

        input.setOnKeyListener((view, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN && event.getAction() == KeyEvent.KEYCODE_ENTER) {
                doSearch(view);
                return true;
            } else {
                return false;
            }
        });
    }

    private void doSearch(View view) {
        String query = input.getText().toString();
        // Dismiss keyboard
        Util.dismissKeyboard(getContext(), view);
        searchViewModel.setQuery(query);
    }

    private void initRecyclerView() {
        repoList.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RepoListAdapter(AppExecutors.getInstance());
        adapter.setOnRepoClickListener(repo -> {
            Util.dismissKeyboard(getContext(), repoList);

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
        repoList.setAdapter(adapter);
        repoList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (recyclerView.getLayoutManager() != null) {
                    LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    int lastPosition = layoutManager.findLastVisibleItemPosition();
                    if (lastPosition != -1 && !shouldLoadingMore && lastPosition == adapter.getItemCount() - 1) {
                        searchViewModel.loadNextPage();
                    }
                }
            }
        });

        searchViewModel.getResults().observe(this, result -> {
            if (result != null) {
                processLoadingState(result);

                adapter.submitList(result.getData());
            }
        });

        searchViewModel.getLoadMoreState().observe(this, loadingMore -> {

            if (loadingMore == null) {
                shouldLoadingMore = false;
            } else {
                shouldLoadingMore = loadingMore.isRunning();
                String error = loadingMore.getErrorMessage();
                if (error != null) {
                    Snackbar.make(loadMoreBar, error, Snackbar.LENGTH_LONG).show();
                }
            }

            loadMoreBar.setVisibility(shouldLoadingMore ? View.VISIBLE : View.GONE);
        });
    }
}
