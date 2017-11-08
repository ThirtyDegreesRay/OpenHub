

package com.thirtydegreesray.openhub.mvp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ThirtyDegreesRay on 2017/10/17 13:52:44
 */

public class CommitFile implements Parcelable {

    public enum CommitFileStatusType{
        modified, added, renamed, removed
    }

    private String sha;
    @SerializedName("filename") private String fileName;
    private CommitFileStatusType status;
    private int additions;
    private int deletions;
    private int changes;
    @SerializedName("blob_url") private String blobUrl;
    @SerializedName("raw_url") private String rawUrl;
    @SerializedName("contents_url") private String contentsUrl;
    private String patch;

    public String getSha() {
        return sha;
    }

    public void setSha(String sha) {
        this.sha = sha;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public CommitFileStatusType getStatus() {
        return status;
    }

    public void setStatus(CommitFileStatusType status) {
        this.status = status;
    }

    public int getAdditions() {
        return additions;
    }

    public void setAdditions(int additions) {
        this.additions = additions;
    }

    public int getDeletions() {
        return deletions;
    }

    public void setDeletions(int deletions) {
        this.deletions = deletions;
    }

    public int getChanges() {
        return changes;
    }

    public void setChanges(int changes) {
        this.changes = changes;
    }

    public String getBlobUrl() {
        return blobUrl;
    }

    public void setBlobUrl(String blobUrl) {
        this.blobUrl = blobUrl;
    }

    public String getRawUrl() {
        return rawUrl;
    }

    public void setRawUrl(String rawUrl) {
        this.rawUrl = rawUrl;
    }

    public String getContentsUrl() {
        return contentsUrl;
    }

    public void setContentsUrl(String contentsUrl) {
        this.contentsUrl = contentsUrl;
    }

    public String getPatch() {
        return patch;
    }

    public void setPatch(String patch) {
        this.patch = patch;
    }

    public String getShortFileName() {
        return fileName == null || !fileName.contains("/") ?
                fileName : fileName.substring(fileName.lastIndexOf("/") + 1);
    }

    public String getBasePath() {
        return fileName == null || !fileName.contains("/") ?
                fileName : fileName.substring(0, fileName.lastIndexOf("/") + 1);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.sha);
        dest.writeString(this.fileName);
        dest.writeInt(this.status == null ? -1 : this.status.ordinal());
        dest.writeInt(this.additions);
        dest.writeInt(this.deletions);
        dest.writeInt(this.changes);
        dest.writeString(this.blobUrl);
        dest.writeString(this.rawUrl);
        dest.writeString(this.contentsUrl);
        dest.writeString(this.patch);
    }

    public CommitFile() {
    }

    protected CommitFile(Parcel in) {
        this.sha = in.readString();
        this.fileName = in.readString();
        int tmpStatus = in.readInt();
        this.status = tmpStatus == -1 ? null : CommitFileStatusType.values()[tmpStatus];
        this.additions = in.readInt();
        this.deletions = in.readInt();
        this.changes = in.readInt();
        this.blobUrl = in.readString();
        this.rawUrl = in.readString();
        this.contentsUrl = in.readString();
        this.patch = in.readString();
    }

    public static final Parcelable.Creator<CommitFile> CREATOR = new Parcelable.Creator<CommitFile>() {
        @Override
        public CommitFile createFromParcel(Parcel source) {
            return new CommitFile(source);
        }

        @Override
        public CommitFile[] newArray(int size) {
            return new CommitFile[size];
        }
    };
}
