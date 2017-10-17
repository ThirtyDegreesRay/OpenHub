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
