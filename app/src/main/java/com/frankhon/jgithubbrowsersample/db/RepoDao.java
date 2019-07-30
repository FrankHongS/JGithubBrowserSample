package com.frankhon.jgithubbrowsersample.db;

import android.util.SparseIntArray;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.frankhon.jgithubbrowsersample.vo.Contributor;
import com.frankhon.jgithubbrowsersample.vo.Repo;
import com.frankhon.jgithubbrowsersample.vo.RepoSearchResult;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Frank_Hon on 7/23/2019.
 * E-mail: v-shhong@microsoft.com
 */
@Dao
public abstract class RepoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(Repo... repo);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertContributors(List<Contributor> contributors);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertRepos(List<Repo> repositories);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(RepoSearchResult result);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract long createRepoIFNotExist(Repo repo);

    @Query("SELECT login,imageUrl,repoName,repoOwner,contributors FROM contributor WHERE repoName=:name AND repoOwner=:owner ORDER BY contributors DESC")
    public abstract LiveData<List<Contributor>> loadContributors(String owner, String name);

    @Query("SELECT * FROM Repo WHERE owner_login = :owner ORDER BY stars DESC")
    public abstract LiveData<List<Repo>> loadRepositories(String owner);

    @Query("SELECT * FROM RepoSearchResult WHERE `query` = :query")
    public abstract LiveData<RepoSearchResult> search(String query);

    @Query("SELECT * FROM repo WHERE owner_login = :ownerLogin AND name = :name")
    public abstract LiveData<Repo> load(String ownerLogin, String name);

    public LiveData<List<Repo>> loadOrdered(List<Integer> repoIds) {
        if (repoIds == null) {
            return null;
        }
        SparseIntArray order = new SparseIntArray();
        for (int i = 0; i < repoIds.size(); i++) {
            order.put(repoIds.get(i), i);
        }

        return Transformations.map(loadById(repoIds), repositories -> {
            Collections.sort(repositories, (r1, r2) -> {
                int pos1 = order.get(r1.getId());
                int pos2 = order.get(r2.getId());
                return pos1 - pos2;
            });
            return repositories;
        });
    }

    @Query("SELECT * FROM Repo WHERE id in (:repoIds)")
    protected abstract LiveData<List<Repo>> loadById(List<Integer> repoIds);

    @Query("SELECT * FROM RepoSearchResult WHERE `query` = :query")
    public abstract RepoSearchResult findSearchResult(String query);
}
