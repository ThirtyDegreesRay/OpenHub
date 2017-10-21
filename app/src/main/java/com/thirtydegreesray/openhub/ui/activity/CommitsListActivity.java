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
import android.support.annotation.NonNull;

import com.thirtydegreesray.dataautoaccess.annotation.AutoAccess;
import com.thirtydegreesray.openhub.R;
import com.thirtydegreesray.openhub.mvp.contract.base.IBaseContract;
import com.thirtydegreesray.openhub.ui.activity.base.SingleFragmentActivity;
import com.thirtydegreesray.openhub.ui.fragment.CommitsFragment;
import com.thirtydegreesray.openhub.util.BundleBuilder;

/**
 * Created by ThirtyDegreesRay on 2017/10/20 11:30:58
 */

public class CommitsListActivity extends SingleFragmentActivity<IBaseContract.Presenter, CommitsFragment> {

    public static void showForCompare(@NonNull Activity activity, @NonNull String user,
                                      @NonNull String repo, @NonNull String before, @NonNull String head){
        Intent intent = new Intent(activity, CommitsListActivity.class);
        intent.putExtras(BundleBuilder.builder().put("type", CommitsListActivity.CommitsListType.Compare)
                .put("user", user).put("repo", repo).put("before", before).put("head", head).build());
        activity.startActivity(intent);
    }

    public enum CommitsListType{
        Compare, Repo
    }

    @AutoAccess CommitsListActivity.CommitsListType type ;
    @AutoAccess String user ;
    @AutoAccess String repo ;

    @AutoAccess String before ;
    @AutoAccess String head ;

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        String repoFullName = user.concat("/").concat(repo);
        setToolbarTitle(getString(R.string.compare), repoFullName);
    }

    @Override
    protected CommitsFragment createFragment() {
        if(CommitsListType.Compare.equals(type)){
            return CommitsFragment.createForCompare(user, repo, before, head);
        }else{
            return null;
        }
    }


}
