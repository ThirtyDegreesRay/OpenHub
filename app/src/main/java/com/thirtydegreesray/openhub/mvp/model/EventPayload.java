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

import java.util.ArrayList;

/**
 * Created by ThirtyDegreesRay on 2017/8/23 21:33:09
 */

public class EventPayload implements Parcelable {

    //PushEvent
    @SerializedName("push_id") private String pushId;
    private int size;
    @SerializedName("distinct_size") private int distinctSize;
    private String ref; //PushEvent&CreateEvent
    private String head;
    private String before;
    private ArrayList<Commit> commits;

    //WatchEvent&PullRequestEvent
    private String action;

    //CreateEvent
    @SerializedName("ref_type") private String refType;
    @SerializedName("master_branch") private String masterBranch;
    private String description;
    @SerializedName("pusher_type") private String pusherType;

    //ForkEvent,PublicEvent None


    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getDistinctSize() {
        return distinctSize;
    }

    public void setDistinctSize(int distinctSize) {
        this.distinctSize = distinctSize;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getBefore() {
        return before;
    }

    public void setBefore(String before) {
        this.before = before;
    }

    public ArrayList<Commit> getCommits() {
        return commits;
    }

    public void setCommits(ArrayList<Commit> commits) {
        this.commits = commits;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.pushId);
        dest.writeInt(this.size);
        dest.writeInt(this.distinctSize);
        dest.writeString(this.ref);
        dest.writeString(this.head);
        dest.writeString(this.before);
        dest.writeTypedList(this.commits);
        dest.writeString(this.action);
        dest.writeString(this.refType);
        dest.writeString(this.masterBranch);
        dest.writeString(this.description);
        dest.writeString(this.pusherType);
    }

    public EventPayload() {
    }

    protected EventPayload(Parcel in) {
        this.pushId = in.readString();
        this.size = in.readInt();
        this.distinctSize = in.readInt();
        this.ref = in.readString();
        this.head = in.readString();
        this.before = in.readString();
        this.commits = in.createTypedArrayList(Commit.CREATOR);
        this.action = in.readString();
        this.refType = in.readString();
        this.masterBranch = in.readString();
        this.description = in.readString();
        this.pusherType = in.readString();
    }

    public static final Parcelable.Creator<EventPayload> CREATOR = new Parcelable.Creator<EventPayload>() {
        @Override
        public EventPayload createFromParcel(Parcel source) {
            return new EventPayload(source);
        }

        @Override
        public EventPayload[] newArray(int size) {
            return new EventPayload[size];
        }
    };
}