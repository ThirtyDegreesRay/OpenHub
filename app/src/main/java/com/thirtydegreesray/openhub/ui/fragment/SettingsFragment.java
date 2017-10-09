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

package com.thirtydegreesray.openhub.ui.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v7.app.AlertDialog;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.util.Log;

import com.thirtydegreesray.openhub.R;
import com.thirtydegreesray.openhub.ui.activity.base.BaseActivity;
import com.thirtydegreesray.openhub.ui.widget.colorChooser.ColorChooserPreference;
import com.thirtydegreesray.openhub.util.PrefHelper;

import java.util.Arrays;
import java.util.List;

/**
 * Created on 2017/8/3.
 *
 * @author ThirtyDegreesRay
 */

public class SettingsFragment extends PreferenceFragmentCompat
        implements Preference.OnPreferenceChangeListener,
        Preference.OnPreferenceClickListener,
        ColorChooserPreference.ColorChooserCallback{

    @Override
    public void onColorChanged(@ColorInt int oriColor, @ColorInt int selectedColor) {
        recreateMain();
    }

    public interface SettingsCallBack{
        void onLogout();
        void onRecreate();
    }

    private SettingsCallBack callBack;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callBack = (SettingsCallBack) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callBack = null;
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.settings);
        findPreference(PrefHelper.THEME).setOnPreferenceClickListener(this);
        findPreference(PrefHelper.LANGUAGE).setOnPreferenceClickListener(this);
        findPreference(PrefHelper.LOGOUT).setOnPreferenceClickListener(this);
        findPreference(PrefHelper.CACHE_FIRST_ENABLE).setOnPreferenceChangeListener(this);
        ((ColorChooserPreference)findPreference(PrefHelper.ACCENT_COLOR))
                .setColorChooserCallback(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        switch (preference.getKey()){
            case PrefHelper.CACHE_FIRST_ENABLE:

                return true;
        }
        return true;
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        switch (preference.getKey()){
            case PrefHelper.THEME:
                showThemeChooser();
                return true;
            case PrefHelper.LANGUAGE:
                showLanguageList();
                return true;
            case PrefHelper.LOGOUT:
                logout();
                return true;
        }
        return false;
    }

    private void showThemeChooser(){
        final List<String> valueList
                = Arrays.asList(getResources().getStringArray(R.array.theme_array));
        int theme = PrefHelper.getTheme();
        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setCancelable(true)
                .setTitle(R.string.choose_theme)
                .setSingleChoiceItems(R.array.theme_array, theme, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        PrefHelper.set(PrefHelper.THEME, which);
                        recreateMain();
                    }
                })
                .show();
    }

    private void showLanguageList(){
        final List<String> valueList
                = Arrays.asList(getResources().getStringArray(R.array.language_id_array));
        String language = PrefHelper.getLanguage();
        int index = valueList.indexOf(language);

        if(getContext() instanceof BaseActivity){
            Log.d("TAG" , "");
        }

        new AlertDialog.Builder(getContext())
                .setCancelable(true)
                .setTitle(R.string.language)
                .setSingleChoiceItems(R.array.language_array, index, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        PrefHelper.set(PrefHelper.LANGUAGE, valueList.get(which));
                        recreateMain();
                    }
                })
                .show();
    }

    private void recreateMain(){
        callBack.onRecreate();
    }

    private void logout(){
        new AlertDialog.Builder(getContext())
                .setCancelable(true)
                .setTitle(R.string.warning_dialog_tile)
                .setMessage(R.string.logout_warning)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(R.string.logout, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callBack.onLogout();
                        dialog.dismiss();
                    }
                })
                .show();
    }


}
