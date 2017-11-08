

package com.thirtydegreesray.openhub.mvp.model;

/**
 * Created by ThirtyDegreesRay on 2017/8/27 12:02:42
 */

public class FilePath {

    private String name;
    private String fullPath;

    public FilePath(String name, String fullPath) {
        this.name = name;
        this.fullPath = fullPath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullPath() {
        return fullPath;
    }

    public void setFullPath(String fullPath) {
        this.fullPath = fullPath;
    }

}
