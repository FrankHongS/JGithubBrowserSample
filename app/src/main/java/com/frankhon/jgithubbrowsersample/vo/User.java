package com.frankhon.jgithubbrowsersample.vo;

import androidx.room.Entity;

import com.google.gson.annotations.SerializedName;
import androidx.annotation.NonNull;

/**
 * Created by Frank_Hon on 7/23/2019.
 * E-mail: v-shhong@microsoft.com
 */
@Entity(primaryKeys = "login")
public class User {

    @NonNull
    @SerializedName("login")
    private String login;

    @SerializedName("avatar_url")
    private String avatarUrl;

    @SerializedName("name")
    private String name;

    @SerializedName("company")
    private String company;

    @SerializedName("repos_url")
    private String reposUrl;

    @SerializedName("blog")
    private String blog;

    public User(String login, String avatarUrl, String name, String company, String reposUrl, String blog) {
        this.login = login;
        this.avatarUrl = avatarUrl;
        this.name = name;
        this.company = company;
        this.reposUrl = reposUrl;
        this.blog = blog;
    }

    public String getLogin() {
        return login;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getName() {
        return name;
    }

    public String getCompany() {
        return company;
    }

    public String getReposUrl() {
        return reposUrl;
    }

    public String getBlog() {
        return blog;
    }
}
