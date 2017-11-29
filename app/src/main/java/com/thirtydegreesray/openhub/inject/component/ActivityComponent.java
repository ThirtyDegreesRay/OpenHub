

package com.thirtydegreesray.openhub.inject.component;

import com.thirtydegreesray.openhub.inject.ActivityScope;
import com.thirtydegreesray.openhub.inject.module.ActivityModule;
import com.thirtydegreesray.openhub.ui.activity.CommitDetailActivity;
import com.thirtydegreesray.openhub.ui.activity.EditIssueActivity;
import com.thirtydegreesray.openhub.ui.activity.IssueDetailActivity;
import com.thirtydegreesray.openhub.ui.activity.IssuesActivity;
import com.thirtydegreesray.openhub.ui.activity.LoginActivity;
import com.thirtydegreesray.openhub.ui.activity.MainActivity;
import com.thirtydegreesray.openhub.ui.activity.ProfileActivity;
import com.thirtydegreesray.openhub.ui.activity.ReleaseInfoActivity;
import com.thirtydegreesray.openhub.ui.activity.RepositoryActivity;
import com.thirtydegreesray.openhub.ui.activity.SearchActivity;
import com.thirtydegreesray.openhub.ui.activity.SettingsActivity;
import com.thirtydegreesray.openhub.ui.activity.SplashActivity;
import com.thirtydegreesray.openhub.ui.activity.TrendingActivity;

import dagger.Component;

/**
 * ActivityComponent
 * Created by ThirtyDegreesRay on 2016/8/30 14:56
 */
@ActivityScope
@Component(modules = ActivityModule.class, dependencies = AppComponent.class)
public interface ActivityComponent {
    void inject(SplashActivity activity);
    void inject(LoginActivity activity);
    void inject(MainActivity activity);
    void inject(SettingsActivity activity);
    void inject(RepositoryActivity activity);
    void inject(ProfileActivity activity);
    void inject(SearchActivity activity);
    void inject(ReleaseInfoActivity activity);
    void inject(IssuesActivity activity);
    void inject(IssueDetailActivity activity);
    void inject(EditIssueActivity activity);
    void inject(CommitDetailActivity activity);
    void inject(TrendingActivity activity);
}
