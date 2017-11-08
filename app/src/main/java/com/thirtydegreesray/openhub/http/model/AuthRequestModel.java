

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
