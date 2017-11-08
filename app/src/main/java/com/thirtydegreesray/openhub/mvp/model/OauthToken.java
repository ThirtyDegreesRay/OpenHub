

package com.thirtydegreesray.openhub.mvp.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created on 2017/7/14.
 *
 * @author ThirtyDegreesRay
 */

public class OauthToken {

    @SerializedName("access_token")
    private String accessToken;

    private String scope;

    public OauthToken() {
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }
}
