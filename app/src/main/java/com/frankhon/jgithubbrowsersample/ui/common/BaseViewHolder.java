package com.frankhon.jgithubbrowsersample.ui.common;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.ButterKnife;

/**
 * Created by Frank Hon on 2019-07-27 00:09.
 * E-mail: frank_hon@foxmail.com
 */
public abstract class BaseViewHolder<T> extends RecyclerView.ViewHolder {
    public BaseViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public abstract void bindView(T item);
}
