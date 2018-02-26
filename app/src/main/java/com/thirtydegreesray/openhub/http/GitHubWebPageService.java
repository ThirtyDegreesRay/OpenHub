package com.thirtydegreesray.openhub.http;

import android.support.annotation.NonNull;

import com.thirtydegreesray.openhub.mvp.model.WikiFeedModel;

import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by ThirtyDegreesRay on 2017/12/25 15:30:56
 */

public interface GitHubWebPageService {

    @NonNull @GET("{owner}/{repo}/wiki.atom")
    Observable<Response<WikiFeedModel>> getWiki(
            @Header("forceNetWork") boolean forceNetWork,
            @Path("owner") String owner,
            @Path("repo") String repo
    );

    @NonNull @GET("collections")
    Observable<Response<ResponseBody>> getCollections(
            @Header("forceNetWork") boolean forceNetWork
    );

    @NonNull @GET("collections/{collectionId}")
    Observable<Response<ResponseBody>> getCollectionInfo(
            @Header("forceNetWork") boolean forceNetWork,
            @Path("collectionId") String collectionId
    );

    @NonNull @GET("topics")
    Observable<Response<ResponseBody>> getTopics(
            @Header("forceNetWork") boolean forceNetWork
    );

    @NonNull @GET("trending/{language}")
    Observable<Response<ResponseBody>> getTrendingRepos(
            @Header("forceNetWork") boolean forceNetWork,
            @Path(value = "language", encoded = true) String language,
            @Query("since") String since
    );

    @Headers("Cache-Control: public, max-age=86400")
    @NonNull @GET("trending")
    Observable<Response<ResponseBody>> getTrendingLanguages();

}
