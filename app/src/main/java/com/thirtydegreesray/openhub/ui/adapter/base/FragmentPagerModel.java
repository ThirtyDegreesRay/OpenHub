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

package com.thirtydegreesray.openhub.ui.adapter.base;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.thirtydegreesray.openhub.R;
import com.thirtydegreesray.openhub.mvp.model.Issue;
import com.thirtydegreesray.openhub.mvp.model.Repository;
import com.thirtydegreesray.openhub.mvp.model.SearchModel;
import com.thirtydegreesray.openhub.mvp.model.User;
import com.thirtydegreesray.openhub.ui.fragment.ActivityFragment;
import com.thirtydegreesray.openhub.ui.fragment.CommitsFragment;
import com.thirtydegreesray.openhub.ui.fragment.IssuesFragment;
import com.thirtydegreesray.openhub.ui.fragment.MarkdownEditorFragment;
import com.thirtydegreesray.openhub.ui.fragment.MarkdownPreviewFragment;
import com.thirtydegreesray.openhub.ui.fragment.ProfileInfoFragment;
import com.thirtydegreesray.openhub.ui.fragment.RepoFilesFragment;
import com.thirtydegreesray.openhub.ui.fragment.RepoInfoFragment;
import com.thirtydegreesray.openhub.ui.fragment.RepositoriesFragment;
import com.thirtydegreesray.openhub.ui.fragment.UserListFragment;
import com.thirtydegreesray.openhub.ui.fragment.base.BaseFragment;
import com.thirtydegreesray.openhub.util.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ThirtyDegreesRay on 2017/8/15 21:10:14
 */

public class FragmentPagerModel {

    private String title;
    private BaseFragment fragment;

    public FragmentPagerModel(String title, BaseFragment fragment) {
        this.title = title;
        this.fragment = fragment;
    }

    public String getTitle() {
        return title;
    }

    public BaseFragment getFragment() {
        return fragment;
    }

    public static List<FragmentPagerModel> createRepoPagerList(@NonNull Context context
            , @NonNull final Repository repository, @NonNull ArrayList<Fragment> fragments) {

        return setPagerFragmentFlag(Arrays.asList(
                new FragmentPagerModel(context.getString(R.string.info),
                        getFragment(fragments, RepoInfoFragment.class, new FragmentCreator() {
                    @Override
                    public Fragment createFragment() {
                        return RepoInfoFragment.create(repository);
                    }
                })),
                new FragmentPagerModel(context.getString(R.string.files),
                        getFragment(fragments, RepoInfoFragment.class, new FragmentCreator() {
                            @Override
                            public Fragment createFragment() {
                                return RepoFilesFragment.create(repository);
                            }
                        })),
                new FragmentPagerModel(context.getString(R.string.commits),
                        getFragment(fragments, RepoInfoFragment.class, new FragmentCreator() {
                            @Override
                            public Fragment createFragment() {
                                return  CommitsFragment.createForRepo(repository.getOwner().getLogin(),
                                        repository.getName(), repository.getDefaultBranch());
                            }
                        })),
                new FragmentPagerModel(context.getString(R.string.activity),
                        getFragment(fragments, RepoInfoFragment.class, new FragmentCreator() {
                            @Override
                            public Fragment createFragment() {
                                return ActivityFragment.create(ActivityFragment.ActivityType.Repository,
                                        repository.getOwner().getLogin(), repository.getName());
                            }
                        }))
        ));
    }

    public static List<FragmentPagerModel> createProfilePagerList(Context context, final User user
            , @NonNull ArrayList<Fragment> fragments) {
        List<FragmentPagerModel> list = new ArrayList<>();
        list.add(new FragmentPagerModel(context.getString(R.string.info),
                getFragment(fragments, ProfileInfoFragment.class, new FragmentCreator() {
            @Override
            public Fragment createFragment() {
                return ProfileInfoFragment.create(user);
            }
        })));
        list.add(new FragmentPagerModel(context.getString(R.string.activity),
                getFragment(fragments, ActivityFragment.class, new FragmentCreator() {
                    @Override
                    public Fragment createFragment() {
                        return ActivityFragment.create(ActivityFragment.ActivityType.User, user.getLogin(), null);
                    }
                })));
        if (user.isUser()) {
            list.add(new FragmentPagerModel(context.getString(R.string.starred),
                    getFragment(fragments, RepositoriesFragment.class, new FragmentCreator() {
                        @Override
                        public Fragment createFragment() {
                            return RepositoriesFragment.create(RepositoriesFragment.RepositoriesType.STARRED, user.getLogin());
                        }
                    })));
        }
        return setPagerFragmentFlag(list);
    }

    public static List<FragmentPagerModel> createSearchPagerList(@NonNull Context context
            , @NonNull final ArrayList<SearchModel> searchModels, @NonNull ArrayList<Fragment> fragments) {
        return setPagerFragmentFlag(Arrays.asList(
                new FragmentPagerModel(context.getString(R.string.repositories),
                        getFragment(fragments, RepositoriesFragment.class, new FragmentCreator() {
                            @Override
                            public Fragment createFragment() {
                                return RepositoriesFragment.createForSearch(searchModels.get(0));
                            }
                        })),
                new FragmentPagerModel(context.getString(R.string.users),
                        getFragment(fragments, UserListFragment.class, new FragmentCreator() {
                            @Override
                            public Fragment createFragment() {
                                return UserListFragment.createForSearch(searchModels.get(1));
                            }
                        }))
        ));
    }

    public static List<FragmentPagerModel> createTrendingPagerList(
            @NonNull Context context, @NonNull ArrayList<Fragment> fragments) {
        return setPagerFragmentFlag(Arrays.asList(
                new FragmentPagerModel(context.getString(R.string.daily),
                        getFragment(fragments, RepositoriesFragment.class, new FragmentCreator() {
                            @Override
                            public Fragment createFragment() {
                                return RepositoriesFragment.createForTrending("daily");
                            }
                        })),
                new FragmentPagerModel(context.getString(R.string.weekly),
                        getFragment(fragments, RepositoriesFragment.class, new FragmentCreator() {
                            @Override
                            public Fragment createFragment() {
                                return RepositoriesFragment.createForTrending("weekly");
                            }
                        })),
                new FragmentPagerModel(context.getString(R.string.monthly),
                        getFragment(fragments, RepositoriesFragment.class, new FragmentCreator() {
                            @Override
                            public Fragment createFragment() {
                                return RepositoriesFragment.createForTrending("monthly");
                            }
                        }))
        ));
    }

    public static List<FragmentPagerModel> createRepoIssuesPagerList(@NonNull Context context
            , @NonNull final String userId, @NonNull final String repoName, @NonNull ArrayList<Fragment> fragments) {
        return setPagerFragmentFlag(Arrays.asList(
                new FragmentPagerModel(context.getString(R.string.open),
                        getFragment(fragments, IssuesFragment.class, new FragmentCreator() {
                            @Override
                            public Fragment createFragment() {
                                return IssuesFragment.createForRepo(Issue.IssueState.open, userId, repoName);
                            }
                        })),
                new FragmentPagerModel(context.getString(R.string.closed),
                        getFragment(fragments, IssuesFragment.class, new FragmentCreator() {
                            @Override
                            public Fragment createFragment() {
                                return IssuesFragment.createForRepo(Issue.IssueState.closed, userId, repoName);
                            }
                        }))
        ));
    }

    public static List<FragmentPagerModel> createUserIssuesPagerList(@NonNull Context context
            , @NonNull ArrayList<Fragment> fragments) {
        return setPagerFragmentFlag(Arrays.asList(
                new FragmentPagerModel(context.getString(R.string.open),
                        getFragment(fragments, IssuesFragment.class, new FragmentCreator() {
                    @Override
                    public Fragment createFragment() {
                        return IssuesFragment.createForUser(Issue.IssueState.open);
                    }
                })),
                new FragmentPagerModel(context.getString(R.string.closed),
                        getFragment(fragments, IssuesFragment.class, new FragmentCreator() {
                            @Override
                            public Fragment createFragment() {
                                return IssuesFragment.createForUser(Issue.IssueState.closed);
                            }
                        }))
        ));
    }

    public static List<FragmentPagerModel> createMarkdownEditorPagerList(@NonNull Context context
            , final String text, @NonNull ArrayList<Fragment> fragments) {
        return setPagerFragmentFlag(Arrays.asList(
                new FragmentPagerModel(context.getString(R.string.write),
                        getFragment(fragments, MarkdownEditorFragment.class, new FragmentCreator() {
                            @Override
                            public Fragment createFragment() {
                                return MarkdownEditorFragment.create(text);
                            }
                        })),
                new FragmentPagerModel(context.getString(R.string.preview),
                        getFragment(fragments, MarkdownPreviewFragment.class, new FragmentCreator() {
                            @Override
                            public Fragment createFragment() {
                                return MarkdownPreviewFragment.create();
                            }
                        }))
        ));
    }

    private static List<FragmentPagerModel> setPagerFragmentFlag(List<FragmentPagerModel> list) {
        for (FragmentPagerModel model : list) {
            model.getFragment().setPagerFragment(true);
        }
        return list;
    }

    private static <F extends Fragment> F getFragment(ArrayList<Fragment> fragments
            , Class<F> fClass, FragmentCreator fragmentCreator){
        Fragment fragment  = null;
        for(Fragment fragment1 : fragments){
            if(fragment1.getClass().getSimpleName().equals(fClass.getSimpleName())){
                fragment = fragment1;
                break;
            }
        }
        if(fragment == null){
            fragment = fragmentCreator.createFragment();
            Logger.d("create fragment");
        }else{
            Logger.d("reuse fragment");
        }
        return (F) fragment;
    }

    interface FragmentCreator<F extends Fragment>{
        F createFragment();
    }

}
