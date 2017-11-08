

package com.thirtydegreesray.openhub.mvp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by ThirtyDegreesRay on 2017/8/25 14:04:04
 */

public class SearchResult<M> implements Parcelable {

    @SerializedName("total_count") private String totalCount;
    @SerializedName("incomplete_results") private boolean incompleteResults;
    private ArrayList<M> items;

    public String getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(String totalCount) {
        this.totalCount = totalCount;
    }

    public boolean isIncompleteResults() {
        return incompleteResults;
    }

    public void setIncompleteResults(boolean incompleteResults) {
        this.incompleteResults = incompleteResults;
    }

    public ArrayList<M> getItems() {
        return items;
    }

    public void setItems(ArrayList<M> items) {
        this.items = items;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.totalCount);
        dest.writeByte(this.incompleteResults ? (byte) 1 : (byte) 0);
        dest.writeList(this.items);
    }

    public SearchResult() {
    }

    protected SearchResult(Parcel in) {
        this.totalCount = in.readString();
        this.incompleteResults = in.readByte() != 0;
        this.items = new ArrayList<M>();
        in.readList(this.items, SearchResult.class.getClassLoader());
    }

    public static final Parcelable.Creator<SearchResult> CREATOR = new Parcelable.Creator<SearchResult>() {
        @Override
        public SearchResult createFromParcel(Parcel source) {
            return new SearchResult(source);
        }

        @Override
        public SearchResult[] newArray(int size) {
            return new SearchResult[size];
        }
    };
}
