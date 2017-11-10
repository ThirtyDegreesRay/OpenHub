

package com.thirtydegreesray.openhub.http;

import android.support.annotation.NonNull;

import com.thirtydegreesray.openhub.mvp.model.Branch;
import com.thirtydegreesray.openhub.mvp.model.Event;
import com.thirtydegreesray.openhub.mvp.model.FileModel;
import com.thirtydegreesray.openhub.mvp.model.Release;
import com.thirtydegreesray.openhub.mvp.model.Repository;
import com.thirtydegreesray.openhub.mvp.model.User;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created on 2017/8/1.
 *
 * @author ThirtyDegreesRay
 */

public interface RepoService {

    /**
     * List repositories being starred
     */
    @NonNull @GET("users/{user}/starred")
    Observable<Response<ArrayList<Repository>>> getStarredRepos(
            @Header("forceNetWork") boolean forceNetWork,
            @Path("user") @NonNull String user,
            @Query("page") int page,
            @Query("sort") String sort,
            @Query("direction") String direction
    );

    @NonNull @GET("user/repos")
    Observable<retrofit2.Response<ArrayList<Repository>>> getUserRepos(
            @Header("forceNetWork") boolean forceNetWork,
            @Query("page") int page,
            @Query("type") String type,
            @Query("sort") String sort,
            @Query("direction") String direction
    );

    /**
     * List user repositories
     */
    @NonNull @GET("users/{user}/repos")
    Observable<retrofit2.Response<ArrayList<Repository>>> getUserPublicRepos(
            @Header("forceNetWork") boolean forceNetWork,
            @Path("user") @NonNull String user,
            @Query("page") int page,
            @Query("type") String type,
            @Query("sort") String sort,
            @Query("direction") String direction
    );

    /**
     * Check if you are starring a repository
     */
    @NonNull @GET("user/starred/{owner}/{repo}")
    Observable<Response<ResponseBody>> checkRepoStarred(
            @Path("owner") String owner,
            @Path("repo") String repo
    );

    /**
     * Star a repository
     */
    @NonNull @PUT("user/starred/{owner}/{repo}")
    Observable<Response<ResponseBody>> starRepo(
            @Path("owner") String owner,
            @Path("repo") String repo
    );

    /**
     * Unstar a repository
     */
    @NonNull @DELETE("user/starred/{owner}/{repo}")
    Observable<Response<ResponseBody>> unstarRepo(
            @Path("owner") String owner,
            @Path("repo") String repo
    );

    @NonNull @GET("user/subscriptions/{owner}/{repo}")
    Observable<Response<ResponseBody>> checkRepoWatched(
            @Path("owner") String owner,
            @Path("repo") String repo
    );

    @NonNull @PUT("user/subscriptions/{owner}/{repo}")
    Observable<Response<ResponseBody>> watchRepo(
            @Path("owner") String owner,
            @Path("repo") String repo
    );

    @NonNull @DELETE("user/subscriptions/{owner}/{repo}")
    Observable<Response<ResponseBody>> unwatchRepo(
            @Path("owner") String owner,
            @Path("repo") String repo
    );

    @NonNull @GET @Headers("Accept: application/vnd.github.html")
    Observable<Response<ResponseBody>> getFileAsHtmlStream(
            @Header("forceNetWork") boolean forceNetWork,
            @Url String url
    );

    @NonNull @GET @Headers("Accept: application/vnd.github.VERSION.raw")
    Observable<Response<ResponseBody>> getFileAsStream(
            @Header("forceNetWork") boolean forceNetWork,
            @Url String url
    );

    @NonNull @GET("repos/{owner}/{repo}/contents/{path}")
    Observable<Response<ArrayList<FileModel>>> getRepoFiles(
            @Path("owner") String owner,
            @Path("repo") String repo,
            @Path(value = "path", encoded = true) String path,
            @Query("ref") String branch
    );

    @NonNull @GET("repos/{owner}/{repo}/branches")
    Observable<Response<ArrayList<Branch>>> getBranches(
            @Path("owner") String owner,
            @Path("repo") String repo
    );

    @NonNull @GET("repos/{owner}/{repo}/tags")
    Observable<Response<ArrayList<Branch>>> getTags(
            @Path("owner") String owner,
            @Path("repo") String repo
    );

    @NonNull @GET("repos/{owner}/{repo}/stargazers")
    Observable<Response<ArrayList<User>>> getStargazers(
            @Header("forceNetWork") boolean forceNetWork,
            @Path(value = "owner") String owner,
            @Path(value = "repo") String repo,
            @Query("page") int page
    );

    @NonNull @GET("repos/{owner}/{repo}/subscribers")
    Observable<Response<ArrayList<User>>> getWatchers(
            @Header("forceNetWork") boolean forceNetWork,
            @Path("owner") String owner,
            @Path("repo") String repo,
            @Query("page") int page
    );

    @NonNull @GET("repos/{owner}/{repo}")
    Observable<Response<Repository>> getRepoInfo(
            @Header("forceNetWork") boolean forceNetWork,
            @Path("owner") String owner,
            @Path("repo") String repo
    );

    @NonNull @POST("repos/{owner}/{repo}/forks")
    Observable<Response<Repository>> createFork(
            @Path("owner") String owner,
            @Path("repo") String repo
    );

    @NonNull @GET("repos/{owner}/{repo}/forks")
    Observable<Response<ArrayList<Repository>>> getForks(
            @Header("forceNetWork") boolean forceNetWork,
            @Path("owner") String owner,
            @Path("repo") String repo,
            @Query("page") int page
    );

    /**
     * List public events for a network of repositories
     */
    @NonNull @GET("networks/{owner}/{repo}/events")
    Observable<Response<ArrayList<Event>>> getRepoEvent(
            @Header("forceNetWork") boolean forceNetWork,
            @Path("owner") String owner,
            @Path("repo") String repo,
            @Query("page") int page
    );

    @NonNull @GET("repos/{owner}/{repo}/releases")
    @Headers("Accept: application/vnd.github.html")
    Observable<Response<ArrayList<Release>>> getReleases(
            @Header("forceNetWork") boolean forceNetWork,
            @Path("owner") String owner,
            @Path("repo") String repo,
            @Query("page") int page
    );

    @NonNull @GET("repos/{owner}/{repo}/releases/tags/{tag}")
    @Headers("Accept: application/vnd.github.html")
    Observable<Response<Release>> getReleaseByTagName(
            @Header("forceNetWork") boolean forceNetWork,
            @Path("owner") String owner,
            @Path("repo") String repo,
            @Path("tag") String tag
    );

}
