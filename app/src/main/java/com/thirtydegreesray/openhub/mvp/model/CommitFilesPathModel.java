

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
