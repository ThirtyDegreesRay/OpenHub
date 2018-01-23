package com.thirtydegreesray.openhub.mvp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ThirtyDegreesRay on 2018/1/16 13:39:14
 */

public class Reactions implements Parcelable {

    @SerializedName("total_count") private int totalCount;
    @SerializedName("+1") private int plusOne;
    @SerializedName("-1") private int minusOne;
    private int laugh;
    private int hooray;
    private int confused;
    private int heart;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getPlusOne() {
        return plusOne;
    }

    public void setPlusOne(int plusOne) {
        this.plusOne = plusOne;
    }

    public int getMinusOne() {
        return minusOne;
    }

    public void setMinusOne(int minusOne) {
        this.minusOne = minusOne;
    }

    public int getLaugh() {
        return laugh;
    }

    public void setLaugh(int laugh) {
        this.laugh = laugh;
    }

    public int getHooray() {
        return hooray;
    }

    public void setHooray(int hooray) {
        this.hooray = hooray;
    }

    public int getConfused() {
        return confused;
    }

    public void setConfused(int confused) {
        this.confused = confused;
    }

    public int getHeart() {
        return heart;
    }

    public void setHeart(int heart) {
        this.heart = heart;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.totalCount);
        dest.writeInt(this.plusOne);
        dest.writeInt(this.minusOne);
        dest.writeInt(this.laugh);
        dest.writeInt(this.hooray);
        dest.writeInt(this.confused);
        dest.writeInt(this.heart);
    }

    public Reactions() {
    }

    protected Reactions(Parcel in) {
        this.totalCount = in.readInt();
        this.plusOne = in.readInt();
        this.minusOne = in.readInt();
        this.laugh = in.readInt();
        this.hooray = in.readInt();
        this.confused = in.readInt();
        this.heart = in.readInt();
    }

    public static final Parcelable.Creator<Reactions> CREATOR = new Parcelable.Creator<Reactions>() {
        @Override
        public Reactions createFromParcel(Parcel source) {
            return new Reactions(source);
        }

        @Override
        public Reactions[] newArray(int size) {
            return new Reactions[size];
        }
    };
}
