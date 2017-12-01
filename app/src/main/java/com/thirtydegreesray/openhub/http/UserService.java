

package com.thirtydegreesray.openhub.http;

import android.support.annotation.NonNull;

import com.thirtydegreesray.openhub.mvp.model.Event;
import com.thirtydegreesray.openhub.mvp.model.User;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created on 2017/8/1.
 *
 * @author ThirtyDegreesRay
 */

public interface UserService {

    @NonNull @GET("user")
    Observable<Response<User>> getPersonInfo(
            @Header("forceNetWork") boolean forceNetWork
    );

    @NonNull @GET("users/{user}")
    Observable<Response<User>> getUser(
            @Header("forceNetWork") boolean forceNetWork,
            @Path("user") String user
    );


    @NonNull @GET("user/following/{user}")
    Observable<Response<ResponseBody>> checkFollowing(
            @Path("user") String user
    );

    /**
     * Check if one user follows another
     */
    @NonNull @GET("users/{user}/following/{targetUser}")
    Observable<Response<ResponseBody>> checkFollowing(
            @Path("user") String user,
            @Path("targetUser") String targetUser
    );

    @NonNull @PUT("user/following/{user}")
    Observable<Response<ResponseBody>> followUser(
            @Path("user") String user
    );

    @NonNull @DELETE("user/following/{user}")
    Observable<Response<ResponseBody>> unfollowUser(
            @Path("user") String user
    );

    @NonNull @GET("users/{user}/followers")
    Observable<Response<ArrayList<User>>> getFollowers(
            @Header("forceNetWork") boolean forceNetWork,
            @Path("user") String user,
            @Query("page") int page
    );

    @NonNull @GET("users/{user}/following")
    Observable<Response<ArrayList<User>>> getFollowing(
            @Header("forceNetWork") boolean forceNetWork,
            @Path("user") String user,
            @Query("page") int page
    );

    /**
     * List events performed by a user
     */
    @NonNull @GET("users/{user}/events")
    Observable<Response<ArrayList<Event>>> getUserEvents(
            @Header("forceNetWork") boolean forceNetWork,
            @Path("user") String user,
            @Query("page") int page
    );

    /**
     * List github public events
     */
    @NonNull @GET("events")
    Observable<Response<ArrayList<Event>>> getPublicEvent(
            @Header("forceNetWork") boolean forceNetWork,
            @Query("page") int page
    );

    @NonNull @GET("users/{user}/received_events")
    Observable<Response<ArrayList<Event>>> getNewsEvent(
            @Header("forceNetWork") boolean forceNetWork,
            @Path("user") String user,
            @Query("page") int page
    );

    @NonNull @GET("orgs/{org}/members")
    Observable<Response<ArrayList<User>>> getOrgMembers(
            @Header("forceNetWork") boolean forceNetWork,
            @Path("org") String org,
            @Query("page") int page
    );

    @NonNull @GET("users/{user}/orgs")
    Observable<Response<ArrayList<User>>> getUserOrgs(
            @Header("forceNetWork") boolean forceNetWork,
            @Path("user") String user
    );


}
