package com.thirtydegreesray.openhub.ui.fragment.base;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.view.MenuItem;

/**
 * Created by ThirtyDegreesRay on 2017/11/9 14:18:24
 */

public interface OnDrawerSelectedListener {

    void onDrawerSelected(@NonNull NavigationView navView, @NonNull MenuItem item);

}
