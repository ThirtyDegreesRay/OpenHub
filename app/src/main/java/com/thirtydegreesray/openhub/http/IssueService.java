package com.thirtydegreesray.openhub.http;

import android.support.annotation.NonNull;

import com.thirtydegreesray.openhub.mvp.model.Issue;
import com.thirtydegreesray.openhub.mvp.model.IssueEvent;

import java.util.ArrayList;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by ThirtyDegreesRay on 2017/9/20 14:28:30
 */

public interface IssueService {

    @NonNull @GET("repos/{owner}/{repo}/issues")
    Observable<Response<ArrayList<Issue>>> getRepoIssues(
            @Header("forceNetWork") boolean forceNetWork,
            @Path("owner") String owner,
            @Path("repo") String repo,
            @Query("state") String state,
            @Query("page") int page
    );

    @NonNull @GET("user/issues")
    Observable<Response<ArrayList<Issue>>> getUserIssues(
            @Header("forceNetWork") boolean forceNetWork,
            @Query("filter") int filter,
            @Query("state") int state,
            @Query("page") int page
    );

    @NonNull @GET("repos/{owner}/{repo}/issues/{issueNumber}")
    Observable<Response<Issue>> getIssueInfo(
            @Header("forceNetWork") boolean forceNetWork,
            @Path("owner") String owner,
            @Path("repo") String repo,
            @Path("issueNumber") int issueNumber
    );

    @NonNull @GET("repos/{owner}/{repo}/issues/{issueNumber}/timeline")
    @Headers("Accept: application/vnd.github.mockingbird-preview")
    Observable<Response<ArrayList<IssueEvent>>> getIssueTimeline(
            @Header("forceNetWork") boolean forceNetWork,
            @Path("owner") String owner,
            @Path("repo") String repo,
            @Path("issueNumber") int issueNumber,
            @Query("page") int page
    );

}
