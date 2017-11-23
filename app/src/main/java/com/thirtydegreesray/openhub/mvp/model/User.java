

package com.thirtydegreesray.openhub.mvp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.thirtydegreesray.openhub.dao.BookMarkUser;
import com.thirtydegreesray.openhub.dao.LocalUser;
import com.thirtydegreesray.openhub.dao.TraceUser;

import java.util.Date;

/**
 * Created on 2017/7/14.
 *
 * @author ThirtyDegreesRay
 */

public class User implements Parcelable {

    public enum UserType{
        User, Organization
    }

    private String login;
    private String id;
    private String name;
    @SerializedName("avatar_url") private String avatarUrl;
    @SerializedName("html_url") private String htmlUrl;
    private UserType type;
    private String company;
    private String blog;
    private String location;
    private String email;
    private String bio;

    @SerializedName("public_repos") private int publicRepos;
    @SerializedName("public_gists") private int publicGists;
    private int followers;
    private int following;
    @SerializedName("created_at") private Date createdAt;
    @SerializedName("updated_at") private Date updatedAt;

    public User() {

    }

    public LocalUser toLocalUser(){
        LocalUser localUser = new LocalUser();
        localUser.setLogin(login);
        localUser.setName(name);
        localUser.setAvatarUrl(avatarUrl);
        localUser.setFollowers(followers);
        localUser.setFollowing(following);
        return localUser;
    }

    public static User generateFromLocalUser(LocalUser localUser){
        User user = new User();
        user.setLogin(localUser.getLogin());
        user.setName(localUser.getName());
        user.setFollowers(localUser.getFollowers());
        user.setFollowing(localUser.getFollowing());
        user.setAvatarUrl(localUser.getAvatarUrl());
        return user;
    }

    public static User generateFromTrace(TraceUser trace){
        User user = new User();
        user.setLogin(trace.getLogin());
        user.setName(trace.getName());
        user.setFollowers(trace.getFollowers());
        user.setFollowing(trace.getFollowing());
        user.setAvatarUrl(trace.getAvatarUrl());
        return user;
    }

    public static User generateFromBookmark(BookMarkUser bookMark){
        User user = new User();
        user.setLogin(bookMark.getLogin());
        user.setName(bookMark.getName());
        user.setFollowers(bookMark.getFollowers());
        user.setFollowing(bookMark.getFollowing());
        user.setAvatarUrl(bookMark.getAvatarUrl());
        return user;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getHtmlUrl() {
        return htmlUrl;
    }

    public void setHtmlUrl(String htmlUrl) {
        this.htmlUrl = htmlUrl;
    }

    public UserType getType() {
        return type;
    }

    public void setType(UserType type) {
        this.type = type;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getBlog() {
        return blog;
    }

    public void setBlog(String blog) {
        this.blog = blog;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public int getPublicRepos() {
        return publicRepos;
    }

    public void setPublicRepos(int publicRepos) {
        this.publicRepos = publicRepos;
    }

    public int getPublicGists() {
        return publicGists;
    }

    public void setPublicGists(int publicGists) {
        this.publicGists = publicGists;
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public int getFollowing() {
        return following;
    }

    public void setFollowing(int following) {
        this.following = following;
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

    public boolean isUser(){
        return UserType.User.equals(type);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.login);
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.avatarUrl);
        dest.writeString(this.htmlUrl);
        dest.writeInt(this.type == null ? -1 : this.type.ordinal());
        dest.writeString(this.company);
        dest.writeString(this.blog);
        dest.writeString(this.location);
        dest.writeString(this.email);
        dest.writeString(this.bio);
        dest.writeInt(this.publicRepos);
        dest.writeInt(this.publicGists);
        dest.writeInt(this.followers);
        dest.writeInt(this.following);
        dest.writeLong(this.createdAt != null ? this.createdAt.getTime() : -1);
        dest.writeLong(this.updatedAt != null ? this.updatedAt.getTime() : -1);
    }

    protected User(Parcel in) {
        this.login = in.readString();
        this.id = in.readString();
        this.name = in.readString();
        this.avatarUrl = in.readString();
        this.htmlUrl = in.readString();
        int tmpType = in.readInt();
        this.type = tmpType == -1 ? null : UserType.values()[tmpType];
        this.company = in.readString();
        this.blog = in.readString();
        this.location = in.readString();
        this.email = in.readString();
        this.bio = in.readString();
        this.publicRepos = in.readInt();
        this.publicGists = in.readInt();
        this.followers = in.readInt();
        this.following = in.readInt();
        long tmpCreatedAt = in.readLong();
        this.createdAt = tmpCreatedAt == -1 ? null : new Date(tmpCreatedAt);
        long tmpUpdatedAt = in.readLong();
        this.updatedAt = tmpUpdatedAt == -1 ? null : new Date(tmpUpdatedAt);
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public boolean equals(Object obj) {
        if(obj != null && obj instanceof User){
            return ((User)obj).getLogin().equals(login);
        }
        return super.equals(obj);
    }
}
