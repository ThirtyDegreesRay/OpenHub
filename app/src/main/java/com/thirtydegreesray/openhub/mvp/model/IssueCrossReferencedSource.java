package com.thirtydegreesray.openhub.mvp.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ThirtyDegreesRay on 2018/1/22 16:21:44
 */

public class IssueCrossReferencedSource implements Parcelable {

    enum Type{
        issue, commit
    }

    private Type type;
    private Issue issue;

    public Type getType() {
        return type;
    }

    public Issue getIssue() {
        return issue;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setIssue(Issue issue) {
        this.issue = issue;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.type == null ? -1 : this.type.ordinal());
        dest.writeParcelable(this.issue, flags);
    }

    public IssueCrossReferencedSource() {
    }

    protected IssueCrossReferencedSource(Parcel in) {
        int tmpType = in.readInt();
        this.type = tmpType == -1 ? null : Type.values()[tmpType];
        this.issue = in.readParcelable(Issue.class.getClassLoader());
    }

    public static final Parcelable.Creator<IssueCrossReferencedSource> CREATOR = new Parcelable.Creator<IssueCrossReferencedSource>() {
        @Override
        public IssueCrossReferencedSource createFromParcel(Parcel source) {
            return new IssueCrossReferencedSource(source);
        }

        @Override
        public IssueCrossReferencedSource[] newArray(int size) {
            return new IssueCrossReferencedSource[size];
        }
    };
}
