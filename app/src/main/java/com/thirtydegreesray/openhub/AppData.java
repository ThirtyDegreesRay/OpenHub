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

package com.thirtydegreesray.openhub;

import android.support.annotation.NonNull;

import com.thirtydegreesray.openhub.db.AuthUser;
import com.thirtydegreesray.openhub.mvp.model.User;

/**
 * Created on 2017/7/14.
 *
 * @author ThirtyDegreesRay
 */

public class AppData {

    private AppData(){}

    private static class InstanceHolder{
        @NonNull static AppData instance = new AppData();
    }

    @NonNull
    public static AppData getInstance(){
        return InstanceHolder.instance;
    }

    private User loginedUser;
    private AuthUser authUser;

    public User getLoginedUser() {
        return loginedUser;
    }

    public void setLoginedUser(User loginedUser) {
        this.loginedUser = loginedUser;
    }

    public AuthUser getAuthUser() {
        return authUser;
    }

    public void setAuthUser(AuthUser authUser) {
        this.authUser = authUser;
    }
}
