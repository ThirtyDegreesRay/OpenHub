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
 * Created by ThirtyDegreesRay on 2017/10/18 16:11:17
 */

public class CommitFilesPathModel implements Parcelable {

    private String path;

    public String getPath() {
        return path;
    }

    public CommitFilesPathModel setPath(String path) {
        this.path = path;
        return this;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.path);
    }

    public CommitFilesPathModel() {
    }

    protected CommitFilesPathModel(Parcel in) {
        this.path = in.readString();
    }

    public static final Parcelable.Creator<CommitFilesPathModel> CREATOR = new Parcelable.Creator<CommitFilesPathModel>() {
        @Override
        public CommitFilesPathModel createFromParcel(Parcel source) {
            return new CommitFilesPathModel(source);
        }

        @Override
        public CommitFilesPathModel[] newArray(int size) {
            return new CommitFilesPathModel[size];
        }
    };
}
