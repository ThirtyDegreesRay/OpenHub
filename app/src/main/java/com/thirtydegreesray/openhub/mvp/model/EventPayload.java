

package com.thirtydegreesray.openhub.mvp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.thirtydegreesray.openhub.util.StringUtils;

import java.util.ArrayList;

/**
 * Created by ThirtyDegreesRay on 2017/8/23 21:33:09
 */

public class EventPayload implements Parcelable {

    public enum RefType{
        repository, branch, tag
    }

    public enum IssueEventActionType{
        assigned, unassigned, labeled, unlabeled, opened,
        edited, milestoned, demilestoned, closed, reopened
    }

    public enum MemberEventActionType{
        added, deleted, edited
    }

    public enum OrgBlockEventActionType{
        blocked, unblocked
    }

    public enum PullRequestReviewCommentEventActionType{
        created, edited, deleted
    }

    public enum PullRequestReviewEventActionType{
        submitted, edited, dismissed
    }

    //PushEvent
    @SerializedName("push_id") private String pushId;
    private int size;
    @SerializedName("distinct_size") private int distinctSize;
    private String ref; //PushEvent&CreateEvent
    private String head;
    private String before;
    private ArrayList<PushEventCommit> commits;

    //WatchEvent&PullRequestEvent
    private String action;

    //CreateEvent
    @SerializedName("ref_type") private RefType refType;
    @SerializedName("master_branch") private String masterBranch;
    private String description;
    @SerializedName("pusher_type") private String pusherType;

    //ReleaseEvent
    private Release release;
    //IssueCommentEvent
    private Issue issue;
    private IssueEvent comment;

    //MemberEvent
    private User member;

    private User organization;
    @SerializedName("blocked_user") private User blockedUser;

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

    public String getBranch(){
        return StringUtils.isBlank(ref) ? null : ref.substring(ref.lastIndexOf("/") + 1);
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

    public ArrayList<PushEventCommit> getCommits() {
        return commits;
    }

    public void setCommits(ArrayList<PushEventCommit> commits) {
        this.commits = commits;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public RefType getRefType() {
        return refType;
    }

    public void setRefType(RefType refType) {
        this.refType = refType;
    }

    public String getMasterBranch() {
        return masterBranch;
    }

    public void setMasterBranch(String masterBranch) {
        this.masterBranch = masterBranch;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPusherType() {
        return pusherType;
    }

    public void setPusherType(String pusherType) {
        this.pusherType = pusherType;
    }

    public Release getRelease() {
        return release;
    }

    public void setRelease(Release release) {
        this.release = release;
    }

    public Issue getIssue() {
        return issue;
    }

    public void setIssue(Issue issue) {
        this.issue = issue;
    }

    public IssueEvent getComment() {
        return comment;
    }

    public void setComment(IssueEvent comment) {
        this.comment = comment;
    }

    public User getMember() {
        return member;
    }

    public void setMember(User member) {
        this.member = member;
    }

    public User getOrganization() {
        return organization;
    }

    public void setOrganization(User organization) {
        this.organization = organization;
    }

    public User getBlockedUser() {
        return blockedUser;
    }

    public void setBlockedUser(User blockedUser) {
        this.blockedUser = blockedUser;
    }

    public EventPayload() {
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
        dest.writeInt(this.refType == null ? -1 : this.refType.ordinal());
        dest.writeString(this.masterBranch);
        dest.writeString(this.description);
        dest.writeString(this.pusherType);
        dest.writeParcelable(this.release, flags);
        dest.writeParcelable(this.issue, flags);
        dest.writeParcelable(this.comment, flags);
        dest.writeParcelable(this.member, flags);
        dest.writeParcelable(this.organization, flags);
        dest.writeParcelable(this.blockedUser, flags);
    }

    protected EventPayload(Parcel in) {
        this.pushId = in.readString();
        this.size = in.readInt();
        this.distinctSize = in.readInt();
        this.ref = in.readString();
        this.head = in.readString();
        this.before = in.readString();
        this.commits = in.createTypedArrayList(PushEventCommit.CREATOR);
        this.action = in.readString();
        int tmpRefType = in.readInt();
        this.refType = tmpRefType == -1 ? null : RefType.values()[tmpRefType];
        this.masterBranch = in.readString();
        this.description = in.readString();
        this.pusherType = in.readString();
        this.release = in.readParcelable(Release.class.getClassLoader());
        this.issue = in.readParcelable(Issue.class.getClassLoader());
        this.comment = in.readParcelable(IssueEvent.class.getClassLoader());
        this.member = in.readParcelable(User.class.getClassLoader());
        this.organization = in.readParcelable(User.class.getClassLoader());
        this.blockedUser = in.readParcelable(User.class.getClassLoader());
    }

    public static final Creator<EventPayload> CREATOR = new Creator<EventPayload>() {
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