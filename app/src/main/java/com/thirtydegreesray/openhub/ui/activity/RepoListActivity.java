

package com.thirtydegreesray.openhub.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.view.Menu;
import android.view.MenuItem;

import com.thirtydegreesray.dataautoaccess.annotation.AutoAccess;
import com.thirtydegreesray.openhub.AppConfig;
import com.thirtydegreesray.openhub.R;
import com.thirtydegreesray.openhub.mvp.contract.base.IBaseContract;
import com.thirtydegreesray.openhub.mvp.model.Collection;
import com.thirtydegreesray.openhub.mvp.model.Topic;
import com.thirtydegreesray.openhub.mvp.model.filter.RepositoriesFilter;
import com.thirtydegreesray.openhub.ui.activity.base.SingleFragmentActivity;
import com.thirtydegreesray.openhub.ui.fragment.RepositoriesFragment;
import com.thirtydegreesray.openhub.ui.fragment.base.OnDrawerSelectedListener;
import com.thirtydegreesray.openhub.util.AppOpener;
import com.thirtydegreesray.openhub.util.BundleHelper;
import com.thirtydegreesray.openhub.util.StringUtils;

/**
 * Created by ThirtyDegreesRay on 2017/8/23 18:15:40
 */

public class RepoListActivity extends SingleFragmentActivity<IBaseContract.Presenter, RepositoriesFragment> {

    public static void show(@NonNull Context context,
                            @NonNull RepositoriesFragment.RepositoriesType type,
                            @NonNull String user){
        Intent intent = new Intent(context, RepoListActivity.class);
        intent.putExtras(BundleHelper.builder().put("type", type).put("user", user).build());
        context.startActivity(intent);
    }

    public static void showCollection(@NonNull Context context, @NonNull Collection collection){
        Intent intent = new Intent(context, RepoListActivity.class);
        intent.putExtras(BundleHelper.builder()
                .put("type", RepositoriesFragment.RepositoriesType.COLLECTION)
                .put("collection", collection)
                .build());
        context.startActivity(intent);
    }

    public static void showTopic(@NonNull Context context, @NonNull Topic topic){
        Intent intent = new Intent(context, RepoListActivity.class);
        intent.putExtras(BundleHelper.builder()
                .put("type", RepositoriesFragment.RepositoriesType.TOPIC)
                .put("topic", topic)
                .build());
        context.startActivity(intent);
    }

    public static void showForks(@NonNull Context context,
                            @NonNull String user, @NonNull String repo){
        Intent intent = new Intent(context, RepoListActivity.class);
        intent.putExtras(BundleHelper.builder()
                .put("type", RepositoriesFragment.RepositoriesType.FORKS)
                .put("user", user)
                .put("repo", repo)
                .build());
        context.startActivity(intent);
    }

    @AutoAccess RepositoriesFragment.RepositoriesType type;
    @AutoAccess String user;
    @AutoAccess String repo;
    @AutoAccess Collection collection;
    @AutoAccess Topic topic;

    private OnDrawerSelectedListener listener;

    @Override
    protected int getContentView() {
        return R.layout.activity_single_fragment_with_drawer;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_open_in_browser){
            String url = null;
            if(RepositoriesFragment.RepositoriesType.COLLECTION.equals(type)){
                url = AppConfig.GITHUB_BASE_URL.concat("collections/").concat(collection.getId());
            } else if(RepositoriesFragment.RepositoriesType.TOPIC.equals(type)){
                url = AppConfig.GITHUB_BASE_URL.concat("topics/").concat(topic.getId());
            }
            if(url != null){
                AppOpener.openInCustomTabsOrBrowser(getActivity(), url);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected RepositoriesFragment getFragment() {
        return super.getFragment();
    }

    @Override
    protected void onNavItemSelected(@NonNull MenuItem item, boolean isStartDrawer) {
        super.onNavItemSelected(item, isStartDrawer);
        listener.onDrawerSelected(navViewEnd, item);
    }

    @Override
    protected boolean isEndDrawerMultiSelect() {
        return true;
    }

    @Override
    protected int getEndDrawerToggleMenuItemId() {
        return R.id.nav_sort;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(isFilterEnable()){
            getMenuInflater().inflate(R.menu.menu_sort, menu);
        } else if(RepositoriesFragment.RepositoriesType.COLLECTION.equals(type)
                || RepositoriesFragment.RepositoriesType.TOPIC.equals(type)){
            getMenuInflater().inflate(R.menu.menu_open_in_browser, menu);
        }
        return true;
    }



    @Override
    protected void initActivity() {
        super.initActivity();
        setEndDrawerEnable(isFilterEnable());
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        String title = getListTitle();
        String subTitle = StringUtils.isBlank(repo) ? user : user.concat("/").concat(repo);
        setToolbarTitle(title, subTitle);
        intiFilter();
        setToolbarScrollAble(true);
    }

    @Override
    protected RepositoriesFragment createFragment() {
        RepositoriesFragment fragment;
        if(RepositoriesFragment.RepositoriesType.COLLECTION.equals(type)){
            fragment = RepositoriesFragment.createForCollection(collection);
        } else if(RepositoriesFragment.RepositoriesType.TOPIC.equals(type)){
            fragment = RepositoriesFragment.createForTopic(topic);
        } else {
            fragment = RepositoriesFragment.RepositoriesType.FORKS.equals(type) ?
                    RepositoriesFragment.createForForks(user, repo) :
                    RepositoriesFragment.create(type, user);
            listener = fragment;
        }
        return fragment;
    }

    private String getListTitle(){
        if(type.equals(RepositoriesFragment.RepositoriesType.PUBLIC)){
            return getString(R.string.public_repositories);
        }else if(type.equals(RepositoriesFragment.RepositoriesType.STARRED)){
            return getString(R.string.starred_repositories);
        }else if(type.equals(RepositoriesFragment.RepositoriesType.FORKS)){
            return getString(R.string.forks);
        }else if(type.equals(RepositoriesFragment.RepositoriesType.COLLECTION)){
            return collection.getName();
        }else if(type.equals(RepositoriesFragment.RepositoriesType.TOPIC)){
            return topic.getName();
        }
        return getString(R.string.repositories);
    }

    private boolean isFilterEnable(){
        return RepositoriesFragment.RepositoriesType.OWNED.equals(type) ||
                RepositoriesFragment.RepositoriesType.PUBLIC.equals(type);
    }

    private void intiFilter(){
        if(isFilterEnable()){
            updateEndDrawerContent(R.menu.menu_repositories_filter);
            RepositoriesFilter.initDrawer(navViewEnd, type);
        }
    }

}
