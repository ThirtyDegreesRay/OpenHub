package com.thirtydegreesray.openhub.mvp.model;

/**
 * Created by ThirtyDegreesRay on 2017/9/16 15:55:57
 */

public class DownloadSource {

    private String url;
    private boolean isSourceCode;
    private String name;
    private long size;

    public DownloadSource(String url, boolean isSourceCode, String name) {
        this.url = url;
        this.isSourceCode = isSourceCode;
        this.name = name;
    }

    public DownloadSource(String url, boolean isSourceCode, String name, long size) {
        this.url = url;
        this.isSourceCode = isSourceCode;
        this.name = name;
        this.size = size;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isSourceCode() {
        return isSourceCode;
    }

    public void setSourceCode(boolean sourceCode) {
        isSourceCode = sourceCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }
}
