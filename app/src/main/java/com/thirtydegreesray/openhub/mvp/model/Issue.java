package com.thirtydegreesray.openhub.mvp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.thirtydegreesray.openhub.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by ThirtyDegreesRay on 2017/9/20 13:19:17
 */

public class Issue implements Parcelable {

    public enum IssueState{
        open, closed
    }

    public enum IssueAuthorAssociation{
        OWNER, CONTRIBUTOR, NONE
    }

    private String id;
    private int number;
    private String title;
    private IssueState state;
    private boolean locked;
    @SerializedName("comments") private int commentNum;

    @SerializedName("created_at") private Date createdAt;
    @SerializedName("updated_at") private Date updatedAt;
    @SerializedName("closed_at") private Date closedAt;
    private String body;
    @SerializedName("body_html") private String bodyHtml;

    private User user;
    @SerializedName("author_association") private IssueAuthorAssociation authorAssociation;
    @SerializedName("repository_url") private String repoUrl;
    @SerializedName("html_url") private String htmlUrl;

    private ArrayList<Label> labels;
    private User assignee;
    private ArrayList<User> assignees;
    private Milestone milestone;
    @SerializedName("closed_by") private User closedBy;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public IssueState getState() {
        return state;
    }

    public void setState(IssueState state) {
        this.state = state;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public int getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(int commentNum) {
        this.commentNum = commentNum;
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

    public Date getClosedAt() {
        return closedAt;
    }

    public void setClosedAt(Date closedAt) {
        this.closedAt = closedAt;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public IssueAuthorAssociation getAuthorAssociation() {
        return authorAssociation;
    }

    public void setAuthorAssociation(IssueAuthorAssociation authorAssociation) {
        this.authorAssociation = authorAssociation;
    }

    public String getRepoUrl() {
        return repoUrl;
    }

    public void setRepoUrl(String repoUrl) {
        this.repoUrl = repoUrl;
    }

    public String getRepoName() {
        return (!StringUtils.isBlank(repoUrl) && repoUrl.contains("/")) ?
                repoUrl.substring(repoUrl.lastIndexOf("/") + 1) : null;
    }

    public String getRepoFullName() {
        return (!StringUtils.isBlank(repoUrl) && repoUrl.contains("repos/")) ?
                repoUrl.substring(repoUrl.lastIndexOf("repos/") + 6) : null;
    }

    public String getRepoAuthorName() {
        return (!StringUtils.isBlank(repoUrl) && repoUrl.contains("repos/")) ?
                repoUrl.substring(repoUrl.lastIndexOf("repos/") + 6, repoUrl.lastIndexOf("/")) : null;
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

    public ArrayList<Label> getLabels() {
        return labels;
    }

    public void setLabels(ArrayList<Label> labels) {
        this.labels = labels;
    }

    public User getAssignee() {
        return assignee;
    }

    public void setAssignee(User assignee) {
        this.assignee = assignee;
    }

    public ArrayList<User> getAssignees() {
        return assignees;
    }

    public void setAssignees(ArrayList<User> assignees) {
        this.assignees = assignees;
    }

    public Milestone getMilestone() {
        return milestone;
    }

    public void setMilestone(Milestone milestone) {
        this.milestone = milestone;
    }

    public User getClosedBy() {
        return closedBy;
    }

    public void setClosedBy(User closedBy) {
        this.closedBy = closedBy;
    }

    public Issue() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeInt(this.number);
        dest.writeString(this.title);
        dest.writeInt(this.state == null ? -1 : this.state.ordinal());
        dest.writeByte(this.locked ? (byte) 1 : (byte) 0);
        dest.writeInt(this.commentNum);
        dest.writeLong(this.createdAt != null ? this.createdAt.getTime() : -1);
        dest.writeLong(this.updatedAt != null ? this.updatedAt.getTime() : -1);
        dest.writeLong(this.closedAt != null ? this.closedAt.getTime() : -1);
        dest.writeString(this.body);
        dest.writeString(this.bodyHtml);
        dest.writeParcelable(this.user, flags);
        dest.writeInt(this.authorAssociation == null ? -1 : this.authorAssociation.ordinal());
        dest.writeString(this.repoUrl);
        dest.writeString(this.htmlUrl);
        dest.writeTypedList(this.labels);
        dest.writeParcelable(this.assignee, flags);
        dest.writeTypedList(this.assignees);
        dest.writeParcelable(this.milestone, flags);
        dest.writeParcelable(this.closedBy, flags);
    }

    protected Issue(Parcel in) {
        this.id = in.readString();
        this.number = in.readInt();
        this.title = in.readString();
        int tmpState = in.readInt();
        this.state = tmpState == -1 ? null : IssueState.values()[tmpState];
        this.locked = in.readByte() != 0;
        this.commentNum = in.readInt();
        long tmpCreatedAt = in.readLong();
        this.createdAt = tmpCreatedAt == -1 ? null : new Date(tmpCreatedAt);
        long tmpUpdatedAt = in.readLong();
        this.updatedAt = tmpUpdatedAt == -1 ? null : new Date(tmpUpdatedAt);
        long tmpClosedAt = in.readLong();
        this.closedAt = tmpClosedAt == -1 ? null : new Date(tmpClosedAt);
        this.body = in.readString();
        this.bodyHtml = in.readString();
        this.user = in.readParcelable(User.class.getClassLoader());
        int tmpAuthorAssociation = in.readInt();
        this.authorAssociation = tmpAuthorAssociation == -1 ? null : IssueAuthorAssociation.values()[tmpAuthorAssociation];
        this.repoUrl = in.readString();
        this.htmlUrl = in.readString();
        this.labels = in.createTypedArrayList(Label.CREATOR);
        this.assignee = in.readParcelable(User.class.getClassLoader());
        this.assignees = in.createTypedArrayList(User.CREATOR);
        this.milestone = in.readParcelable(Milestone.class.getClassLoader());
        this.closedBy = in.readParcelable(User.class.getClassLoader());
    }

    public static final Creator<Issue> CREATOR = new Creator<Issue>() {
        @Override
        public Issue createFromParcel(Parcel source) {
            return new Issue(source);
        }

        @Override
        public Issue[] newArray(int size) {
            return new Issue[size];
        }
    };
}
