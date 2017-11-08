

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

    public String getSha() {
        return sha;
    }

    public String getShortSha() {
        return sha == null || sha.length() <= 7 ? sha : sha.substring(0, 7);
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
    }

    public static final Creator<RepoCommit> CREATOR = new Creator<RepoCommit>() {
        @Override
        public RepoCommit createFromParcel(Parcel in) {
            return new RepoCommit(in);
        }

        @Override
        public RepoCommit[] newArray(int size) {
            return new RepoCommit[size];
        }
    };

}
