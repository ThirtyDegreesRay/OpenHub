

package com.thirtydegreesray.openhub.mvp.model;

import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;

/**
 * Created on 2017/8/2.
 *
 * @author ThirtyDegreesRay
 */

public class SettingModel {

    private int iconResId;
    private String title;
    private String desc;
    private boolean switchChecked;

    private boolean switchEnable = false;

    public SettingModel(@DrawableRes @NonNull int iconResId, @NonNull String title) {
        this.iconResId = iconResId;
        this.title = title;
    }

    public SettingModel(@DrawableRes @NonNull int iconResId, @NonNull String title, String desc) {
        this(iconResId, title);
        this.desc = desc;
    }

    public int getIconResId() {
        return iconResId;
    }

    public SettingModel setIconResId(@DrawableRes @NonNull int iconResId) {
        this.iconResId = iconResId;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public SettingModel setTitle(@NonNull String title) {
        this.title = title;
        return this;
    }

    public String getDesc() {
        return desc;
    }

    public SettingModel setDesc(String desc) {
        this.desc = desc;
        return this;
    }

    public boolean isSwitchChecked() {
        return switchChecked;
    }

    public SettingModel setSwitchChecked(boolean switchChecked) {
        this.switchChecked = switchChecked;
        return this;
    }

    public void toggleSwitch(){
        this.switchChecked = !switchChecked;
    }

    public boolean isSwitchEnable() {
        return switchEnable;
    }

    public SettingModel setSwitchEnable(boolean switchEnable) {
        this.switchEnable = switchEnable;
        return this;
    }
}
