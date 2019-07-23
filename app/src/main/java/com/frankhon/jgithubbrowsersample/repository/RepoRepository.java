package com.frankhon.jgithubbrowsersample.repository;

import com.frankhon.jgithubbrowsersample.AppExecutors;
import com.frankhon.jgithubbrowsersample.db.GithubDb;
import com.frankhon.jgithubbrowsersample.db.RepoDao;

/**
 * Created by Frank_Hon on 7/23/2019.
 * E-mail: v-shhong@microsoft.com
 */
public final class RepoRepository {

    private final AppExecutors appExecutors;
    private final GithubDb db;
    private final RepoDao repoDao;

    public RepoRepository(AppExecutors appExecutors, GithubDb db, RepoDao repoDao) {
        this.appExecutors = appExecutors;
        this.db = db;
        this.repoDao = repoDao;
    }
}
