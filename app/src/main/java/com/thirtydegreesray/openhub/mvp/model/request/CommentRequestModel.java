package com.thirtydegreesray.openhub.mvp.model.request;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ThirtyDegreesRay on 2017/9/29 16:39:59
 */

public class CommentRequestModel implements Parcelable {

    private String body;

    public CommentRequestModel(String body) {
        this.body = body;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.body);
    }

    protected CommentRequestModel(Parcel in) {
        this.body = in.readString();
    }

    public static final Parcelable.Creator<CommentRequestModel> CREATOR = new Parcelable.Creator<CommentRequestModel>() {
        @Override
        public CommentRequestModel createFromParcel(Parcel source) {
            return new CommentRequestModel(source);
        }

        @Override
        public CommentRequestModel[] newArray(int size) {
            return new CommentRequestModel[size];
        }
    };

}
