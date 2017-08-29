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

package com.thirtydegreesray.openhub.mvp.model;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created on 2017/8/1.
 *
 * @author ThirtyDegreesRay
 */

public class BasicToken {

    private int id;
    private String url;
    private String token;
    @SerializedName("created_at") private Date createdAt;
    @SerializedName("updated_at") private Date updatedAt;
    private List<String> scopes;

    public BasicToken() {

    }

    public static BasicToken generateFromOauthToken(OauthToken oauthToken){
        BasicToken basicToken = new BasicToken();
        basicToken.setToken(oauthToken.getAccessToken());
        basicToken.setScopes(Arrays.asList(oauthToken.getScope().split(",")));
        return basicToken;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<String> getScopes() {
        return scopes;
    }

    public void setScopes(List<String> scopes) {
        this.scopes = scopes;
    }

    //    {
//        "id": 118469758,
//            "url": "https://api.github.com/authorizations/118469758",
//            "app": {
//        "name": "OpenHub",
//                "url": "https://github.com/ThirtyDegreesRay/OpenHub",
//                "client_id": "2a2f29517239a22ad850"
//    },
//        "token": "2d6b2205ccffb76fe32cd53314bf75c47369b0f1",
//            "hashed_token": "2eb24cf25622f32fdf26761c3d9051813c6985faed9d457d5d5278cb2bfe70a2",
//            "token_last_eight": "7369b0f1",
//            "note": "com.thirtydegreesray.openhub",
//            "note_url": null,
//            "created_at": "2017-08-01T03:46:37Z",
//            "updated_at": "2017-08-01T03:46:37Z",
//            "scopes": [
//        "user",
//                "repo",
//                "gist",
//                "notifications"
//    ],
//        "fingerprint": null
//    }

}
