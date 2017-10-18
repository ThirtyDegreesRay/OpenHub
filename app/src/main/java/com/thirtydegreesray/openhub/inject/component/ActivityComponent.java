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
}
