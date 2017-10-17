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

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by ThirtyDegreesRay on 2017/10/17 13:41:45
 */

public class RepoCommit implements Parcelable {

    private String sha;
    private String url;
    @SerializedName("html_url") private String htmlUrl;
    @SerializedName("comments_url") private String commentsUrl;

    private CommitGitInfo commit;
    private User author;
    private User committer;
    private ArrayList<RepoCommit> parents;
    private ArrayList<CommitFile> files;
    private CommitStats stats;

    public String getSha() {
        return sha;
    }

    public void setSha(String sha) {
        this.sha = sha;
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

    public String getCommentsUrl() {
        return commentsUrl;
    }

    public void setCommentsUrl(String commentsUrl) {
        this.commentsUrl = commentsUrl;
    }

    public CommitGitInfo getCommit() {
        return commit;
    }

    public void setCommit(CommitGitInfo commit) {
        this.commit = commit;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public User getCommitter() {
        return committer;
    }

    public void setCommitter(User committer) {
        this.committer = committer;
    }

    public ArrayList<RepoCommit> getParents() {
        return parents;
    }

    public void setParents(ArrayList<RepoCommit> parents) {
        this.parents = parents;
    }

    public ArrayList<CommitFile> getFiles() {
        return files;
    }

    public void setFiles(ArrayList<CommitFile> files) {
        this.files = files;
    }

    public CommitStats getStats() {
        return stats;
    }

    public void setStats(CommitStats stats) {
        this.stats = stats;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.sha);
        dest.writeString(this.url);
        dest.writeString(this.htmlUrl);
        dest.writeString(this.commentsUrl);
        dest.writeParcelable(this.commit, flags);
        dest.writeParcelable(this.author, flags);
        dest.writeParcelable(this.committer, flags);
        dest.writeList(this.parents);
        dest.writeTypedList(this.files);
        dest.writeParcelable(this.stats, flags);
    }

    public RepoCommit() {
    }

    protected RepoCommit(Parcel in) {
        this.sha = in.readString();
        this.url = in.readString();
        this.htmlUrl = in.readString();
        this.commentsUrl = in.readString();
        this.commit = in.readParcelable(CommitGitInfo.class.getClassLoader());
        this.author = in.readParcelable(User.class.getClassLoader());
        this.committer = in.readParcelable(User.class.getClassLoader());
        this.parents = new ArrayList<RepoCommit>();
        in.readList(this.parents, RepoCommit.class.getClassLoader());
        this.files = in.createTypedArrayList(CommitFile.CREATOR);
        this.stats = in.readParcelable(CommitStats.class.getClassLoader());
    }

    public static final Parcelable.Creator<RepoCommit> CREATOR = new Parcelable.Creator<RepoCommit>() {
        @Override
        public RepoCommit createFromParcel(Parcel source) {
            return new RepoCommit(source);
        }

        @Override
        public RepoCommit[] newArray(int size) {
            return new RepoCommit[size];
        }
    };

}
