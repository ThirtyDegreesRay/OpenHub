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
