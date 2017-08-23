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

import java.util.Date;

/**
 * Created by ThirtyDegreesRay on 2017/8/23 21:11:20
 */

public class Event {

    private String id;
    private String type;
    private User actor;
    private Repository repo;
    private User org;
    private EventPayload payload;
    @SerializedName("public") private boolean isPublic;
    @SerializedName("created_at") private Date createdAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public User getActor() {
        return actor;
    }

    public void setActor(User actor) {
        this.actor = actor;
    }

    public Repository getRepo() {
        return repo;
    }

    public void setRepo(Repository repo) {
        this.repo = repo;
    }

    public User getOrg() {
        return org;
    }

    public void setOrg(User org) {
        this.org = org;
    }

    public EventPayload getPayload() {
        return payload;
    }

    public void setPayload(EventPayload payload) {
        this.payload = payload;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}

//{
//        "id": "6501666755",
//        "type": "PushEvent",
//        "actor": {
//        "id": 9625508,
//        "login": "ThirtyDegreesRay",
//        "display_login": "ThirtyDegreesRay",
//        "gravatar_id": "",
//        "url": "https://api.github.com/users/ThirtyDegreesRay",
//        "avatar_url": "https://avatars.githubusercontent.com/u/9625508?"
//        },
//        "repo": {
//        "id": 96896313,
//        "name": "ThirtyDegreesRay/OpenHub",
//        "url": "https://api.github.com/repos/ThirtyDegreesRay/OpenHub"
//        },
//        "payload": {
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
//        "public": true,
//        "created_at": "2017-08-23T10:32:25Z"
//        },