package com.thirtydegreesray.openhub.mvp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by ThirtyDegreesRay on 2017/9/20 13:51:47
 */

public class IssueEvent implements Parcelable {

    public enum IssueEventType{
        closed, commented, reopened
//      renamed,subscribed, unlocked, locked, labeled
    }

    private String id;
    private User user;
    @SerializedName("created_at") private Date createdAt;
    @SerializedName("updated_at") private Date updatedAt;
    @SerializedName("author_association") private Issue.IssueAuthorAssociation authorAssociation;
    private String body;
    @SerializedName("body_html") private String bodyHtml;
    @SerializedName("event") private IssueEventType type;
    @SerializedName("html_url") private String htmlUrl;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Issue.IssueAuthorAssociation getAuthorAssociation() {
        return authorAssociation;
    }

    public void setAuthorAssociation(Issue.IssueAuthorAssociation authorAssociation) {
        this.authorAssociation = authorAssociation;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public IssueEventType getType() {
        return type;
    }

    public void setType(IssueEventType type) {
        this.type = type;
    }

    public String getHtmlUrl() {
        return htmlUrl;
    }

    public void setHtmlUrl(String htmlUrl) {
        this.htmlUrl = htmlUrl;
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
        dest.writeParcelable(this.user, flags);
        dest.writeLong(this.createdAt != null ? this.createdAt.getTime() : -1);
        dest.writeLong(this.updatedAt != null ? this.updatedAt.getTime() : -1);
        dest.writeInt(this.authorAssociation == null ? -1 : this.authorAssociation.ordinal());
        dest.writeString(this.body);
        dest.writeInt(this.type == null ? -1 : this.type.ordinal());
        dest.writeString(this.htmlUrl);
        dest.writeString(this.bodyHtml);
    }

    public IssueEvent() {
    }

    protected IssueEvent(Parcel in) {
        this.id = in.readString();
        this.user = in.readParcelable(User.class.getClassLoader());
        long tmpCreatedAt = in.readLong();
        this.createdAt = tmpCreatedAt == -1 ? null : new Date(tmpCreatedAt);
        long tmpUpdatedAt = in.readLong();
        this.updatedAt = tmpUpdatedAt == -1 ? null : new Date(tmpUpdatedAt);
        int tmpAuthorAssociation = in.readInt();
        this.authorAssociation = tmpAuthorAssociation == -1 ? null : Issue.IssueAuthorAssociation.values()[tmpAuthorAssociation];
        this.body = in.readString();
        int tmpType = in.readInt();
        this.type = tmpType == -1 ? null : IssueEventType.values()[tmpType];
        this.htmlUrl = in.readString();
        this.bodyHtml = in.readString();
    }

    public static final Parcelable.Creator<IssueEvent> CREATOR = new Parcelable.Creator<IssueEvent>() {
        @Override
        public IssueEvent createFromParcel(Parcel source) {
            return new IssueEvent(source);
        }

        @Override
        public IssueEvent[] newArray(int size) {
            return new IssueEvent[size];
        }
    };
}
