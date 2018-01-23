package com.thirtydegreesray.openhub.mvp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by ThirtyDegreesRay on 2017/9/20 13:51:47
 */

public class IssueEvent implements Parcelable {

    private String id;
    private User user;
    @SerializedName("created_at") private Date createdAt;
    @SerializedName("updated_at") private Date updatedAt;
    @SerializedName("author_association") private Issue.IssueAuthorAssociation authorAssociation;
    private String body;
    @SerializedName("body_html") private String bodyHtml;
    @SerializedName("event") private Type type;
    @SerializedName("html_url") private String htmlUrl;

    private User assignee;
    private User assigner;
    private User actor;
    private Label label;
    private Milestone milestone;
    private Reactions reactions;
    private IssueCrossReferencedSource source;

    private Issue parentIssue;

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

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
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

    public User getAssignee() {
        return assignee;
    }

    public void setAssignee(User assignee) {
        this.assignee = assignee;
    }

    public User getAssigner() {
        return assigner;
    }

    public void setAssigner(User assigner) {
        this.assigner = assigner;
    }

    public User getActor() {
        return actor;
    }

    public void setActor(User actor) {
        this.actor = actor;
    }

    public Label getLabel() {
        return label;
    }

    public void setLabel(Label label) {
        this.label = label;
    }

    public Milestone getMilestone() {
        return milestone;
    }

    public void setMilestone(Milestone milestone) {
        this.milestone = milestone;
    }

    public Reactions getReactions() {
        return reactions;
    }

    public void setReactions(Reactions reactions) {
        this.reactions = reactions;
    }

    public IssueCrossReferencedSource getSource() {
        return source;
    }

    public void setSource(IssueCrossReferencedSource source) {
        this.source = source;
    }

    public Issue getParentIssue() {
        return parentIssue;
    }

    public void setParentIssue(Issue parentIssue) {
        this.parentIssue = parentIssue;
    }

    public IssueEvent() {
    }

    public enum Type {
        /**
         * The issue was closed by the actor. When the commit_id is present, it identifies the
         * commit that closed the issue using "closes / fixes #NN" syntax.
         */
        closed,
        /**
         * The issue was reopened by the actor.
         */
        reopened,
        commented,
        @SerializedName("comment_deleted") commentDeleted,


        /**
         * The issue title was changed.
         */
        renamed,
        /**
         * The issue was locked by the actor.
         */
        locked,
        /**
         * The issue was unlocked by the actor.
         */
        unlocked,

        /**
         * The issue was referenced from another issue. The `source` attribute contains the `id`,
         * `actor`, and `url` of the reference's source.
         */
        @SerializedName("cross-referenced") crossReferenced,


        /**
         * The issue was assigned to the actor.
         */
        assigned,
        /**
         * The actor was unassigned from the issue.
         */
        unassigned,
        /**
         * A label was added to the issue.
         */
        labeled,
        /**
         * A label was removed from the issue.
         */
        unlabeled,
        /**
         * The issue was added to a milestone.
         */
        milestoned,
        /**
         * The issue was removed from a milestone.
         */
        demilestoned,
        /**
         * The issue was added to a project board.
         */
//        @SerializedName("added_to_project") addedToProject,
        /**
         * The issue was moved between columns in a project board.
         */
//        @SerializedName("moved_columns_in_project") movedColumnsInProject,
        /**
         * The issue was removed from a project board.
         */
//        @SerializedName("removed_from_project") removedFromProject,


        /**
         * A commit was added to the pull request's `HEAD` branch. Only provided for pull requests.
         */
//        committed,
        /**
         * The issue was merged by the actor. The `commit_id` attribute is the SHA1 of
         * the HEAD commit that was merged.
         */
//        merged,
        /**
         * The pull request's branch was deleted.
         */
//        @SerializedName("head_ref_deleted") headRefDeleted,
        /**
         * The pull request's branch was restored.
         */
//        @SerializedName("head_ref_restored") headRefRestored,
        /**
         * The actor dismissed a review from the pull request.
         */
//        @SerializedName("review_dismissed") reviewDismissed,
        /**
         * The actor requested review from the subject on this pull request.
         */
//        @SerializedName("review_requested") reviewRequested,
        /**
         * The actor removed the review request for the subject on this pull request.
         */
//        @SerializedName("review_request_removed") reviewRequestRemoved,
        /**
         * A user with write permissions marked an issue as a duplicate of another issue or a
         * pull request as a duplicate of another pull request.
         */
//        @SerializedName("marked_as_duplicate") markedAsDuplicate,
        /**
         * An issue that a user had previously marked as a duplicate of another issue
         * is no longer considered a duplicate, or a pull request that a user had previously
         * marked as a duplicate of another pull request is no longer considered a duplicate.
         */
//        @SerializedName("unmarked_as_duplicate") unmarkedAsDuplicate,
        /**
         * The issue was created by converting a note in a project board to an issue.
         */
//        @SerializedName("converted_note_to_issue") convertedNoteToIssue,

        //unable
        /**
         * The issue was referenced from a commit message. The `commit_id` attribute is
         * the commit SHA1 of where that happened.
         */
//        referenced,
        /**
         * The actor subscribed to receive notifications for an issue.
         */
//        subscribed,
        /**
         * The actor unsubscribed to stop receiving notifications for an issue.
         */
//        unsubscribed,
        /**
         * The actor was @mentioned in an issue body.
         */
//        mentioned,
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
        dest.writeString(this.bodyHtml);
        dest.writeInt(this.type == null ? -1 : this.type.ordinal());
        dest.writeString(this.htmlUrl);
        dest.writeParcelable(this.assignee, flags);
        dest.writeParcelable(this.assigner, flags);
        dest.writeParcelable(this.actor, flags);
        dest.writeParcelable(this.label, flags);
        dest.writeParcelable(this.milestone, flags);
        dest.writeParcelable(this.reactions, flags);
        dest.writeParcelable(this.source, flags);
        dest.writeParcelable(this.parentIssue, flags);
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
        this.bodyHtml = in.readString();
        int tmpType = in.readInt();
        this.type = tmpType == -1 ? null : Type.values()[tmpType];
        this.htmlUrl = in.readString();
        this.assignee = in.readParcelable(User.class.getClassLoader());
        this.assigner = in.readParcelable(User.class.getClassLoader());
        this.actor = in.readParcelable(User.class.getClassLoader());
        this.label = in.readParcelable(Label.class.getClassLoader());
        this.milestone = in.readParcelable(Milestone.class.getClassLoader());
        this.reactions = in.readParcelable(Reactions.class.getClassLoader());
        this.source = in.readParcelable(IssueCrossReferencedSource.class.getClassLoader());
        this.parentIssue = in.readParcelable(Issue.class.getClassLoader());
    }

    public static final Creator<IssueEvent> CREATOR = new Creator<IssueEvent>() {
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
