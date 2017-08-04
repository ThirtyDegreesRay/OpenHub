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

package com.thirtydegreesray.openhub.ui.widget.colorChooser;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceViewHolder;
import android.util.AttributeSet;
import android.view.View;

import com.afollestad.materialdialogs.Theme;
import com.thirtydegreesray.openhub.R;
import com.thirtydegreesray.openhub.ui.activity.base.BaseActivity;
import com.thirtydegreesray.openhub.util.PrefHelper;

/**
 * Created on 2017/8/4.
 *
 * @author ThirtyDegreesRay
 */

public class ColorChooserPreference extends Preference implements ColorChooserDialog.ColorCallback {

    private ColorChooserDialog colorChooserDialog;
    private View colorView;

    public ColorChooserPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public ColorChooserPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public ColorChooserPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ColorChooserPreference(Context context) {
        super(context);
        init();
    }

    private void init(){
        setWidgetLayoutResource(R.layout.preference_widget_color);
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        colorView = holder.findViewById(R.id.color_view);
        colorView.setBackgroundResource(R.drawable.shape_circle);
        colorView.getBackground().setColorFilter(PrefHelper.getAccentColor(), PorterDuff.Mode.SRC_IN);
    }

    @Override
    protected void onClick() {
        super.onClick();
        colorChooserDialog = new ColorChooserDialog
                .Builder(BaseActivity.getCurActivity(), this, R.string.theme_accent_color)
                .titleSub(R.string.choose_theme)
                .customColors(getAccentColors(), null)
                .preselect(PrefHelper.getAccentColor())
                .customButton(0)
                .accentMode(true)
                .show();
    }

    @Override
    public void onColorSelection(@NonNull ColorChooserDialog dialog, @ColorInt int selectedColor) {
        PrefHelper.set(PrefHelper.ACCENT_COLOR, selectedColor);
        colorView.getBackground().setColorFilter(selectedColor, PorterDuff.Mode.SRC_IN);
    }

    @Override
    public void onColorChooserDismissed(@NonNull ColorChooserDialog dialog) {

    }

    @NonNull
    private int[] getAccentColors(){
        int[] colorsResId = getContext().getResources().getIntArray(R.array.accent_color_array);
        return colorsResId;
    }
}
