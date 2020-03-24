package com.thirtydegreesray.openhub.mvp.model;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;

import com.thirtydegreesray.openhub.dao.MyTrendingLanguage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ThirtyDegreesRay on 2017/11/28 11:40:40
 */

public class TrendingLanguage implements Parcelable {

    private String name;
    private String slug;
    private int order;
    private boolean selected;

    public TrendingLanguage(String name, String slug) {
        this.name = name;
        this.slug = slug;
    }

    public static ArrayList<TrendingLanguage> generateFromDB(@NonNull List<MyTrendingLanguage> myLanguages){
        ArrayList<TrendingLanguage> languages = new ArrayList<>();
        for(MyTrendingLanguage myTrendingLanguage : myLanguages){
            languages.add(generateFromDB(myTrendingLanguage));
        }
        return languages;
    }

    public static TrendingLanguage generateFromDB(@NonNull MyTrendingLanguage myTrendingLanguage){
        TrendingLanguage language = new TrendingLanguage();
        language.setSlug(myTrendingLanguage.getSlug());
        language.setName(myTrendingLanguage.getName());
        language.setOrder(myTrendingLanguage.getOrder());
        return language;
    }

    public static MyTrendingLanguage generateDB(@NonNull TrendingLanguage trendingLanguage, int order){
        MyTrendingLanguage myTrendingLanguage = new MyTrendingLanguage();
        myTrendingLanguage.setName(trendingLanguage.getName());
        myTrendingLanguage.setOrder(order);
        myTrendingLanguage.setSlug(trendingLanguage.getSlug());
        return myTrendingLanguage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.slug);
        dest.writeInt(this.order);
        dest.writeByte(this.selected ? (byte) 1 : (byte) 0);
    }

    public TrendingLanguage() {
    }

    protected TrendingLanguage(Parcel in) {
        this.name = in.readString();
        this.slug = in.readString();
        this.order = in.readInt();
        this.selected = in.readByte() != 0;
    }

    public static final Parcelable.Creator<TrendingLanguage> CREATOR = new Parcelable.Creator<TrendingLanguage>() {
        @Override
        public TrendingLanguage createFromParcel(Parcel source) {
            return new TrendingLanguage(source);
        }

        @Override
        public TrendingLanguage[] newArray(int size) {
            return new TrendingLanguage[size];
        }
    };

    @Override
    public boolean equals(Object obj) {
        if(obj != null && obj instanceof TrendingLanguage){
            TrendingLanguage compare = (TrendingLanguage) obj;
            return slug == null ? slug == compare.getSlug() : slug.equals(compare.getSlug());
        }
        return super.equals(obj);
    }
}
