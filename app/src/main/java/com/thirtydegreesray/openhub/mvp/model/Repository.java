

package com.thirtydegreesray.openhub.mvp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.thirtydegreesray.openhub.dao.BookMarkRepo;
import com.thirtydegreesray.openhub.dao.LocalRepo;
import com.thirtydegreesray.openhub.dao.TraceRepo;
import com.thirtydegreesray.openhub.mvp.model.filter.TrendingSince;

import java.util.Date;

/**
 * Created on 2017/7/18.
 *
 * @author ThirtyDegreesRay
 */

public class Repository implements Parcelable {

    private int id;
    private String name;
    @SerializedName("full_name") private String fullName;
    @SerializedName("private") private boolean repPrivate;
    @SerializedName("html_url") private String htmlUrl;
    private String description;
    private String language;
    private User owner;

    @SerializedName("default_branch") private String defaultBranch ;

    @SerializedName("created_at") private Date createdAt ;
    @SerializedName("updated_at") private Date updatedAt ;
    @SerializedName("pushed_at") private Date pushedAt ;

    @SerializedName("git_url") private String gitUrl ;
    @SerializedName("ssh_url") private String sshUrl ;
    @SerializedName("clone_url") private String cloneUrl ;
    @SerializedName("svn_url") private String svnUrl ;

    private long size ;
    @SerializedName("stargazers_count") private int stargazersCount ;
    @SerializedName("watchers_count") private int watchersCount ;
    @SerializedName("forks_count") private int forksCount ;
    @SerializedName("open_issues_count") private int openIssuesCount ;
    @SerializedName("subscribers_count") private int subscribersCount ;

    private boolean fork;
    private Repository parent;
    private RepositoryPermissions permissions;

    @SerializedName("has_issues") private boolean hasIssues;
    @SerializedName("has_projects") private boolean hasProjects;
    @SerializedName("has_downloads") private boolean hasDownloads;
    @SerializedName("has_wiki") private boolean hasWiki;
    @SerializedName("has_pages") private boolean hasPages;

    private int sinceStargazersCount ;
    private TrendingSince since;

    public Repository() {
    }

    public LocalRepo toLocalRepo(){
        LocalRepo localRepo = new LocalRepo();
        localRepo.setId(id);
        localRepo.setName(name);
        localRepo.setDescription(description);
        localRepo.setLanguage(language);
        localRepo.setStargazersCount(stargazersCount);
        localRepo.setWatchersCount(watchersCount);
        localRepo.setForksCount(forksCount);
        localRepo.setFork(fork);
        localRepo.setOwnerLogin(owner.getLogin());
        localRepo.setOwnerAvatarUrl(owner.getAvatarUrl());
        return localRepo;
    }

    public static Repository generateFromLocalRepo(LocalRepo localRepo){
        Repository repo = new Repository();
        repo.setId((int) localRepo.getId());
        repo.setName(localRepo.getName());
        repo.setDescription(localRepo.getDescription());
        repo.setLanguage(localRepo.getLanguage());
        repo.setStargazersCount(localRepo.getStargazersCount());
        repo.setWatchersCount(localRepo.getWatchersCount());
        repo.setForksCount(localRepo.getForksCount());
        repo.setFork(localRepo.getFork());
        User user = new User();
        user.setLogin(localRepo.getOwnerLogin());
        user.setAvatarUrl(localRepo.getOwnerAvatarUrl());
        repo.setOwner(user);
        return repo;
    }

    public static Repository generateFromTrace(TraceRepo trace){
        Repository repo = new Repository();
        repo.setId((int) trace.getId());
        repo.setName(trace.getName());
        repo.setDescription(trace.getDescription());
        repo.setLanguage(trace.getLanguage());
        repo.setStargazersCount(trace.getStargazersCount());
        repo.setWatchersCount(trace.getWatchersCount());
        repo.setForksCount(trace.getForksCount());
        repo.setFork(trace.getFork());
        User user = new User();
        user.setLogin(trace.getOwnerLogin());
        user.setAvatarUrl(trace.getOwnerAvatarUrl());
        repo.setOwner(user);
        return repo;
    }

    public static Repository generateFromBookmark(BookMarkRepo bookMark){
        Repository repo = new Repository();
        repo.setId((int) bookMark.getId());
        repo.setName(bookMark.getName());
        repo.setDescription(bookMark.getDescription());
        repo.setLanguage(bookMark.getLanguage());
        repo.setStargazersCount(bookMark.getStargazersCount());
        repo.setWatchersCount(bookMark.getWatchersCount());
        repo.setForksCount(bookMark.getForksCount());
        repo.setFork(bookMark.getFork());
        User user = new User();
        user.setLogin(bookMark.getOwnerLogin());
        user.setAvatarUrl(bookMark.getOwnerAvatarUrl());
        repo.setOwner(user);
        return repo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public boolean isRepPrivate() {
        return repPrivate;
    }

    public void setRepPrivate(boolean repPrivate) {
        this.repPrivate = repPrivate;
    }

    public String getHtmlUrl() {
        return htmlUrl;
    }

    public void setHtmlUrl(String htmlUrl) {
        this.htmlUrl = htmlUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getDefaultBranch() {
        return defaultBranch;
    }

    public void setDefaultBranch(String defaultBranch) {
        this.defaultBranch = defaultBranch;
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

    public Date getPushedAt() {
        return pushedAt;
    }

    public void setPushedAt(Date pushedAt) {
        this.pushedAt = pushedAt;
    }

    public String getGitUrl() {
        return gitUrl;
    }

    public void setGitUrl(String gitUrl) {
        this.gitUrl = gitUrl;
    }

    public String getSshUrl() {
        return sshUrl;
    }

    public void setSshUrl(String sshUrl) {
        this.sshUrl = sshUrl;
    }

    public String getCloneUrl() {
        return cloneUrl;
    }

    public void setCloneUrl(String cloneUrl) {
        this.cloneUrl = cloneUrl;
    }

    public String getSvnUrl() {
        return svnUrl;
    }

    public void setSvnUrl(String svnUrl) {
        this.svnUrl = svnUrl;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public int getStargazersCount() {
        return stargazersCount;
    }

    public void setStargazersCount(int stargazersCount) {
        this.stargazersCount = stargazersCount;
    }

    public int getWatchersCount() {
        return watchersCount;
    }

    public void setWatchersCount(int watchersCount) {
        this.watchersCount = watchersCount;
    }

    public int getForksCount() {
        return forksCount;
    }

    public void setForksCount(int forksCount) {
        this.forksCount = forksCount;
    }

    public int getOpenIssuesCount() {
        return openIssuesCount;
    }

    public void setOpenIssuesCount(int openIssuesCount) {
        this.openIssuesCount = openIssuesCount;
    }

    public boolean isFork() {
        return fork;
    }

    public void setFork(boolean fork) {
        this.fork = fork;
    }

    public RepositoryPermissions getPermissions() {
        return permissions;
    }

    public void setPermissions(RepositoryPermissions permissions) {
        this.permissions = permissions;
    }

    public int getSubscribersCount() {
        return subscribersCount;
    }

    public void setSubscribersCount(int subscribersCount) {
        this.subscribersCount = subscribersCount;
    }

    public Repository getParent() {
        return parent;
    }

    public void setParent(Repository parent) {
        this.parent = parent;
    }

    public boolean isHasIssues() {
        return hasIssues;
    }

    public void setHasIssues(boolean hasIssues) {
        this.hasIssues = hasIssues;
    }

    public boolean isHasProjects() {
        return hasProjects;
    }

    public void setHasProjects(boolean hasProjects) {
        this.hasProjects = hasProjects;
    }

    public boolean isHasDownloads() {
        return hasDownloads;
    }

    public void setHasDownloads(boolean hasDownloads) {
        this.hasDownloads = hasDownloads;
    }

    public boolean isHasWiki() {
        return hasWiki;
    }

    public void setHasWiki(boolean hasWiki) {
        this.hasWiki = hasWiki;
    }

    public boolean isHasPages() {
        return hasPages;
    }

    public void setHasPages(boolean hasPages) {
        this.hasPages = hasPages;
    }

    public int getSinceStargazersCount() {
        return sinceStargazersCount;
    }

    public void setSinceStargazersCount(int sinceStargazersCount) {
        this.sinceStargazersCount = sinceStargazersCount;
    }

    public TrendingSince getSince() {
        return since;
    }

    public void setSince(TrendingSince since) {
        this.since = since;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.fullName);
        dest.writeByte(this.repPrivate ? (byte) 1 : (byte) 0);
        dest.writeString(this.htmlUrl);
        dest.writeString(this.description);
        dest.writeString(this.language);
        dest.writeParcelable(this.owner, flags);
        dest.writeString(this.defaultBranch);
        dest.writeLong(this.createdAt != null ? this.createdAt.getTime() : -1);
        dest.writeLong(this.updatedAt != null ? this.updatedAt.getTime() : -1);
        dest.writeLong(this.pushedAt != null ? this.pushedAt.getTime() : -1);
        dest.writeString(this.gitUrl);
        dest.writeString(this.sshUrl);
        dest.writeString(this.cloneUrl);
        dest.writeString(this.svnUrl);
        dest.writeLong(this.size);
        dest.writeInt(this.stargazersCount);
        dest.writeInt(this.watchersCount);
        dest.writeInt(this.forksCount);
        dest.writeInt(this.openIssuesCount);
        dest.writeInt(this.subscribersCount);
        dest.writeByte(this.fork ? (byte) 1 : (byte) 0);
        dest.writeParcelable(this.parent, flags);
        dest.writeParcelable(this.permissions, flags);
        dest.writeByte(this.hasIssues ? (byte) 1 : (byte) 0);
        dest.writeByte(this.hasProjects ? (byte) 1 : (byte) 0);
        dest.writeByte(this.hasDownloads ? (byte) 1 : (byte) 0);
        dest.writeByte(this.hasWiki ? (byte) 1 : (byte) 0);
        dest.writeByte(this.hasPages ? (byte) 1 : (byte) 0);
        dest.writeInt(this.sinceStargazersCount);
        dest.writeInt(this.since == null ? -1 : this.since.ordinal());

    }

    protected Repository(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.fullName = in.readString();
        this.repPrivate = in.readByte() != 0;
        this.htmlUrl = in.readString();
        this.description = in.readString();
        this.language = in.readString();
        this.owner = in.readParcelable(User.class.getClassLoader());
        this.defaultBranch = in.readString();
        long tmpCreatedAt = in.readLong();
        this.createdAt = tmpCreatedAt == -1 ? null : new Date(tmpCreatedAt);
        long tmpUpdatedAt = in.readLong();
        this.updatedAt = tmpUpdatedAt == -1 ? null : new Date(tmpUpdatedAt);
        long tmpPushedAt = in.readLong();
        this.pushedAt = tmpPushedAt == -1 ? null : new Date(tmpPushedAt);
        this.gitUrl = in.readString();
        this.sshUrl = in.readString();
        this.cloneUrl = in.readString();
        this.svnUrl = in.readString();
        this.size = in.readLong();
        this.stargazersCount = in.readInt();
        this.watchersCount = in.readInt();
        this.forksCount = in.readInt();
        this.openIssuesCount = in.readInt();
        this.subscribersCount = in.readInt();
        this.fork = in.readByte() != 0;
        this.parent = in.readParcelable(Repository.class.getClassLoader());
        this.permissions = in.readParcelable(RepositoryPermissions.class.getClassLoader());
        this.hasIssues = in.readByte() != 0;
        this.hasProjects = in.readByte() != 0;
        this.hasDownloads = in.readByte() != 0;
        this.hasWiki = in.readByte() != 0;
        this.hasPages = in.readByte() != 0;
        this.sinceStargazersCount = in.readInt();
        int tmpTrendingSince = in.readInt();
        this.since = tmpTrendingSince == -1 ? null : TrendingSince.values()[tmpTrendingSince];
    }

    public static final Creator<Repository> CREATOR = new Creator<Repository>() {
        @Override
        public Repository createFromParcel(Parcel source) {
            return new Repository(source);
        }

        @Override
        public Repository[] newArray(int size) {
            return new Repository[size];
        }
    };
}
