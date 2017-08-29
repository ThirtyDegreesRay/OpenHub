/*
 *    Copyright 2017 ThirtyDegreesRay
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

import com.google.gson.annotations.SerializedName;

/**
 * Created by ThirtyDegreesRay on 2017/8/15 17:56:03
 */

public class Branch {

    private String name;
    @SerializedName("zipball_url") private String zipballUrl;
    @SerializedName("tarball_url") private String tarballUrl;

    private boolean isBranch = true;

    public Branch() {
    }

    public Branch(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getZipballUrl() {
        return zipballUrl;
    }

    public void setZipballUrl(String zipballUrl) {
        this.zipballUrl = zipballUrl;
    }

    public String getTarballUrl() {
        return tarballUrl;
    }

    public void setTarballUrl(String tarballUrl) {
        this.tarballUrl = tarballUrl;
    }

    public boolean isBranch() {
        return isBranch;
    }

    public void setBranch(boolean branch) {
        isBranch = branch;
    }
}

//{
//        "name": "2.1-nightly-26",
//        "zipball_url": "https://api.github.com/repos/pxb1988/dex2jar/zipball/2.1-nightly-26",
//        "tarball_url": "https://api.github.com/repos/pxb1988/dex2jar/tarball/2.1-nightly-26",
//        "commit": {
//        "sha": "d69d6c17c67e0e34862cb1f688905017d106c5b7",
//        "url": "https://api.github.com/repos/pxb1988/dex2jar/commits/d69d6c17c67e0e34862cb1f688905017d106c5b7"
//        }
//        }