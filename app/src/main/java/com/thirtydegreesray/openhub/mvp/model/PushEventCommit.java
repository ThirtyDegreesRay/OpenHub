

package com.thirtydegreesray.openhub.mvp.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ThirtyDegreesRay on 2017/8/23 21:41:08
 * Commit model for PushEvent
 */

public class PushEventCommit implements Parcelable {

    private String sha;
    //email&name
    private User author;
    private String message;
    private boolean distinct;
    private String url;

    public String getSha() {
        return sha;
    }

    public void setSha(String sha) {
        this.sha = sha;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.sha);
        dest.writeParcelable(this.author, flags);
        dest.writeString(this.message);
        dest.writeByte(this.distinct ? (byte) 1 : (byte) 0);
        dest.writeString(this.url);
    }

    public PushEventCommit() {
    }

    protected PushEventCommit(Parcel in) {
        this.sha = in.readString();
        this.author = in.readParcelable(User.class.getClassLoader());
        this.message = in.readString();
        this.distinct = in.readByte() != 0;
        this.url = in.readString();
    }

    public static final Parcelable.Creator<PushEventCommit> CREATOR = new Parcelable.Creator<PushEventCommit>() {
        @Override
        public PushEventCommit createFromParcel(Parcel source) {
            return new PushEventCommit(source);
        }

        @Override
        public PushEventCommit[] newArray(int size) {
            return new PushEventCommit[size];
        }
    };
}
