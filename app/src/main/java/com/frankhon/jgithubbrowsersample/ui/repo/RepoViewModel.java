package com.frankhon.jgithubbrowsersample.ui.repo;

import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.frankhon.jgithubbrowsersample.repository.RepoRepository;
import com.frankhon.jgithubbrowsersample.util.AbsentLiveData;
import com.frankhon.jgithubbrowsersample.vo.Contributor;
import com.frankhon.jgithubbrowsersample.vo.Repo;
import com.frankhon.jgithubbrowsersample.vo.Resource;

import java.util.List;
import java.util.Objects;

/**
 * Created by Frank Hon on 2019-07-28 20:21.
 * E-mail: frank_hon@foxmail.com
 */
public class RepoViewModel extends ViewModel {

    private MutableLiveData<RepoId> repoId = new MutableLiveData<>();

    private LiveData<Resource<List<Contributor>>> contributors;
    private LiveData<Resource<Repo>> repo;

    @SuppressWarnings("Convert2MethodRef")
    public RepoViewModel(RepoRepository repository) {
        contributors = Transformations.switchMap(repoId, input ->
                input.ifExists((owner, name) -> repository.loadContributors(owner, name)));

        repo = Transformations.switchMap(repoId, input ->
                input.ifExists((owner, name) -> repository.loadRepo(owner, name)));
    }

    public LiveData<Resource<Repo>> getRepo() {
        return repo;
    }

    public LiveData<Resource<List<Contributor>>> getContributors() {
        return contributors;
    }

    public void retry() {
        RepoId value = repoId.getValue();

        if (value != null) {
            String owner = value.getOwner();
            String name = value.getName();
            if (owner != null && name != null) {
                repoId.setValue(new RepoId(owner, name));
            }
        }
    }

    public void setId(String owner, String name) {
        RepoId update = new RepoId(owner, name);

        RepoId old = repoId.getValue();
        if (!update.equals(old)) {
            repoId.setValue(update);
        }
    }

    class RepoId {
        private String owner;
        private String name;

        RepoId(String owner, String name) {
            this.owner = owner;
            this.name = name;
        }

        public String getOwner() {
            return owner;
        }

        public String getName() {
            return name;
        }

        @Override
        public boolean equals(@Nullable Object obj) {
            if (this == obj) {
                return true;
            }

            if (obj instanceof RepoId) {
                RepoId other = (RepoId) obj;
                return Objects.equals(owner, other.getOwner()) &&
                        Objects.equals(name, other.getName());
            }

            return false;
        }

        <T> LiveData<T> ifExists(Function<T> function) {
            if (TextUtils.isEmpty(owner) || TextUtils.isEmpty(name)) {
                return AbsentLiveData.create();
            } else {
                return function.apply(owner, name);
            }
        }
    }

    interface Function<T> {

        LiveData<T> apply(String owner, String name);

    }
}
