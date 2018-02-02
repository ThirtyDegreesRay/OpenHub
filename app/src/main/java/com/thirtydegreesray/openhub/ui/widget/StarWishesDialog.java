package com.thirtydegreesray.openhub.ui.widget;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;

import com.thirtydegreesray.openhub.AppConfig;
import com.thirtydegreesray.openhub.AppData;
import com.thirtydegreesray.openhub.R;
import com.thirtydegreesray.openhub.http.RepoService;
import com.thirtydegreesray.openhub.http.core.AppRetrofit;
import com.thirtydegreesray.openhub.http.core.HttpObserver;
import com.thirtydegreesray.openhub.http.core.HttpResponse;
import com.thirtydegreesray.openhub.http.core.HttpSubscriber;
import com.thirtydegreesray.openhub.util.NetHelper;
import com.thirtydegreesray.openhub.util.StarWishesHelper;
import com.thirtydegreesray.openhub.util.StringUtils;

import es.dmoral.toasty.Toasty;
import okhttp3.ResponseBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ThirtyDegreesRay on 2018/2/2 13:57:48
 */

public class StarWishesDialog {

    private Context mContext;
    private boolean isAlive;
    private boolean starred;

    public StarWishesDialog(@NonNull Context context) {
        mContext = context;
        isAlive = true;
    }

    public void checkStarWishes() {
        if (!isStarWishesTipable()) return;
        new Handler().postDelayed(() -> {
            if (isStarWishesTipable()) checkStar();
        }, 3000);
    }

    private void checkStar() {
        HttpSubscriber<ResponseBody> subscriber = new HttpSubscriber<>(
                new HttpObserver<ResponseBody>() {
                    @Override
                    public void onError(Throwable error) {

                    }

                    @Override
                    public void onSuccess(HttpResponse<ResponseBody> response) {
                        starred = response.isSuccessful();
                        if ((ignoreStarred() || !starred) && isStarWishesTipable()) {
                            showStarWishes();
                        }
                    }
                }
        );
        String author = getString(R.string.author_login_id);
        String openHub = getString(R.string.app_github_name);
        getRepoService().checkRepoStarred(author, openHub)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    protected void showStarWishes() {
        new AlertDialog.Builder(mContext)
                .setTitle(R.string.openhub_wishes)
                .setMessage(getContent())
                .setNegativeButton(R.string.star_next_time, null)
                .setPositiveButton(starred ? R.string.ok : R.string.star_me, (dialog, which) -> {
                    if (!starred) {
                        starRepo();
                        Toasty.success(mContext, getString(R.string.star_thanks)).show();
                    }
                })
                .setCancelable(false)
                .show();
        StarWishesHelper.addStarWishesTipTimes();
    }

    private void starRepo() {
        String author = getString(R.string.author_login_id);
        String openHub = getString(R.string.app_name);
        getRepoService().starRepo(author, openHub)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpSubscriber<>());
    }

    private RepoService getRepoService() {
        return AppRetrofit.INSTANCE
                .getRetrofit(AppConfig.GITHUB_API_BASE_URL, AppData.INSTANCE.getAccessToken())
                .create(RepoService.class);
    }

    protected String getString(int id) {
        return mContext.getString(id);
    }

    protected boolean isStarWishesTipable() {
        return isAlive && StarWishesHelper.isStarWishesTipable() && NetHelper.INSTANCE.getNetEnabled();
    }

    protected String getContent() {
        String message = getString(R.string.star_wishes);
        String user = AppData.INSTANCE.getLoggedUser().getName();
        user = StringUtils.isBlank(user) ? AppData.INSTANCE.getLoggedUser().getLogin() : user;
        message = String.format(message, user, StarWishesHelper.getInstalledDays());
        return message;
    }

    protected boolean ignoreStarred() {
        return false;
    }

    protected boolean isStarred(){
        return starred;
    }

    public void cancel() {
        isAlive = false;
    }

}
