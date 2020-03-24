package com.thirtydegreesray.openhub.ui.fragment.base;

import androidx.annotation.NonNull;
import com.google.android.material.navigation.NavigationView;
import android.view.MenuItem;

/**
 * Created by ThirtyDegreesRay on 2017/11/9 14:18:24
 */

public interface OnDrawerSelectedListener {

    void onDrawerSelected(@NonNull NavigationView navView, @NonNull MenuItem item);

}
