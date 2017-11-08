

package com.thirtydegreesray.openhub.mvp.model;

import android.os.Parcel;

import java.util.ArrayList;

/**
 * Created by ThirtyDegreesRay on 2017/10/17 13:41:45
 */

public class RepoCommitExt extends RepoCommit{

    private ArrayList<CommitFile> files;
    private CommitStats stats;

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
        super.writeToParcel(dest, flags);
        dest.writeTypedList(this.files);
        dest.writeParcelable(this.stats, flags);
    }

    public RepoCommitExt() {
    }

    protected RepoCommitExt(Parcel in) {
        super(in);
        this.files = in.createTypedArrayList(CommitFile.CREATOR);
        this.stats = in.readParcelable(CommitStats.class.getClassLoader());
    }

    public static final Creator<RepoCommitExt> CREATOR = new Creator<RepoCommitExt>() {
        @Override
        public RepoCommitExt createFromParcel(Parcel source) {
            return new RepoCommitExt(source);
        }

        @Override
        public RepoCommitExt[] newArray(int size) {
            return new RepoCommitExt[size];
        }
    };
}
