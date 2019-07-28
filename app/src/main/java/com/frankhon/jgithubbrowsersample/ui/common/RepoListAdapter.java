package com.frankhon.jgithubbrowsersample.ui.common;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.frankhon.jgithubbrowsersample.AppExecutors;
import com.frankhon.jgithubbrowsersample.R;
import com.frankhon.jgithubbrowsersample.vo.Repo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by Frank Hon on 2019-07-26 23:48.
 * E-mail: frank_hon@foxmail.com
 * <p>
 * A RecyclerView adapter for [Repo] class.
 */
public class RepoListAdapter extends ListAdapter<Repo, BaseViewHolder<Repo>> {

    private boolean showFullName = true;
    private OnRepoClickListener onRepoClickListener;

    public RepoListAdapter(AppExecutors appExecutors) {
        super(new AsyncDifferConfig.Builder<>(
                new DiffUtil.ItemCallback<Repo>() {
                    @Override
                    public boolean areItemsTheSame(@NonNull Repo oldItem, @NonNull Repo newItem) {
                        return oldItem.getOwner().equals(newItem.getOwner()) && oldItem.getName().equals(newItem.getName());
                    }

                    @Override
                    public boolean areContentsTheSame(@NonNull Repo oldItem, @NonNull Repo newItem) {
                        return oldItem.getDescription() != null && oldItem.getDescription().equals(newItem.getDescription()) &&
                                oldItem.getStars() == newItem.getStars();
                    }
                }
        )
                .setBackgroundThreadExecutor(appExecutors.getDiskIO())
                .build());
    }

    @NonNull
    @Override
    public BaseViewHolder<Repo> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.repo_item, parent, false);
        return new RepoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder<Repo> holder, int position) {
        holder.bindView(getItem(position));
    }

    public void setShowFullName(boolean showFullName) {
        this.showFullName = showFullName;
    }

    public void setOnRepoClickListener(OnRepoClickListener onRepoClickListener) {
        this.onRepoClickListener = onRepoClickListener;
    }

    class RepoViewHolder extends BaseViewHolder<Repo> {

        @BindView(R.id.repo_card)
        CardView repoCard;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.desc)
        TextView desc;
        @BindView(R.id.stars)
        TextView stars;

        RepoViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        public void bindView(Repo item) {
            if (showFullName) {
                name.setText(item.getFullName());
            } else {
                name.setText(item.getName());
            }
            desc.setText(item.getDescription());
            stars.setText(String.valueOf(item.getStars()));
            if (onRepoClickListener != null) {
                repoCard.setOnClickListener(v -> onRepoClickListener.onClick(item));
            }
        }
    }

    public interface OnRepoClickListener {
        void onClick(Repo repo);
    }
}
