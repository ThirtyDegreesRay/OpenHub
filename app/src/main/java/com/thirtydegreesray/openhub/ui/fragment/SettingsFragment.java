

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
import com.thirtydegreesray.openhub.util.PrefUtils;

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
        findPreference(PrefUtils.THEME).setOnPreferenceClickListener(this);
        findPreference(PrefUtils.LANGUAGE).setOnPreferenceClickListener(this);
        findPreference(PrefUtils.LOGOUT).setOnPreferenceClickListener(this);
        findPreference(PrefUtils.CACHE_FIRST_ENABLE).setOnPreferenceChangeListener(this);
        ((ColorChooserPreference)findPreference(PrefUtils.ACCENT_COLOR))
                .setColorChooserCallback(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        switch (preference.getKey()){
            case PrefUtils.CACHE_FIRST_ENABLE:

                return true;
        }
        return true;
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        switch (preference.getKey()){
            case PrefUtils.THEME:
                showThemeChooser();
                return true;
            case PrefUtils.LANGUAGE:
                showLanguageList();
                return true;
            case PrefUtils.LOGOUT:
                logout();
                return true;
        }
        return false;
    }

    private void showThemeChooser(){
        final List<String> valueList
                = Arrays.asList(getResources().getStringArray(R.array.theme_array));
        int theme = PrefUtils.getTheme();
        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setCancelable(true)
                .setTitle(R.string.choose_theme)
                .setSingleChoiceItems(R.array.theme_array, theme, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        PrefUtils.set(PrefUtils.THEME, which);
                        recreateMain();
                    }
                })
                .show();
    }

    private void showLanguageList(){
        final List<String> valueList
                = Arrays.asList(getResources().getStringArray(R.array.language_id_array));
        String language = PrefUtils.getLanguage();
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
                        PrefUtils.set(PrefUtils.LANGUAGE, valueList.get(which));
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
