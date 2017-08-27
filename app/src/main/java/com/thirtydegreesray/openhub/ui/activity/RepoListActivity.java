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

package com.thirtydegreesray.openhub.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.thirtydegreesray.dataautoaccess.annotation.AutoAccess;
import com.thirtydegreesray.openhub.R;
import com.thirtydegreesray.openhub.inject.component.AppComponent;
import com.thirtydegreesray.openhub.ui.activity.base.BaseActivity;
import com.thirtydegreesray.openhub.ui.fragment.RepositoriesFragment;
import com.thirtydegreesray.openhub.util.BundleBuilder;

/**
 * Created by ThirtyDegreesRay on 2017/8/23 18:15:40
 */

public class RepoListActivity extends BaseActivity {

    public static void show(@NonNull Context context,
                            @NonNull RepositoriesFragment.RepositoriesType type,
                            @NonNull String user){
        Intent intent = new Intent(context, RepoListActivity.class);
        intent.putExtras(BundleBuilder.builder().put("type", type).put("user", user).build());
        context.startActivity(intent);
    }

    public static void showForks(@NonNull Context context,
                            @NonNull String user, @NonNull String repo){
        Intent intent = new Intent(context, RepoListActivity.class);
        intent.putExtras(BundleBuilder.builder()
                .put("type", RepositoriesFragment.RepositoriesType.FORKS)
                .put("user", user)
                .put("repo", repo)
                .build());
        context.startActivity(intent);
    }

    @AutoAccess RepositoriesFragment.RepositoriesType type;
    @AutoAccess String user;
    @AutoAccess String repo;

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }

    @Nullable
    @Override
    protected int getContentView() {
        return R.layout.activity_single_fragment;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        setToolbarBackEnable();
        setToolbarTitle(getListTitle());
        RepositoriesFragment fragment = RepositoriesFragment.RepositoriesType.FORKS.equals(type) ?
                RepositoriesFragment.createForForks(user, repo) :
                RepositoriesFragment.create(type, user);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, fragment)
                .commit();
    }

    private String getListTitle(){
        if(type.equals(RepositoriesFragment.RepositoriesType.OWNED)){
            return getString(R.string.owned);
        }else if(type.equals(RepositoriesFragment.RepositoriesType.STARRED)){
            return getString(R.string.starred);
        }else if(type.equals(RepositoriesFragment.RepositoriesType.FORKS)){
            return getString(R.string.forks);
        }
        return getString(R.string.repositories);
    }
}
