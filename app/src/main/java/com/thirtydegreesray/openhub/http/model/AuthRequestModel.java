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

package com.thirtydegreesray.openhub.http.model;

import com.google.gson.annotations.SerializedName;
import com.thirtydegreesray.openhub.AppConfig;
import com.thirtydegreesray.openhub.BuildConfig;

import java.util.Arrays;
import java.util.List;

/**
 * Created on 2017/8/1.
 *
 * @author ThirtyDegreesRay
 */

public class AuthRequestModel {

    private List<String> scopes;
    private String note;
    private String noteUrl;
    @SerializedName("client_id") private String clientId;
    @SerializedName("client_secret") private String clientSecret;

    public static AuthRequestModel generate(){
        AuthRequestModel model = new AuthRequestModel();
        model.scopes = Arrays.asList("user", "repo", "gist", "notifications");
        model.note = BuildConfig.APPLICATION_ID;
        model.clientId = AppConfig.OPENHUB_CLIENT_ID;
        model.clientSecret = AppConfig.OPENHUB_CLIENT_SECRET;
        model.noteUrl = AppConfig.REDIRECT_URL;
        return model;
    }

    public List<String> getScopes() {
        return scopes;
    }

    public String getNote() {
        return note;
    }

    public String getNoteUrl() {
        return noteUrl;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }
}
