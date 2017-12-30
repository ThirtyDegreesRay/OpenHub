

package com.thirtydegreesray.openhub.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.thirtydegreesray.dataautoaccess.annotation.AutoAccess;
import com.thirtydegreesray.openhub.R;
import com.thirtydegreesray.openhub.mvp.contract.base.IBaseContract;
import com.thirtydegreesray.openhub.ui.activity.base.SingleFragmentActivity;
import com.thirtydegreesray.openhub.ui.fragment.UserListFragment;
import com.thirtydegreesray.openhub.util.BundleHelper;
import com.thirtydegreesray.openhub.util.StringUtils;

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
        intent.putExtras(BundleHelper.builder()
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
        String title = getListTitle();
        String subTitle = StringUtils.isBlank(repo) ? user : user.concat("/").concat(repo);
        setToolbarTitle(title, subTitle);
        setToolbarScrollAble(true);
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
