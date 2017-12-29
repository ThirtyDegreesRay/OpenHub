package com.thirtydegreesray.openhub.mvp.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ThirtyDegreesRay on 2017/12/29 11:04:27
 */

public class Topic implements Parcelable {

    private String id;
    private String name;
    private String desc;
    private String image;

    public String getId() {
        return id;
    }

    public Topic setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Topic setName(String name) {
        this.name = name;
        return this;
    }

    public String getDesc() {
        return desc;
    }

    public Topic setDesc(String desc) {
        this.desc = desc;
        return this;
    }

    public String getImage() {
        return image;
    }

    public Topic setImage(String image) {
        this.image = image;
        return this;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.desc);
        dest.writeString(this.image);
    }

    public Topic() {
    }

    protected Topic(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.desc = in.readString();
        this.image = in.readString();
    }

    public static final Parcelable.Creator<Topic> CREATOR = new Parcelable.Creator<Topic>() {
        @Override
        public Topic createFromParcel(Parcel source) {
            return new Topic(source);
        }

        @Override
        public Topic[] newArray(int size) {
            return new Topic[size];
        }
    };

}
