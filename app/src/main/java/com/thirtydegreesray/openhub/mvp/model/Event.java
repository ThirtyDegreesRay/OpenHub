/*
 *    Copyright 2017 ThirtyDegressRay
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.thirtydegreesray.openhub.mvp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by ThirtyDegreesRay on 2017/8/23 21:11:20
 */

public class Event implements Parcelable {

    public enum EventType{
        WatchEvent,
        CreateEvent,
        ForkEvent,
        PushEvent,
        PullRequestEvent,
        PullRequestReviewCommentEvent,
        //made repository public
        PublicEvent,
        IssuesEvent,
        IssueCommentEvent,
        MemberEvent,
    }

    private String id;
    private EventType type;
    private User actor;
    private Repository repo;
    private User org;
    private EventPayload payload;
    @SerializedName("public") private boolean isPublic;
    @SerializedName("created_at") private Date createdAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public User getActor() {
        return actor;
    }

    public void setActor(User actor) {
        this.actor = actor;
    }

    public Repository getRepo() {
        return repo;
    }

    public void setRepo(Repository repo) {
        this.repo = repo;
    }

    public User getOrg() {
        return org;
    }

    public void setOrg(User org) {
        this.org = org;
    }

    public EventPayload getPayload() {
        return payload;
    }

    public void setPayload(EventPayload payload) {
        this.payload = payload;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeInt(this.type == null ? -1 : this.type.ordinal());
        dest.writeParcelable(this.actor, flags);
        dest.writeParcelable(this.repo, flags);
        dest.writeParcelable(this.org, flags);
        dest.writeParcelable(this.payload, flags);
        dest.writeByte(this.isPublic ? (byte) 1 : (byte) 0);
        dest.writeLong(this.createdAt != null ? this.createdAt.getTime() : -1);
    }

    public Event() {
    }

    protected Event(Parcel in) {
        this.id = in.readString();
        int tmpType = in.readInt();
        this.type = tmpType == -1 ? null : EventType.values()[tmpType];
        this.actor = in.readParcelable(User.class.getClassLoader());
        this.repo = in.readParcelable(Repository.class.getClassLoader());
        this.org = in.readParcelable(User.class.getClassLoader());
        this.payload = in.readParcelable(EventPayload.class.getClassLoader());
        this.isPublic = in.readByte() != 0;
        long tmpCreatedAt = in.readLong();
        this.createdAt = tmpCreatedAt == -1 ? null : new Date(tmpCreatedAt);
    }

    public static final Parcelable.Creator<Event> CREATOR = new Parcelable.Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel source) {
            return new Event(source);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };
}