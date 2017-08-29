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
