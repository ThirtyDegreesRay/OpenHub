

package com.thirtydegreesray.openhub.mvp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ThirtyDegreesRay on 2017/10/17 13:44:37
 */

public class CommitGitInfo implements Parcelable {

    private String message;
    private String url;
    @SerializedName("comment_count") private int commentCount;
    private CommitGitUser author;
    private CommitGitUser committer;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public CommitGitUser getAuthor() {
        return author;
    }

    public void setAuthor(CommitGitUser author) {
        this.author = author;
    }

    public CommitGitUser getCommitter() {
        return committer;
    }

    public void setCommitter(CommitGitUser committer) {
        this.committer = committer;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.message);
        dest.writeString(this.url);
        dest.writeInt(this.commentCount);
        dest.writeParcelable(this.committer, flags);
        dest.writeParcelable(this.author, flags);
    }

    public CommitGitInfo() {
    }

    protected CommitGitInfo(Parcel in) {
        this.message = in.readString();
        this.url = in.readString();
        this.commentCount = in.readInt();
        this.committer = in.readParcelable(CommitGitUser.class.getClassLoader());
        this.author = in.readParcelable(CommitGitUser.class.getClassLoader());
    }

    public static final Parcelable.Creator<CommitGitInfo> CREATOR = new Parcelable.Creator<CommitGitInfo>() {
        @Override
        public CommitGitInfo createFromParcel(Parcel source) {
            return new CommitGitInfo(source);
        }

        @Override
        public CommitGitInfo[] newArray(int size) {
            return new CommitGitInfo[size];
        }
    };

}
