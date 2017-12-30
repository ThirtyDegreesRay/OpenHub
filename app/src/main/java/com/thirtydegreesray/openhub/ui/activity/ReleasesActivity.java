package com.thirtydegreesray.openhub.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.thirtydegreesray.dataautoaccess.annotation.AutoAccess;
import com.thirtydegreesray.openhub.R;
import com.thirtydegreesray.openhub.mvp.contract.base.IBaseContract;
import com.thirtydegreesray.openhub.ui.activity.base.SingleFragmentActivity;
import com.thirtydegreesray.openhub.ui.fragment.ReleasesFragment;
import com.thirtydegreesray.openhub.util.BundleHelper;

/**
 * Created by ThirtyDegreesRay on 2017/9/16 10:58:03
 */

public class ReleasesActivity extends SingleFragmentActivity<IBaseContract.Presenter, ReleasesFragment> {

    public static void show(Activity activity, String owner, String repo) {
        Intent intent = createIntent(activity, owner, repo);
        activity.startActivity(intent);
    }

    public static Intent createIntent(Activity activity, String owner, String repo) {
        return new Intent(activity, ReleasesActivity.class)
                .putExtras(BundleHelper.builder()
                        .put("owner", owner)
                        .put("repo", repo).build());
    }

    @AutoAccess String owner;
    @AutoAccess String repo;

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        String subTitle = owner.concat("/").concat(repo);
        setToolbarTitle(getString(R.string.releases), subTitle);
        setToolbarScrollAble(true);
    }

    @Override
    protected ReleasesFragment createFragment() {
        return ReleasesFragment.create(owner, repo);
    }
}
