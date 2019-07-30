package com.frankhon.jgithubbrowsersample.ui.repo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.bumptech.glide.Glide;
import com.frankhon.jgithubbrowsersample.AppExecutors;
import com.frankhon.jgithubbrowsersample.R;
import com.frankhon.jgithubbrowsersample.ui.common.BaseViewHolder;
import com.frankhon.jgithubbrowsersample.vo.Contributor;

import java.util.Objects;

import butterknife.BindView;

/**
 * Created by Frank Hon on 2019-07-28 19:52.
 * E-mail: frank_hon@foxmail.com
 */
public class ContributorAdapter extends ListAdapter<Contributor, BaseViewHolder<Contributor>> {

    private OnContributorClickListener onContributorClickListener;

    public ContributorAdapter(AppExecutors appExecutors) {
        super(new AsyncDifferConfig.Builder<>(
                new DiffUtil.ItemCallback<Contributor>() {
                    @Override
                    public boolean areItemsTheSame(@NonNull Contributor oldItem, @NonNull Contributor newItem) {
                        return oldItem.getLogin().equals(newItem.getLogin());
                    }

                    @Override
                    public boolean areContentsTheSame(@NonNull Contributor oldItem, @NonNull Contributor newItem) {
                        return Objects.equals(oldItem.getImageUrl(), newItem.getImageUrl()) &&
                                oldItem.getContributors() == newItem.getContributors();
                    }
                }
        )
                .setBackgroundThreadExecutor(appExecutors.getDiskIO())
                .build());
    }

    @NonNull
    @Override
    public BaseViewHolder<Contributor> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contributor_item, parent, false);
        return new ContributorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder<Contributor> holder, int position) {
        holder.bindView(getItem(position));
    }

    public void setOnContributorClickListener(OnContributorClickListener onContributorClickListener) {
        this.onContributorClickListener = onContributorClickListener;
    }

    class ContributorViewHolder extends BaseViewHolder<Contributor> {

        @BindView(R.id.contributor_card)
        CardView contributorCard;
        @BindView(R.id.iv_avatar)
        ImageView avatar;
        @BindView(R.id.tv_login)
        TextView login;

        ContributorViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        public void bindView(Contributor item) {
            Glide.with(itemView.getContext())
                    .load(item.getImageUrl())
                    .into(avatar);

            login.setText(item.getLogin());

            contributorCard.setOnClickListener(v -> {
                if (onContributorClickListener != null) {
                    onContributorClickListener.onClick(item, avatar);
                }
            });
        }
    }

    interface OnContributorClickListener {
        void onClick(Contributor contributor, ImageView imageView);
    }
}
