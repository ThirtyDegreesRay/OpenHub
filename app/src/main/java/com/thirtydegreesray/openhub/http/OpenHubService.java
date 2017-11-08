

package com.thirtydegreesray.openhub.http;

import android.support.annotation.NonNull;

import com.thirtydegreesray.openhub.mvp.model.Repository;

import java.util.ArrayList;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import rx.Observable;

/**
 * This link are for OpenHub only. Please do not use this endpoint in your applications.
 * If you want to get trending repositories, you may stand up your own instance.
 * https://github.com/thedillonb/GitHub-Trending
 * Created by ThirtyDegreesRay on 2017/8/26 16:04:02
 */

public interface OpenHubService {

    /**
     * get trending repos
     * @param since daily, weekly, monthly
     */
    @Headers("Cache-Control: public, max-age=3600")
    @NonNull @GET("trending")
    Observable<Response<ArrayList<Repository>>> getTrendingRepos(
            @Query("since") String since
    );

}
