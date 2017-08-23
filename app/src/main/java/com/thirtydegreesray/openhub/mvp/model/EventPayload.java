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

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by ThirtyDegreesRay on 2017/8/23 21:33:09
 */

public class EventPayload {

    @SerializedName("push_id") private String pushId;
    private int size;
    @SerializedName("distinct_size") private int distinctSize;
    private String ref;
    private String head;
    private String before;
    private ArrayList<Commit> commits;

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getDistinctSize() {
        return distinctSize;
    }

    public void setDistinctSize(int distinctSize) {
        this.distinctSize = distinctSize;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getBefore() {
        return before;
    }

    public void setBefore(String before) {
        this.before = before;
    }

    public ArrayList<Commit> getCommits() {
        return commits;
    }

    public void setCommits(ArrayList<Commit> commits) {
        this.commits = commits;
    }
}

//"payload": {
//        "push_id": 1938913526,
//        "size": 1,
//        "distinct_size": 1,
//        "ref": "refs/heads/master",
//        "head": "83d02ab5420c8d573b1c49160a5fe8c532c1fae3",
//        "before": "9e18b40ce4221eb7bca3e24fb300500542d824ca",
//        "commits": [
    //        {
        //        "sha": "83d02ab5420c8d573b1c49160a5fe8c532c1fae3",
        //        "author": {
            //        "email": "550906320@qq.com",
            //        "name": "13372038054"
            //        },
        //        "message": "develop profile page",
        //        "distinct": true,
        //        "url": "https://api.github.com/repos/ThirtyDegreesRay/OpenHub/commits/83d02ab5420c8d573b1c49160a5fe8c532c1fae3"
    //        }
//        ]
//        },