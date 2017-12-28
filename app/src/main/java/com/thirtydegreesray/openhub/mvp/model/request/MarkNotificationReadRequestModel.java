package com.thirtydegreesray.openhub.mvp.model.request;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by ThirtyDegreesRay on 2017/12/28 11:08:16
 */

public class MarkNotificationReadRequestModel {

    @SerializedName("last_read_at") private Date lastReadAt ;

    public static MarkNotificationReadRequestModel newInstance(){
        MarkNotificationReadRequestModel model = new MarkNotificationReadRequestModel();
        model.setLastReadAt(new Date());
        return model;
    }

    public Date getLastReadAt() {
        return lastReadAt;
    }

    public void setLastReadAt(Date lastReadAt) {
        this.lastReadAt = lastReadAt;
    }
}
