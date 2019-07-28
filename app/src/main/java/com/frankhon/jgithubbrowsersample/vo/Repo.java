package com.frankhon.jgithubbrowsersample.vo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Index;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

/**
 * Created by Frank_Hon on 7/23/2019.
 * E-mail: v-shhong@microsoft.com
 */
@Entity(
        indices = {
                @Index("id"),
                @Index("owner_login")
        },
        primaryKeys = {"name", "owner_login"}
)
public class Repo {

    public static final int UNKNOWN_ID = -1;

    private int id;

    @NonNull
    @SerializedName("name")
    private String name;

    @SerializedName("full_name")
    private String fullName;

    @SerializedName("description")
    private String description;

    @NonNull
    @SerializedName("owner")
    @Embedded(prefix = "owner_")
    private Owner owner;

    @SerializedName("stargazers_count")
    private int stars;

    public Repo(int id, @NonNull String name, String fullName, String description, @NonNull Owner owner, int stars) {
        this.id = id;
        this.name = name;
        this.fullName = fullName;
        this.description = description;
        this.owner = owner;
        this.stars = stars;
    }

    public int getId() {
        return id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public String getFullName() {
        return fullName;
    }

    public String getDescription() {
        return description;
    }

    @NonNull
    public Owner getOwner() {
        return owner;
    }

    public int getStars() {
        return stars;
    }

    @Override
    public String toString() {
        return "Repo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", fullName='" + fullName + '\'' +
                ", description='" + description + '\'' +
                ", owner=" + owner +
                ", stars=" + stars +
                '}';
    }

    public static class Owner {
        @NonNull
        @SerializedName("login")
        private String login;

        @SerializedName("url")
        private String url;

        public Owner(@NonNull String login, String url) {
            this.login = login;
            this.url = url;
        }

        @NonNull
        public String getLogin() {
            return login;
        }

        public String getUrl() {
            return url;
        }

        @Override
        public String toString() {
            return "Owner{" +
                    "login='" + login + '\'' +
                    ", url='" + url + '\'' +
                    '}';
        }

        @Override
        public boolean equals(@Nullable Object obj) {
            if (this == obj) {
                return true;
            }

            if (obj instanceof Owner) {
                Owner other = (Owner) obj;
                return Objects.equals(login, other.getLogin()) &&
                        Objects.equals(url, other.getUrl());
            }

            return false;
        }
    }
}
