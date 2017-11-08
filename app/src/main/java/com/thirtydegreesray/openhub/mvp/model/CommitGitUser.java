

package com.thirtydegreesray.openhub.mvp.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by ThirtyDegreesRay on 2017/10/17 14:05:31
 */

public class CommitGitUser implements Parcelable {

    private String name;
    private String email;
    private Date date;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.email);
        dest.writeLong(this.date != null ? this.date.getTime() : -1);
    }

    public CommitGitUser() {
    }

    protected CommitGitUser(Parcel in) {
        this.name = in.readString();
        this.email = in.readString();
        long tmpDate = in.readLong();
        this.date = tmpDate == -1 ? null : new Date(tmpDate);
    }

    public static final Parcelable.Creator<CommitGitUser> CREATOR = new Parcelable.Creator<CommitGitUser>() {
        @Override
        public CommitGitUser createFromParcel(Parcel source) {
            return new CommitGitUser(source);
        }

        @Override
        public CommitGitUser[] newArray(int size) {
            return new CommitGitUser[size];
        }
    };
}
