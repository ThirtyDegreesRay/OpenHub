package com.thirtydegreesray.openhub.mvp.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by ThirtyDegreesRay on 2017/12/6 15:59:31
 */

@Root(name = "entry")
public class WikiModel implements Parcelable {

    @Element(name = "id") private String id;
    @Element(name = "published") private String published;
    @Element(name = "updated") private String updated;
    @Element(name = "content") private String content;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPublished() {
        return published;
    }

    public void setPublished(String published) {
        this.published = published;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getContent() {
        return content;
    }

    public String getContentWithTitle() {
        String titleHtml = "<h1>" + getName() + "</h1>";
        return titleHtml + content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getName(){
        if(id != null && id.contains("wiki")){
            int start = id.indexOf("wiki/") + 5;
            int end = id.lastIndexOf("/");
            if(end > start){
                return id.substring(start, end).replaceAll("-", " ");
            } else {
                return "Home";
            }
        } else {
            return null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.published);
        dest.writeString(this.updated);
        dest.writeString(this.content);
    }

    public WikiModel() {
    }

    protected WikiModel(Parcel in) {
        this.id = in.readString();
        this.published = in.readString();
        this.updated = in.readString();
        this.content = in.readString();
    }

    public static final Creator<WikiModel> CREATOR = new Creator<WikiModel>() {
        @Override
        public WikiModel createFromParcel(Parcel source) {
            return new WikiModel(source);
        }

        @Override
        public WikiModel[] newArray(int size) {
            return new WikiModel[size];
        }
    };

}