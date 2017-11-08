

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
