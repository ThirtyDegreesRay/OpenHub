

package com.thirtydegreesray.openhub.mvp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ThirtyDegreesRay on 2017/8/14 15:40:39
 */

public class FileModel implements Parcelable {

    private String name;
    private String path;
    private String sha;
    private int size;
    private String url;
    @SerializedName("html_url") private String htmlUrl;
    @SerializedName("git_url") private String gitUrl;
    @SerializedName("download_url") private String downloadUrl;
    @SerializedName("type") private String type;

    public FileModel() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getSha() {
        return sha;
    }

    public void setSha(String sha) {
        this.sha = sha;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHtmlUrl() {
        return htmlUrl;
    }

    public void setHtmlUrl(String htmlUrl) {
        this.htmlUrl = htmlUrl;
    }

    public String getGitUrl() {
        return gitUrl;
    }

    public void setGitUrl(String gitUrl) {
        this.gitUrl = gitUrl;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isFile(){
        return type.equals("file");
    }

    public boolean isDir(){
        return type.equals("dir");
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.path);
        dest.writeString(this.sha);
        dest.writeInt(this.size);
        dest.writeString(this.url);
        dest.writeString(this.htmlUrl);
        dest.writeString(this.gitUrl);
        dest.writeString(this.downloadUrl);
        dest.writeString(this.type);
    }

    protected FileModel(Parcel in) {
        this.name = in.readString();
        this.path = in.readString();
        this.sha = in.readString();
        this.size = in.readInt();
        this.url = in.readString();
        this.htmlUrl = in.readString();
        this.gitUrl = in.readString();
        this.downloadUrl = in.readString();
        this.type = in.readString();
    }

    public static final Parcelable.Creator<FileModel> CREATOR = new Parcelable.Creator<FileModel>() {
        @Override
        public FileModel createFromParcel(Parcel source) {
            return new FileModel(source);
        }

        @Override
        public FileModel[] newArray(int size) {
            return new FileModel[size];
        }
    };
}