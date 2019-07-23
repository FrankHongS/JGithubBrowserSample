package com.frankhon.jgithubbrowsersample.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.frankhon.jgithubbrowsersample.vo.User;

/**
 * Created by Frank_Hon on 7/23/2019.
 * E-mail: v-shhong@microsoft.com
 */
@Database(
        entities = {User.class},
        version = 1,
        exportSchema = false
)
public abstract class GithubDb extends RoomDatabase {

    private static volatile GithubDb INSTANCE;

    public static GithubDb getInstance(Context context){
        if(INSTANCE==null){
            synchronized (GithubDb.class){
                if(INSTANCE==null){
                    INSTANCE= Room
                            .databaseBuilder(context.getApplicationContext(),
                                    GithubDb.class,
                                    "github.db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }

        return INSTANCE;
    }

    public abstract UserDao userDao();

}
