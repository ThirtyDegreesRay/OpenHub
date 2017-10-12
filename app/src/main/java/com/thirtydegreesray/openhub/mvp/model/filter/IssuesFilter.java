package com.thirtydegreesray.openhub.mvp.model.filter;

import android.os.Parcel;
import android.os.Parcelable;

import com.thirtydegreesray.openhub.mvp.model.Issue;

/**
 * Created by ThirtyDegreesRay on 2017/9/26 11:20:36
 */

public class IssuesFilter implements Parcelable {

    public enum Type {
        Repo, User
    }

    public enum UserIssuesFilterType {
        All, Created, Assigned, Mentioned
    }

    public enum SortType {
        Created, Updated, Comments
    }

    private IssuesFilter.Type type;
    private Issue.IssueState issueState;
    private IssuesFilter.UserIssuesFilterType userIssuesFilterType = UserIssuesFilterType.All;
    private IssuesFilter.SortType sortType = SortType.Created;
    private SortDirection sortDirection = SortDirection.Desc;

    public IssuesFilter(Type type, Issue.IssueState issueState) {
        this.type = type;
        this.issueState = issueState;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public UserIssuesFilterType getUserIssuesFilterType() {
        return userIssuesFilterType;
    }

    public void setUserIssuesFilterType(UserIssuesFilterType userIssuesFilterType) {
        this.userIssuesFilterType = userIssuesFilterType;
    }

    public SortType getSortType() {
        return sortType;
    }

    public void setSortType(SortType sortType) {
        this.sortType = sortType;
    }

    public Issue.IssueState getIssueState() {
        return issueState;
    }

    public void setIssueState(Issue.IssueState issueState) {
        this.issueState = issueState;
    }

    public SortDirection getSortDirection() {
        return sortDirection;
    }

    public void setSortDirection(SortDirection sortDirection) {
        this.sortDirection = sortDirection;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.type == null ? -1 : this.type.ordinal());
        dest.writeInt(this.issueState == null ? -1 : this.issueState.ordinal());
        dest.writeInt(this.userIssuesFilterType == null ? -1 : this.userIssuesFilterType.ordinal());
        dest.writeInt(this.sortType == null ? -1 : this.sortType.ordinal());
        dest.writeInt(this.sortDirection == null ? -1 : this.sortDirection.ordinal());
    }

    public IssuesFilter() {
    }

    protected IssuesFilter(Parcel in) {
        int tmpType = in.readInt();
        this.type = tmpType == -1 ? null : Type.values()[tmpType];
        int tmpIssueState = in.readInt();
        this.issueState = tmpIssueState == -1 ? null : Issue.IssueState.values()[tmpIssueState];
        int tmpUserIssuesFilterType = in.readInt();
        this.userIssuesFilterType = tmpUserIssuesFilterType == -1 ? null : UserIssuesFilterType.values()[tmpUserIssuesFilterType];
        int tmpSortType = in.readInt();
        this.sortType = tmpSortType == -1 ? null : SortType.values()[tmpSortType];
        int tmpSortDirection = in.readInt();
        this.sortDirection = tmpSortDirection == -1 ? null : SortDirection.values()[tmpSortDirection];
    }

    public static final Creator<IssuesFilter> CREATOR = new Creator<IssuesFilter>() {
        @Override
        public IssuesFilter createFromParcel(Parcel source) {
            return new IssuesFilter(source);
        }

        @Override
        public IssuesFilter[] newArray(int size) {
            return new IssuesFilter[size];
        }
    };
}
