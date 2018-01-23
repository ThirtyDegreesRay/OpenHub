package com.thirtydegreesray.openhub.mvp.model;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ThirtyDegreesRay on 2018/1/10 16:05:39
 */

public class Label implements Parcelable {

    private long id;
    private String name;
    private String color;
    @SerializedName("default") private boolean isDefault;

    private boolean select;

    public Label(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public int getColorValue() {
        try{
            return Color.parseColor("#" + color);
        } catch (IllegalArgumentException e){
            e.printStackTrace();
            return 0;
        }
    }

    public Label() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.name);
        dest.writeString(this.color);
        dest.writeByte(this.isDefault ? (byte) 1 : (byte) 0);
        dest.writeByte(this.select ? (byte) 1 : (byte) 0);
    }

    protected Label(Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
        this.color = in.readString();
        this.isDefault = in.readByte() != 0;
        this.select = in.readByte() != 0;
    }

    public static final Creator<Label> CREATOR = new Creator<Label>() {
        @Override
        public Label createFromParcel(Parcel source) {
            return new Label(source);
        }

        @Override
        public Label[] newArray(int size) {
            return new Label[size];
        }
    };

    @Override
    public boolean equals(Object obj) {
        if(obj != null && obj instanceof Label){
            return ((Label) obj).getId() == id;
        }
        return super.equals(obj);
    }
}
