package com.frankhon.jgithubbrowsersample.vo;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Frank Hon on 2019-07-28 19:53.
 * E-mail: frank_hon@foxmail.com
 */
@Entity(
        primaryKeys = {"login","repoName","repoOwner"},
        foreignKeys = {
                @ForeignKey(
                        entity = Repo.class,
                        parentColumns = {"name","owner_login"},
                        childColumns = {"repoName","repoOwner"},
                        onUpdate = ForeignKey.CASCADE,
                        deferred = true
                )
        }
)
public class Contributor {

    @NonNull
    @SerializedName("login")
    private String login;

    @SerializedName("contributors")
    private int contributors;

    @SerializedName("image_url")
    private String imageUrl;

    // does not show up in the response but set in post processing.
    @NonNull
    private String repoName = "";

    // does not show up in the response but set in post processing.
    @NonNull
    private String repoOwner ="";

    public Contributor(@NonNull String login, int contributors, String imageUrl) {
        this.login = login;
        this.contributors = contributors;
        this.imageUrl = imageUrl;
    }

    @NonNull
    public String getLogin() {
        return login;
    }

    public int getContributors() {
        return contributors;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    @NonNull
    public String getRepoName() {
        return repoName;
    }

    public void setRepoName(@NonNull String repoName) {
        this.repoName = repoName;
    }

    @NonNull
    public String getRepoOwner() {
        return repoOwner;
    }

    public void setRepoOwner(@NonNull String repoOwner) {
        this.repoOwner = repoOwner;
    }
}
