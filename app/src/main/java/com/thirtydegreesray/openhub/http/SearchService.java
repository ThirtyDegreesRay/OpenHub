

package com.thirtydegreesray.openhub.http;

import android.support.annotation.NonNull;

import com.thirtydegreesray.openhub.mvp.model.Issue;
import com.thirtydegreesray.openhub.mvp.model.Repository;
import com.thirtydegreesray.openhub.mvp.model.SearchResult;
import com.thirtydegreesray.openhub.mvp.model.User;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by ThirtyDegreesRay on 2017/8/25 13:52:09
 */

public interface SearchService {

//    https://api.github.com/search/users?q=Ray&sort=followers&order=desc
    @NonNull @GET("search/users")
    Observable<Response<SearchResult<User>>> searchUsers(
            @Query(value = "q", encoded = true) String query,
            @Query("sort") String sort,
            @Query("order") String order,
            @Query("page") int page
    );

    @NonNull @GET("search/repositories")
    Observable<Response<SearchResult<Repository>>> searchRepos(
            @Query(value = "q", encoded = true) String query,
            @Query("sort") String sort,
            @Query("order") String order,
            @Query("page") int page
    );

//    https://api.github.com/search/issues?sort=created&page=1&q=user:ThirtyDegreesRay+state:open&order=desc
    @NonNull @GET("search/issues")
    @Headers("Accept: application/vnd.github.html,application/vnd.github.VERSION.raw")
    Observable<Response<SearchResult<Issue>>> searchIssues(
            @Header("forceNetWork") boolean forceNetWork,
            @Query(value = "q", encoded = true) String query,
            @Query("sort") String sort,
            @Query("order") String order,
            @Query("page") int page
    );

}
