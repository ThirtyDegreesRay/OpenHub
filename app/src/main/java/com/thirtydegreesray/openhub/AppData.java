

package com.thirtydegreesray.openhub;

import android.support.annotation.Nullable;

import com.thirtydegreesray.dataautoaccess.annotation.AutoAccess;
import com.thirtydegreesray.openhub.dao.AuthUser;
import com.thirtydegreesray.openhub.mvp.model.User;

/**
 * Created on 2017/7/14.
 *
 * @author ThirtyDegreesRay
 */

public enum  AppData {
    INSTANCE;

    @AutoAccess(dataName = "appData_loggedUser") User loggedUser;
    @AutoAccess(dataName = "appData_authUser") AuthUser authUser;

    public User getLoggedUser() {
        return loggedUser;
    }

    public void setLoggedUser(User loggedUser) {
        this.loggedUser = loggedUser;
    }

    public AuthUser getAuthUser() {
        return authUser;
    }

    public void setAuthUser(AuthUser authUser) {
        this.authUser = authUser;
    }

    @Nullable public String getAccessToken() {
        return authUser == null ? null : authUser.getAccessToken();
    }

}
