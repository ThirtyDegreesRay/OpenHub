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

import com.thirtydegreesray.openhub.inject.FragmentScope;
import com.thirtydegreesray.openhub.inject.module.FragmentModule;
import com.thirtydegreesray.openhub.ui.fragment.ActivityFragment;
import com.thirtydegreesray.openhub.ui.fragment.CommitFilesFragment;
import com.thirtydegreesray.openhub.ui.fragment.CommitsFragment;
import com.thirtydegreesray.openhub.ui.fragment.IssueTimelineFragment;
import com.thirtydegreesray.openhub.ui.fragment.IssuesFragment;
import com.thirtydegreesray.openhub.ui.fragment.ProfileInfoFragment;
import com.thirtydegreesray.openhub.ui.fragment.ReleasesFragment;
import com.thirtydegreesray.openhub.ui.fragment.RepoFilesFragment;
import com.thirtydegreesray.openhub.ui.fragment.RepoInfoFragment;
import com.thirtydegreesray.openhub.ui.fragment.RepositoriesFragment;
import com.thirtydegreesray.openhub.ui.fragment.UserListFragment;
import com.thirtydegreesray.openhub.ui.fragment.ViewerFragment;

import dagger.Component;

/**
 * Created on 2017/7/18.
 *
 * @author ThirtyDegreesRay
 */

@FragmentScope
@Component(modules = FragmentModule.class, dependencies = AppComponent.class)
public interface FragmentComponent {
    void inject(RepositoriesFragment fragment);
    void inject(RepoInfoFragment fragment);
    void inject(RepoFilesFragment fragment);
    void inject(UserListFragment fragment);
    void inject(ViewerFragment fragment);
    void inject(ProfileInfoFragment fragment);
    void inject(ActivityFragment fragment);
    void inject(ReleasesFragment fragment);
    void inject(IssuesFragment fragment);
    void inject(IssueTimelineFragment fragment);
    void inject(CommitsFragment fragment);
    void inject(CommitFilesFragment fragment);
}
