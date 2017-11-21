package com.thirtydegreesray.openhub.mvp.model;

import android.support.annotation.NonNull;

/**
 * Created by ThirtyDegreesRay on 2017/11/14 11:37:23
 */

public class ActivityRedirectionModel {

    public enum Type {
        User, Repo,
        Fork,
        Issues, Issue,
        Commits, CommitCompare, Commit,
        Releases, Release
    }

    private Type type;

    private String actor;

    private String owner;
    private String repoName;

    private Issue issue;

    private String diffBefore;
    private String diffHead;

    private String branch;
    private String commitSha;

    private Release release;

    public static ActivityRedirectionModel generateForUser(@NonNull Event event) {
        return new ActivityRedirectionModel()
                .setType(Type.User)
                .setActor(event.getActor().getLogin());
    }

    public static ActivityRedirectionModel generateForRepo(@NonNull Event event) {
        return generateRepoInfo(event, Type.Repo);
    }

    public static ActivityRedirectionModel generateForFork(@NonNull Event event) {
        return generateRepoInfo(event, Type.Fork).setActor(event.getActor().getLogin());
    }


    public static ActivityRedirectionModel generateRepoInfo(@NonNull Event event, @NonNull Type type) {
        return new ActivityRedirectionModel()
                .setRepoFullName(event.getRepo().getFullName())
                .setType(type);
    }

    public static ActivityRedirectionModel generateForIssues(@NonNull Event event) {
        return generateRepoInfo(event, Type.Issue)
                .setIssue(event.getPayload().getIssue());
    }

    public static ActivityRedirectionModel generateForCommits(@NonNull Event event) {
        return generateRepoInfo(event, Type.Commits).setBranch(event.getPayload().getBranch());
    }

    public static ActivityRedirectionModel generateForCommitCompare(@NonNull Event event) {
        return generateRepoInfo(event, Type.CommitCompare)
                .setDiffBefore(event.getPayload().getBefore())
                .setDiffHead(event.getPayload().getHead());
    }

    public static ActivityRedirectionModel generateForCommit(@NonNull Event event, int index) {
        return generateRepoInfo(event, Type.Commit)
                .setCommitSha(event.getPayload().getCommits().get(index).getSha());
    }

    public static ActivityRedirectionModel generateForRelease(@NonNull Event event) {
        return generateRepoInfo(event, Type.Release)
                .setRelease(event.getPayload().getRelease());
    }

    public String getOwner() {
        return owner;
    }

    public String getRepoName() {
        return repoName;
    }

    public Issue getIssue() {
        return issue;
    }

    public String getDiffBefore() {
        return diffBefore;
    }

    public String getDiffHead() {
        return diffHead;
    }

    public String getCommitSha() {
        return commitSha;
    }

    public Release getRelease() {
        return release;
    }

    public ActivityRedirectionModel setOwner(String owner) {
        this.owner = owner;
        return this;
    }

    public ActivityRedirectionModel setRepoName(String repoName) {
        this.repoName = repoName;
        return this;
    }

    public ActivityRedirectionModel setIssue(Issue issue) {
        this.issue = issue;
        return this;
    }

    public ActivityRedirectionModel setDiffBefore(String diffBefore) {
        this.diffBefore = diffBefore;
        return this;
    }

    public ActivityRedirectionModel setDiffHead(String diffHead) {
        this.diffHead = diffHead;
        return this;
    }

    public ActivityRedirectionModel setCommitSha(String commitSha) {
        this.commitSha = commitSha;
        return this;
    }

    public ActivityRedirectionModel setRelease(Release release) {
        this.release = release;
        return this;
    }

    public Type getType() {
        return type;
    }

    public ActivityRedirectionModel setType(Type type) {
        this.type = type;
        return this;
    }

    public String getActor() {
        return actor;
    }

    public ActivityRedirectionModel setActor(String actor) {
        this.actor = actor;
        return this;
    }

    public ActivityRedirectionModel setRepoFullName(@NonNull String fullName) {
        owner = fullName.split("/")[0];
        repoName = fullName.split("/")[1];
        return this;
    }

    public String getBranch() {
        return branch;
    }

    public ActivityRedirectionModel setBranch(String branch) {
        this.branch = branch;
        return this;
    }
}
