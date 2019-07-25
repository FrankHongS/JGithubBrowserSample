package com.frankhon.jgithubbrowsersample.api;

import com.frankhon.jgithubbrowsersample.vo.Repo;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Frank_Hon on 7/25/2019.
 * E-mail: v-shhong@microsoft.com
 */
public class RepoSearchResponse {

    @SerializedName("total_count")
    private int total;

    @SerializedName("items")
    private List<Repo> items;

    private int nextPage;

    public RepoSearchResponse(int total, List<Repo> items) {
        this.total = total;
        this.items = items;
    }

    public int getTotal() {
        return total;
    }

    public List<Repo> getItems() {
        return items;
    }

    public int getNextPage() {
        return nextPage;
    }

    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }
}
