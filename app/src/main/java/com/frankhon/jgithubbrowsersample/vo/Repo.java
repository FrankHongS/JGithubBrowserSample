package com.frankhon.jgithubbrowsersample.vo;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Index;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Frank_Hon on 7/23/2019.
 * E-mail: v-shhong@microsoft.com
 */
@Entity(
        indices = {
                @Index("id"),
                @Index("owner_login")
        },
        primaryKeys = {"name","owner_login"}
)
public class Repo {

    public static final int UNKNOWN_ID = -1;

    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("full_name")
    private String fullName;

    @SerializedName("description")
    private String description;

    @SerializedName("owner")
    @Embedded(prefix = "owner_")
    private Owner owner;

    @SerializedName("stargazers_count")
    private int stars;

    public Repo(String name, String fullName, String description, Owner owner, int stars) {
        this.name = name;
        this.fullName = fullName;
        this.description = description;
        this.owner = owner;
        this.stars = stars;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getFullName() {
        return fullName;
    }

    public String getDescription() {
        return description;
    }

    public Owner getOwner() {
        return owner;
    }

    public int getStars() {
        return stars;
    }

    public static class Owner{
        @SerializedName("login")
        private String login;

        @SerializedName("url")
        private String url;

        public Owner(String login, String url) {
            this.login = login;
            this.url = url;
        }

        public String getLogin() {
            return login;
        }

        public String getUrl() {
            return url;
        }
    }
}
