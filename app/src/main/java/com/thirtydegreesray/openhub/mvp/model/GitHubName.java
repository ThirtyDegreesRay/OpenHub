

package com.thirtydegreesray.openhub.mvp.model;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.thirtydegreesray.openhub.util.GitHubHelper;

import java.util.ArrayList;

/**
 * Created by ThirtyDegreesRay on 2017/10/30 13:49:02
 */

public class GitHubName {

    private String url;
    private String userName ;
    private String repoName ;

    public static GitHubName fromUrl(@NonNull String url){
        if(!GitHubHelper.isGitHubUrl(url)) return null;
        GitHubName gitHubName = new GitHubName();
        url = url.endsWith("/") ? url.substring(0, url.length() - 1) : url;
        gitHubName.url = url;
        try{
            Uri uri = Uri.parse(url);
            ArrayList<String> list = new ArrayList<>(uri.getPathSegments());
            list.remove("repos");
            if(list.size() > 0) gitHubName.userName = list.get(0);
            if(list.size() > 1) gitHubName.repoName = list.get(1);
        }catch (Exception e){

        }
        return gitHubName;
    }

    public String getUserName() {
        return userName;
    }

    public String getRepoName() {
        return repoName;
    }

    public String getReleaseTagName(){
        if(!GitHubHelper.isReleaseTagUrl(url)){
            return null;
        }
        return url.substring(url.lastIndexOf("/") + 1);
    }

    public String getCommitShaName(){
        if(!GitHubHelper.isCommitUrl(url)){
            return null;
        }
        return url.substring(url.lastIndexOf("/") + 1);
    }


}
