package com.frankhon.jgithubbrowsersample.vo;

import androidx.room.Entity;
import androidx.room.TypeConverters;

import com.frankhon.jgithubbrowsersample.db.GithubTypeConverters;

import java.util.List;

/**
 * Created by Frank_Hon on 7/26/2019.
 * E-mail: v-shhong@microsoft.com
 */
@Entity(primaryKeys = {"query"})
@TypeConverters(GithubTypeConverters.class)
public class RepoSearchResult {

    private String query;

    private List<Integer> repoIds;

    private int totalCount;

    private int next;

    public RepoSearchResult(String query, List<Integer> repoIds, int totalCount, int next) {
        this.query = query;
        this.repoIds = repoIds;
        this.totalCount = totalCount;
        this.next = next;
    }

    public String getQuery() {
        return query;
    }

    public List<Integer> getRepoIds() {
        return repoIds;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public int getNext() {
        return next;
    }
}
