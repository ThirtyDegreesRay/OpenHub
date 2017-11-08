

package com.thirtydegreesray.openhub.mvp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by ThirtyDegreesRay on 2017/11/6 20:06:45
 */

public class Notification implements Parcelable {

    public enum Type{
        @SerializedName("updated_at") Assign, //You were assigned to the Issue.
        @SerializedName("author") Author, //You created the thread.
        @SerializedName("comment") Comment, //You commented on the thread.
        @SerializedName("invitation") Invitation, //You accepted an invitation to contribute to the repository.
        @SerializedName("manual") Manual, //You subscribed to the thread (via an Issue or Pull Request).
        @SerializedName("mention") Mention, //You were specifically @mentioned in the content.
        @SerializedName("state_change") StateChange, //You changed the thread state (for example, closing an Issue or merging a Pull Request).
        @SerializedName("subscribed") Subscribed, //You're watching the repository.
        @SerializedName("team_mention") TeamMention //You were on a team that was mentioned.
    }

    private String id;
    private boolean unread;
    private Type reason;
    @SerializedName("updated_at") private Date updateAt;
    @SerializedName("last_read_at") private Date lastReadAt;
    private Repository repository;
    private NotificationSubject subject;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isUnread() {
        return unread;
    }

    public void setUnread(boolean unread) {
        this.unread = unread;
    }

    public Type getReason() {
        return reason;
    }

    public void setReason(Type reason) {
        this.reason = reason;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }

    public Date getLastReadAt() {
        return lastReadAt;
    }

    public void setLastReadAt(Date lastReadAt) {
        this.lastReadAt = lastReadAt;
    }

    public Repository getRepository() {
        return repository;
    }

    public void setRepository(Repository repository) {
        this.repository = repository;
    }

    public NotificationSubject getSubject() {
        return subject;
    }

    public void setSubject(NotificationSubject subject) {
        this.subject = subject;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeByte(this.unread ? (byte) 1 : (byte) 0);
        dest.writeInt(this.reason == null ? -1 : this.reason.ordinal());
        dest.writeLong(this.updateAt != null ? this.updateAt.getTime() : -1);
        dest.writeLong(this.lastReadAt != null ? this.lastReadAt.getTime() : -1);
        dest.writeParcelable(this.repository, flags);
        dest.writeParcelable(this.subject, flags);
    }

    public Notification() {
    }

    protected Notification(Parcel in) {
        this.id = in.readString();
        this.unread = in.readByte() != 0;
        int tmpReason = in.readInt();
        this.reason = tmpReason == -1 ? null : Type.values()[tmpReason];
        long tmpUpdateAt = in.readLong();
        this.updateAt = tmpUpdateAt == -1 ? null : new Date(tmpUpdateAt);
        long tmpLastReadAt = in.readLong();
        this.lastReadAt = tmpLastReadAt == -1 ? null : new Date(tmpLastReadAt);
        this.repository = in.readParcelable(Repository.class.getClassLoader());
        this.subject = in.readParcelable(NotificationSubject.class.getClassLoader());
    }

    public static final Parcelable.Creator<Notification> CREATOR = new Parcelable.Creator<Notification>() {
        @Override
        public Notification createFromParcel(Parcel source) {
            return new Notification(source);
        }

        @Override
        public Notification[] newArray(int size) {
            return new Notification[size];
        }
    };

}
