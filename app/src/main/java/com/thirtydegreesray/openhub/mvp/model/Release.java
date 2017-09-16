package com.thirtydegreesray.openhub.mvp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

/**
 * Created by ThirtyDegreesRay on 2017/9/16 11:06:22
 */

public class Release implements Parcelable {

    private String id;
    @SerializedName("tag_name") private String tagName;
    @SerializedName("target_commitish") private String targetCommitish;
    private String name;
    private String body;
    @SerializedName("body_html") private String bodyHtml;
    @SerializedName("tarball_url") private String tarballUrl;
    @SerializedName("zipball_url") private String zipballUrl;

    private boolean draft;
    @SerializedName("prerelease") private boolean preRelease;
    @SerializedName("created_at") private Date createdAt;
    @SerializedName("published_at") private Date publishedAt;

    private User author;
    private List<ReleaseAsset> assets;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getTargetCommitish() {
        return targetCommitish;
    }

    public void setTargetCommitish(String targetCommitish) {
        this.targetCommitish = targetCommitish;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTarballUrl() {
        return tarballUrl;
    }

    public void setTarballUrl(String tarballUrl) {
        this.tarballUrl = tarballUrl;
    }

    public String getZipballUrl() {
        return zipballUrl;
    }

    public void setZipballUrl(String zipballUrl) {
        this.zipballUrl = zipballUrl;
    }

    public boolean isDraft() {
        return draft;
    }

    public void setDraft(boolean draft) {
        this.draft = draft;
    }

    public boolean isPreRelease() {
        return preRelease;
    }

    public void setPreRelease(boolean preRelease) {
        this.preRelease = preRelease;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(Date publishedAt) {
        this.publishedAt = publishedAt;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public List<ReleaseAsset> getAssets() {
        return assets;
    }

    public void setAssets(List<ReleaseAsset> assets) {
        this.assets = assets;
    }

    public String getBodyHtml() {
        return bodyHtml;
    }

    public void setBodyHtml(String bodyHtml) {
        this.bodyHtml = bodyHtml;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.tagName);
        dest.writeString(this.targetCommitish);
        dest.writeString(this.name);
        dest.writeString(this.body);
        dest.writeString(this.bodyHtml);
        dest.writeString(this.tarballUrl);
        dest.writeString(this.zipballUrl);
        dest.writeByte(this.draft ? (byte) 1 : (byte) 0);
        dest.writeByte(this.preRelease ? (byte) 1 : (byte) 0);
        dest.writeLong(this.createdAt != null ? this.createdAt.getTime() : -1);
        dest.writeLong(this.publishedAt != null ? this.publishedAt.getTime() : -1);
        dest.writeParcelable(this.author, flags);
        dest.writeTypedList(this.assets);
    }

    public Release() {
    }

    protected Release(Parcel in) {
        this.id = in.readString();
        this.tagName = in.readString();
        this.targetCommitish = in.readString();
        this.name = in.readString();
        this.body = in.readString();
        this.bodyHtml = in.readString();
        this.tarballUrl = in.readString();
        this.zipballUrl = in.readString();
        this.draft = in.readByte() != 0;
        this.preRelease = in.readByte() != 0;
        long tmpCreatedAt = in.readLong();
        this.createdAt = tmpCreatedAt == -1 ? null : new Date(tmpCreatedAt);
        long tmpPublishedAt = in.readLong();
        this.publishedAt = tmpPublishedAt == -1 ? null : new Date(tmpPublishedAt);
        this.author = in.readParcelable(User.class.getClassLoader());
        this.assets = in.createTypedArrayList(ReleaseAsset.CREATOR);
    }

    public static final Parcelable.Creator<Release> CREATOR = new Parcelable.Creator<Release>() {
        @Override
        public Release createFromParcel(Parcel source) {
            return new Release(source);
        }

        @Override
        public Release[] newArray(int size) {
            return new Release[size];
        }
    };

    //    {
//        "url": "https://api.github.com/repos/ThirtyDegreesRay/OpenHub/releases/7754776",
//        "assets_url": "https://api.github.com/repos/ThirtyDegreesRay/OpenHub/releases/7754776/assets",
//        "upload_url": "https://uploads.github.com/repos/ThirtyDegreesRay/OpenHub/releases/7754776/assets{?name,label}",
//        "html_url": "https://github.com/ThirtyDegreesRay/OpenHub/releases/tag/v1.0.2",
//        "id": 7754776,
//        "tag_name": "v1.0.2",
//        "target_commitish": "master",
//        "name": "v1.0.2",
//        "draft": false,
//        "author": {},
//        "prerelease": false,
//            "created_at": "2017-09-14T14:11:05Z",
//            "published_at": "2017-09-14T14:33:40Z",
//            "assets": [],
//        "tarball_url": "https://api.github.com/repos/ThirtyDegreesRay/OpenHub/tarball/v1.0.2",
//            "zipball_url": "https://api.github.com/repos/ThirtyDegreesRay/OpenHub/zipball/v1.0.2",
//            "body": "* 1.fix bug when bundle data is too large.\r\n* 2.fix bug when authorized is unable."
//    }

}
