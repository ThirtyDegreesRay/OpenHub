

package com.thirtydegreesray.openhub.mvp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by ThirtyDegreesRay on 2017/10/20 11:53:38
 */

public class CommitsComparison implements Parcelable {

    private String url;
    @SerializedName("html_url") private String htmlUrl;
    @SerializedName("base_commit") private RepoCommit baseCommit;
    @SerializedName("merge_base_commit") private RepoCommit mergeBaseCommit;
    private String status;
    @SerializedName("total_commits") private int totalCommits;
    private ArrayList<RepoCommit> commits;
    private ArrayList<CommitFile> files;

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

    public RepoCommit getBaseCommit() {
        return baseCommit;
    }

    public void setBaseCommit(RepoCommit baseCommit) {
        this.baseCommit = baseCommit;
    }

    public RepoCommit getMergeBaseCommit() {
        return mergeBaseCommit;
    }

    public void setMergeBaseCommit(RepoCommit mergeBaseCommit) {
        this.mergeBaseCommit = mergeBaseCommit;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTotalCommits() {
        return totalCommits;
    }

    public void setTotalCommits(int totalCommits) {
        this.totalCommits = totalCommits;
    }

    public ArrayList<RepoCommit> getCommits() {
        return commits;
    }

    public void setCommits(ArrayList<RepoCommit> commits) {
        this.commits = commits;
    }

    public ArrayList<CommitFile> getFiles() {
        return files;
    }

    public void setFiles(ArrayList<CommitFile> files) {
        this.files = files;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.url);
        dest.writeString(this.htmlUrl);
        dest.writeParcelable(this.baseCommit, flags);
        dest.writeParcelable(this.mergeBaseCommit, flags);
        dest.writeString(this.status);
        dest.writeInt(this.totalCommits);
        dest.writeTypedList(this.commits);
        dest.writeTypedList(this.files);
    }

    public CommitsComparison() {
    }

    protected CommitsComparison(Parcel in) {
        this.url = in.readString();
        this.htmlUrl = in.readString();
        this.baseCommit = in.readParcelable(RepoCommit.class.getClassLoader());
        this.mergeBaseCommit = in.readParcelable(RepoCommit.class.getClassLoader());
        this.status = in.readString();
        this.totalCommits = in.readInt();
        this.commits = in.createTypedArrayList(RepoCommit.CREATOR);
        this.files = in.createTypedArrayList(CommitFile.CREATOR);
    }

    public static final Parcelable.Creator<CommitsComparison> CREATOR = new Parcelable.Creator<CommitsComparison>() {
        @Override
        public CommitsComparison createFromParcel(Parcel source) {
            return new CommitsComparison(source);
        }

        @Override
        public CommitsComparison[] newArray(int size) {
            return new CommitsComparison[size];
        }
    };

}
