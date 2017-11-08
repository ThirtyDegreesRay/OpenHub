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

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;

import com.danielstone.materialaboutlibrary.MaterialAboutActivity;
import com.danielstone.materialaboutlibrary.items.MaterialAboutActionItem;
import com.danielstone.materialaboutlibrary.items.MaterialAboutItemOnClickAction;
import com.danielstone.materialaboutlibrary.items.MaterialAboutTitleItem;
import com.danielstone.materialaboutlibrary.model.MaterialAboutCard;
import com.danielstone.materialaboutlibrary.model.MaterialAboutList;
import com.tencent.bugly.beta.Beta;
import com.thirtydegreesray.openhub.AppConfig;
import com.thirtydegreesray.openhub.AppData;
import com.thirtydegreesray.openhub.BuildConfig;
import com.thirtydegreesray.openhub.R;
import com.thirtydegreesray.openhub.http.RepoService;
import com.thirtydegreesray.openhub.http.core.AppRetrofit;
import com.thirtydegreesray.openhub.http.core.HttpObserver;
import com.thirtydegreesray.openhub.http.core.HttpResponse;
import com.thirtydegreesray.openhub.http.core.HttpSubscriber;
import com.thirtydegreesray.openhub.ui.widget.UpgradeDialog;
import com.thirtydegreesray.openhub.util.AppOpener;
import com.thirtydegreesray.openhub.util.AppUtils;
import com.thirtydegreesray.openhub.util.StarWishesHelper;
import com.thirtydegreesray.openhub.util.ThemeHelper;

import es.dmoral.toasty.Toasty;
import okhttp3.ResponseBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ThirtyDegreesRay on 2017/8/29.
 */

public class AboutActivity extends MaterialAboutActivity {

    public static void show(@NonNull Context context){
        Intent intent = new Intent(context, AboutActivity.class);
        context.startActivity(intent);
    }

    private boolean isAlive = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        isAlive = true;
        ThemeHelper.applyForAboutActivity(this);
        super.onCreate(savedInstanceState);
        UpgradeDialog.INSTANCE.setShowDialogActivity(this);
        checkStarWishes();
    }

    @NonNull
    @Override
    protected MaterialAboutList getMaterialAboutList(@NonNull Context context) {
        MaterialAboutCard.Builder appBuilder = new MaterialAboutCard.Builder();
        buildApp(appBuilder, context);
        MaterialAboutCard.Builder authorBuilder = new MaterialAboutCard.Builder();
        buildAuthor(authorBuilder, context);
        MaterialAboutCard.Builder shareBuilder = new MaterialAboutCard.Builder();
        buildShare(shareBuilder, context);
        return new MaterialAboutList(appBuilder.build(), authorBuilder.build(), shareBuilder.build());
    }

    @Nullable
    @Override
    protected CharSequence getActivityTitle() {
        return getString(R.string.about);
    }

    private void buildApp(MaterialAboutCard.Builder appBuilder, final Context context){
        appBuilder.addItem(new MaterialAboutTitleItem.Builder()
                .text(getString(R.string.app_name))
                .desc(getString(R.string.app_copyright))
                .icon(R.mipmap.logo)
                .build());
        appBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text(R.string.version)
                .subText(BuildConfig.VERSION_NAME)
                .icon(R.drawable.ic_menu_about)
                .setOnClickAction(new MaterialAboutItemOnClickAction() {
                    @Override
                    public void onClick() {
                        Beta.checkUpgrade(true, true);
                    }
                })
                .build());
        appBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text(R.string.source_code)
                .subText(R.string.source_code_wishes)
                .icon(R.drawable.ic_code)
                .setOnClickAction(new MaterialAboutItemOnClickAction() {
                    @Override
                    public void onClick() {
                        RepositoryActivity.show(context, getString(R.string.author_login_id), getString(R.string.app_name));
                    }
                })
                .build());
    }

    private void buildAuthor(MaterialAboutCard.Builder appBuilder, final Context context){
        appBuilder.title(R.string.author);
        appBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text(R.string.author_name)
                .subText(R.string.author_location)
                .icon(R.drawable.ic_menu_person)
                .setOnClickAction(new MaterialAboutItemOnClickAction() {
                    @Override
                    public void onClick() {
                        ProfileActivity.show(AboutActivity.this, getString(R.string.author_login_id),
                                getString(R.string.author_avatar_url));
                    }
                })
                .build());
        appBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text(R.string.follow_on_github)
                .icon(R.drawable.ic_github)
                .setOnClickAction(new MaterialAboutItemOnClickAction() {
                    @Override
                    public void onClick() {
                        ProfileActivity.show(AboutActivity.this, getString(R.string.author_login_id),
                                getString(R.string.author_avatar_url));
                    }
                })
                .build());
        appBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text(R.string.email)
                .subText(R.string.auth_email_address)
                .icon(R.drawable.ic_mail)
                .setOnClickAction(new MaterialAboutItemOnClickAction() {
                    @Override
                    public void onClick() {
                        AppOpener.launchEmail(context, getString(R.string.auth_email_address));
                    }
                })
                .setOnLongClickAction(new MaterialAboutItemOnClickAction() {
                    @Override
                    public void onClick() {
                        AppUtils.copyToClipboard(context, getString(R.string.auth_email_address));
                    }
                })
                .build());
    }

    private void buildShare(MaterialAboutCard.Builder appBuilder, final Context context) {
        appBuilder.title(R.string.feedback_and_share);
        appBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text(R.string.share_to_friends)
                .icon(R.drawable.ic_share)
                .setOnClickAction(new MaterialAboutItemOnClickAction() {
                    @Override
                    public void onClick() {
                        AppOpener.shareText(AboutActivity.this, getString(R.string.cookapk_download_url));
                    }
                })
                .build());
        appBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text(R.string.rate_in_market)
                .icon(R.drawable.ic_menu_star)
                .setOnClickAction(new MaterialAboutItemOnClickAction() {
                    @Override
                    public void onClick() {
                        AppOpener.openInMarket(context);
                    }
                })
                .build());
        appBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text(R.string.feedback)
                .icon(R.drawable.ic_feedback)
                .setOnClickAction(new MaterialAboutItemOnClickAction() {
                    @Override
                    public void onClick() {
                        IssuesActivity.showForRepo(AboutActivity.this,
                                getString(R.string.author_login_id), getString(R.string.app_name));
                    }
                })
                .build());
    }

    @Override
    protected void onDestroy() {
        isAlive = false;
        UpgradeDialog.INSTANCE.setShowDialogActivity(null);
        super.onDestroy();
    }

    private void checkStarWishes(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(isAlive && StarWishesHelper.isStarWishesTipable()){
                    checkStar();
                }
            }
        }, 3000);
    }

    private void checkStar(){
        HttpSubscriber<ResponseBody> subscriber = new HttpSubscriber<>(
                new HttpObserver<ResponseBody>() {
                    @Override
                    public void onError(Throwable error) {

                    }

                    @Override
                    public void onSuccess(HttpResponse<ResponseBody> response) {
                        boolean starred = response.isSuccessful();
                        if(isAlive && !starred && StarWishesHelper.isStarWishesTipable()){
                            showStarWishes();
                        }
                    }
                }
        );
        String author = getString(R.string.author_login_id);
        String openHub = getString(R.string.app_name);
        getRepoService().checkRepoStarred(author, openHub)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void showStarWishes() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.openhub_wishes)
                .setMessage(R.string.star_wishes)
                .setNegativeButton(R.string.next_time, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(R.string.star, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        starRepo();
                        Toasty.success(AboutActivity.this, getString(R.string.star_thanks)).show();
                    }
                })
                .show();
        StarWishesHelper.addStarWishesTipTimes();
    }

    private void starRepo(){
        String author = getString(R.string.author_login_id);
        String openHub = getString(R.string.app_name);
        getRepoService().starRepo(author, openHub)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpSubscriber<ResponseBody>());
    }

    private RepoService getRepoService(){
        return AppRetrofit.INSTANCE
                .getRetrofit(AppConfig.GITHUB_API_BASE_URL, AppData.INSTANCE.getAccessToken())
                .create(RepoService.class);
    }

}
