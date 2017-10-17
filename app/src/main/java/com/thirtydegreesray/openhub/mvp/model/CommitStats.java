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

/**
 * Created by ThirtyDegreesRay on 2017/10/17 13:54:24
 */

public class CommitStats implements Parcelable {

    private int total;
    private int additions;
    private int deletions;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getAdditions() {
        return additions;
    }

    public void setAdditions(int additions) {
        this.additions = additions;
    }

    public int getDeletions() {
        return deletions;
    }

    public void setDeletions(int deletions) {
        this.deletions = deletions;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.total);
        dest.writeInt(this.additions);
        dest.writeInt(this.deletions);
    }

    public CommitStats() {
    }

    protected CommitStats(Parcel in) {
        this.total = in.readInt();
        this.additions = in.readInt();
        this.deletions = in.readInt();
    }

    public static final Parcelable.Creator<CommitStats> CREATOR = new Parcelable.Creator<CommitStats>() {
        @Override
        public CommitStats createFromParcel(Parcel source) {
            return new CommitStats(source);
        }

        @Override
        public CommitStats[] newArray(int size) {
            return new CommitStats[size];
        }
    };
}
