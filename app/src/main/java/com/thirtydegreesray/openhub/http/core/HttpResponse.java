

package com.thirtydegreesray.openhub.http.core;

import android.support.annotation.Nullable;

import retrofit2.Response;

/**
 * Created on 2017/7/13.
 *
 * @author ThirtyDegreesRay
 */

public class HttpResponse <T extends Object> {

    private Response<T> oriResponse;

    public HttpResponse(Response<T> response){
        oriResponse = response;
    }

    public boolean isSuccessful(){
        return oriResponse.isSuccessful();
    }

    public boolean isFromCache(){
        return  isResponseEnable(oriResponse.raw().cacheResponse());
    }

    public boolean isFromNetWork(){
        return  isResponseEnable(oriResponse.raw().networkResponse());
    }

    private boolean isResponseEnable(@Nullable okhttp3.Response response){
        return response != null && response.code() == 200;
    }

    public Response<T> getOriResponse() {
        return oriResponse;
    }

    public T body(){
        return oriResponse.body();
    }
}
