/*
 *    Copyright 2017 ThirtyDegressRay
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.thirtydegreesray.openhub.mvp.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2017/6/26.
 *
 * @author ThirtyDegreesRay
 */

public class FileInfo {

    private String path;

    private String name;

    private List<FileInfo> childrenList;

    private String parentPath;

    private boolean isFile;

    public FileInfo(){

    }

    public FileInfo initFromFile(File file, boolean getChildren){
        setName(file.getName())
                .setPath(file.getPath())
                .setFile(file.isFile())
                .setParentPath(file.getParent());

        if(getChildren && file.isDirectory()){
            File[] childrenFiles = file.listFiles();
            childrenList = new ArrayList<>();
            for(int i = 0; i < childrenFiles.length; i++){
                childrenList.add(new FileInfo().initFromFile(childrenFiles[i], false));
            }
        }

        return this;
    }

    public String getPath() {
        return path;
    }

    public FileInfo setPath(String path) {
        this.path = path;
        return this;
    }

    public String getName() {
        return name;
    }

    public FileInfo setName(String name) {
        this.name = name;
        return this;
    }

    public List<FileInfo> getChildrenList() {
        return childrenList;
    }

    public FileInfo setChildrenList(List<FileInfo> childrenList) {
        this.childrenList = childrenList;
        return this;
    }

    public String getParentPath() {
        return parentPath;
    }

    public FileInfo setParentPath(String parentPath) {
        this.parentPath = parentPath;
        return this;
    }

    public boolean isFile() {
        return isFile;
    }

    public boolean isDirectory() {
        return !isFile;
    }

    public FileInfo setFile(boolean file) {
        isFile = file;
        return this;
    }
}
