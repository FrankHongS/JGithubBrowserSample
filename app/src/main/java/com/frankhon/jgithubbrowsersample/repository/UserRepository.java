package com.frankhon.jgithubbrowsersample.repository;

import androidx.lifecycle.LiveData;

import com.frankhon.jgithubbrowsersample.AppExecutors;
import com.frankhon.jgithubbrowsersample.api.ApiResponseUtil;
import com.frankhon.jgithubbrowsersample.api.GithubServiceImpl;
import com.frankhon.jgithubbrowsersample.db.UserDao;
import com.frankhon.jgithubbrowsersample.vo.Resource;
import com.frankhon.jgithubbrowsersample.vo.User;

/**
 * Created by Frank_Hon on 7/31/2019.
 * E-mail: v-shhong@microsoft.com
 */
public final class UserRepository {

    private static volatile UserRepository INSTANCE;

    private final AppExecutors appExecutors;
    private final UserDao userDao;
    private final GithubServiceImpl githubService;

    private UserRepository(AppExecutors appExecutors, UserDao userDao, GithubServiceImpl githubService) {
        this.appExecutors = appExecutors;
        this.userDao = userDao;
        this.githubService = githubService;
    }

    public static UserRepository getInstance(AppExecutors appExecutors, UserDao userDao, GithubServiceImpl githubService){
        if (INSTANCE==null){
            synchronized (UserRepository.class){
                if (INSTANCE==null){
                    INSTANCE=new UserRepository(appExecutors, userDao, githubService);
                }
            }
        }

        return INSTANCE;
    }

    public LiveData<Resource<User>> loadUser(String login){
        return new NetworkBoundResource<User,User>(appExecutors){
            @Override
            protected void saveCallResult(User item) {
                userDao.insert(item);
            }

            @Override
            protected boolean shouldFetch(User data) {
                return data==null;
            }

            @Override
            protected LiveData<User> loadFromDb() {
                return userDao.findByLogin(login);
            }

            @Override
            protected LiveData<ApiResponseUtil.ApiResponse<User>> createCall() {
                return githubService.getUser(login);
            }
        }.asLiveData();
    }
}
