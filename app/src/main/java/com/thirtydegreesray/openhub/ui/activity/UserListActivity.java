/*
 *    Copyright 2017 ThirtyDegreesRay
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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.thirtydegreesray.dataautoaccess.annotation.AutoAccess;
import com.thirtydegreesray.openhub.R;
import com.thirtydegreesray.openhub.mvp.contract.base.IBaseContract;
import com.thirtydegreesray.openhub.ui.activity.base.SingleFragmentActivity;
import com.thirtydegreesray.openhub.ui.fragment.UserListFragment;
import com.thirtydegreesray.openhub.util.BundleBuilder;

/**
 * Created by ThirtyDegreesRay on 2017/8/16 17:22:44
 */

public class UserListActivity extends SingleFragmentActivity<IBaseContract.Presenter, UserListFragment> {

    public static void show(Activity context, UserListFragment.UserListType type,
                            String user){
        show(context, type, user, null);
    }

    public static void show(Activity context, UserListFragment.UserListType type,
                            String user, String repo){
        Intent intent = new Intent(context, UserListActivity.class);
        intent.putExtras(BundleBuilder.builder()
                .put("type", type)
                .put("user", user)
                .put("repo", repo)
                .build());
        context.startActivity(intent);
    }

    @AutoAccess UserListFragment.UserListType type;
    @AutoAccess String user;
    @AutoAccess String repo;

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        setToolbarTitle(getListTitle());
    }

    @Override
    protected UserListFragment createFragment() {
        return UserListFragment.create(type, user, repo);
    }

    private String getListTitle(){
        if(type.equals(UserListFragment.UserListType.STARGAZERS)){
            return getString(R.string.stargazers);
        }else if(type.equals(UserListFragment.UserListType.WATCHERS)){
            return getString(R.string.watchers);
        }else if(type.equals(UserListFragment.UserListType.FOLLOWERS)){
            return getString(R.string.followers);
        }else if(type.equals(UserListFragment.UserListType.FOLLOWING)){
            return getString(R.string.following);
        }else if(type.equals(UserListFragment.UserListType.ORG_MEMBERS)){
            return getString(R.string.members);
        }
        return getString(R.string.users);
    }
}
