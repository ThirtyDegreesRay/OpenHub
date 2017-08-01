/*
 *    Copyright 2017 ThirtyDegressRay
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

import com.thirtydegreesray.openhub.mvp.model.Repository;

import java.util.ArrayList;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created on 2017/8/1.
 *
 * @author ThirtyDegreesRay
 */

public interface RepoService {

    @GET("user{user}/starred")
    Observable<Response<ArrayList<Repository>>> getUserStarred(
            @Header("forceNetWork") boolean forceNetWork,
            @Path("user") String user
    );

    @GET("user{user}/repos")
    Observable<retrofit2.Response<ArrayList<Repository>>> getUserRepos(
            @Header("forceNetWork") boolean forceNetWork,
            @Path("user") String user
    );

}
