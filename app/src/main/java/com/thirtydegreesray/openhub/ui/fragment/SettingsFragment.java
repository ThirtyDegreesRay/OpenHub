

package com.thirtydegreesray.openhub.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v7.app.AlertDialog;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.thirtydegreesray.openhub.R;
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
        ColorChooserPreference.ColorChooserCallback {

    @Override
    public void onColorChanged(@ColorInt int oriColor, @ColorInt int selectedColor) {
        recreateMain();
    }

    public interface SettingsCallBack {
        void onLogout();

        void onRecreate();
    }

    private SettingsCallBack callBack;

    private List<String> idList ;
    private List<String> nameList ;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callBack = (SettingsCallBack) context;
        idList = Arrays.asList(getResources().getStringArray(R.array.start_pages_id));
        nameList = Arrays.asList(getResources().getStringArray(R.array.start_pages_name));
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
//        findPreference(PrefUtils.LOGOUT).setOnPreferenceClickListener(this);
        findPreference(PrefUtils.START_PAGE).setOnPreferenceClickListener(this);
        findPreference(PrefUtils.START_PAGE).setSummary(nameList.get(getStartPageIndex()));
        ((ColorChooserPreference) findPreference(PrefUtils.ACCENT_COLOR))
                .setColorChooserCallback(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        return true;
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        switch (preference.getKey()) {
            case PrefUtils.THEME:
                showThemeChooser();
                return true;
            case PrefUtils.LANGUAGE:
                showLanguageList();
                return true;
            case PrefUtils.LOGOUT:
                logout();
                return true;
            case PrefUtils.START_PAGE:
                showChooseStartPageDialog();
                return true;
        }
        return false;
    }

    private void showThemeChooser() {
        final List<String> valueList
                = Arrays.asList(getResources().getStringArray(R.array.theme_array));
        String theme = PrefUtils.getTheme();
        int selectIndex = valueList.indexOf(theme);
        new AlertDialog.Builder(getContext())
                .setCancelable(true)
                .setTitle(R.string.choose_theme)
                .setSingleChoiceItems(R.array.theme_array, selectIndex, (dialog1, which) -> {
                    dialog1.dismiss();
                    PrefUtils.set(PrefUtils.THEME, valueList.get(which));
                    recreateMain();
                })
                .show();
    }

    private void showLanguageList() {
        final List<String> valueList
                = Arrays.asList(getResources().getStringArray(R.array.language_id_array));
        String language = PrefUtils.getLanguage();
        int index = valueList.indexOf(language);

        new AlertDialog.Builder(getContext())
                .setCancelable(true)
                .setTitle(R.string.language)
                .setSingleChoiceItems(R.array.language_array, index, (dialog, which) -> {
                    dialog.dismiss();
                    PrefUtils.set(PrefUtils.LANGUAGE, valueList.get(which));
                    recreateMain();
                })
                .show();
    }

    private void recreateMain() {
        callBack.onRecreate();
    }

    private void logout() {
        new AlertDialog.Builder(getContext())
                .setCancelable(true)
                .setTitle(R.string.warning_dialog_tile)
                .setMessage(R.string.logout_warning)
                .setNegativeButton(R.string.cancel, (dialog, which) -> dialog.dismiss())
                .setPositiveButton(R.string.logout, (dialog, which) -> {
                    callBack.onLogout();
                    dialog.dismiss();
                })
                .show();
    }

    private void showChooseStartPageDialog(){
        new AlertDialog.Builder(getContext())
                .setCancelable(true)
                .setTitle(R.string.start_page)
                .setSingleChoiceItems(R.array.start_pages_name, getStartPageIndex(), (dialog, which) -> {
                    dialog.dismiss();
                    PrefUtils.set(PrefUtils.START_PAGE, idList.get(which));
                    findPreference(PrefUtils.START_PAGE).setSummary(nameList.get(which));
                })
                .setNegativeButton(R.string.cancel, (dialog, which) -> {

                })
                .show();
    }

    private int getStartPageIndex(){
        String startPage = PrefUtils.getStartPage();
        return idList.indexOf(startPage);
    }

}
