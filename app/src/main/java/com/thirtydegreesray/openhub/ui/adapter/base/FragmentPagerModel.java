/*
 *    Copyright 2017 ThirtyDegressRay
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.thirtydegreesray.openhub.ui.adapter.base;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.thirtydegreesray.openhub.R;
import com.thirtydegreesray.openhub.mvp.model.Repository;
import com.thirtydegreesray.openhub.mvp.model.SearchModel;
import com.thirtydegreesray.openhub.mvp.model.User;
import com.thirtydegreesray.openhub.ui.fragment.ActivityFragment;
import com.thirtydegreesray.openhub.ui.fragment.ProfileInfoFragment;
import com.thirtydegreesray.openhub.ui.fragment.RepoFilesFragment;
import com.thirtydegreesray.openhub.ui.fragment.RepoInfoFragment;
import com.thirtydegreesray.openhub.ui.fragment.RepositoriesFragment;
import com.thirtydegreesray.openhub.ui.fragment.UserListFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ThirtyDegreesRay on 2017/8/15 21:10:14
 */

public class FragmentPagerModel {

    private String title;
    private Fragment fragment;

    public FragmentPagerModel(String title, Fragment fragment) {
        this.title = title;
        this.fragment = fragment;
    }

    public String getTitle() {
        return title;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public static List<FragmentPagerModel> createRepoPagerList(Context context, Repository repository){
        return Arrays.asList(
                new FragmentPagerModel(context.getString(R.string.info), RepoInfoFragment.create(repository)),
                new FragmentPagerModel(context.getString(R.string.files), RepoFilesFragment.create(repository)),
                new FragmentPagerModel(context.getString(R.string.activity),
                        ActivityFragment.create(ActivityFragment.ActivityType.Repository,
                                repository.getOwner().getLogin(), repository.getName()))
//                new FragmentPagerModel(context.getString(R.string.commits), new ProfileFragment().setName("profile"))
        );
    }

    public static List<FragmentPagerModel> createProfilePagerList(Context context, User user){
        return Arrays.asList(
                new FragmentPagerModel(context.getString(R.string.info), ProfileInfoFragment.create(user)),
                new FragmentPagerModel(context.getString(R.string.activity),
                        ActivityFragment.create(ActivityFragment.ActivityType.User, user.getLogin())),
                new FragmentPagerModel(context.getString(R.string.starred),
                        RepositoriesFragment.create(RepositoriesFragment.RepositoriesType.STARRED, user.getLogin()))
        );
    }

    public static List<FragmentPagerModel> createSearchPagerList(
            @NonNull Context context, @NonNull ArrayList<SearchModel> searchModels){
        return Arrays.asList(
                new FragmentPagerModel(context.getString(R.string.repositories),
                        RepositoriesFragment.createForSearch(searchModels.get(0))),
                new FragmentPagerModel(context.getString(R.string.users),
                        UserListFragment.createForSearch(searchModels.get(1)))
        );
    }

    public static List<FragmentPagerModel> createTrendingPagerList(
            @NonNull Context context){
        return Arrays.asList(
                new FragmentPagerModel(context.getString(R.string.daily),
                        RepositoriesFragment.createForTrending("daily")),
                new FragmentPagerModel(context.getString(R.string.weekly),
                        RepositoriesFragment.createForTrending("weekly")),
                new FragmentPagerModel(context.getString(R.string.monthly),
                        RepositoriesFragment.createForTrending("monthly"))
        );
    }

}
