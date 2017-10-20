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

import com.thirtydegreesray.openhub.mvp.model.CommitsComparison;
import com.thirtydegreesray.openhub.mvp.model.RepoCommit;
import com.thirtydegreesray.openhub.mvp.model.RepoCommitExt;

import java.util.ArrayList;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by ThirtyDegreesRay on 2017/10/17 13:13:33
 */

public interface CommitService {

    @NonNull @GET("repos/{owner}/{repo}/commits")
    Observable<Response<ArrayList<RepoCommit>>> getRepoCommits(
            @Header("forceNetWork") boolean forceNetWork,
            @Path("owner") String owner,
            @Path("repo") String repo,
            //SHA or branch to start listing commits from. Default: the repository’s default branch (usually master).
            @Query("sha") String branch,
            @Query("page") int page
    );

    @NonNull @GET("repos/{owner}/{repo}/commits/{sha}")
    Observable<Response<RepoCommitExt>> getCommitInfo(
            @Header("forceNetWork") boolean forceNetWork,
            @Path("owner") String owner,
            @Path("repo") String repo,
            @Path("sha") String sha
    );

    @NonNull @GET("repos/{owner}/{repo}/commits/{ref}/comments")
    Observable<Response<ArrayList<RepoCommit>>> getCommitComments(
            @Header("forceNetWork") boolean forceNetWork,
            @Path("owner") String owner,
            @Path("repo") String repo,
            @Path("ref") String ref,
            @Query("page") int page
    );

    @NonNull @GET("repos/{owner}/{repo}/compare/{before}...{head}")
    Observable<Response<CommitsComparison>> compareTwoCommits(
            @Header("forceNetWork") boolean forceNetWork,
            @Path("owner") String owner,
            @Path("repo") String repo,
            @Path("before") String before,
            @Path("head") String head
    );

}
