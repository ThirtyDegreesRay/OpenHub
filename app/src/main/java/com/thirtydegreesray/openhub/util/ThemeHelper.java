

package com.thirtydegreesray.openhub.util;

import android.app.Activity;
import android.support.annotation.StyleRes;

import com.thirtydegreesray.openhub.R;
import com.thirtydegreesray.openhub.ui.activity.SplashActivity;
import com.thirtydegreesray.openhub.ui.activity.base.BaseActivity;

/**
 * Created on 2017/10/30 13:26:25
 * Copied from Copyright (C) 2017 Kosh.
 * Modified by Copyright (C) 2017 ThirtyDegreesRay.
 */

public class ThemeHelper {

    public static void apply(BaseActivity activity){
        if(ignorePage(activity)) return ;
        int theme = PrefUtils.getTheme();
        int accentColor = PrefUtils.getAccentColor();
        activity.setTheme(getTheme(theme, accentColor));
    }

    public static void applyForAboutActivity(Activity activity){
        int theme = PrefUtils.getTheme();
        activity.setTheme(getAboutTheme(theme));
    }

    @StyleRes
    public static int getAboutTheme(int theme){
        return theme == PrefUtils.LIGHT ? R.style.ThemeLight_AboutActivity : R.style.ThemeDark_AboutActivity;
    }

    @StyleRes
    public static int getTheme(int theme, int accentColor){
        switch (theme){
            case PrefUtils.LIGHT:
                switch (accentColor){
                    case PrefUtils.LIGHT_BLUE:
                        return R.style.ThemeLight_LightBlue;
                    case PrefUtils.BLUE:
                        return R.style.ThemeLight_Blue;
                    case PrefUtils.INDIGO:
                        return R.style.ThemeLight_Indigo;
                    case PrefUtils.ORANGE:
                        return R.style.ThemeLight_Orange;

                    case PrefUtils.YELLOW:
                        return R.style.ThemeLight_Yellow;
                    case PrefUtils.AMBER:
                        return R.style.ThemeLight_Amber;
                    case PrefUtils.GREY:
                        return R.style.ThemeLight_Grey;
                    case PrefUtils.BROWN:
                        return R.style.ThemeLight_Brown;

                    case PrefUtils.CYAN:
                        return R.style.ThemeLight_Cyan;
                    case PrefUtils.TEAL:
                        return R.style.ThemeLight_Teal;
                    case PrefUtils.LIME:
                        return R.style.ThemeLight_Lime;
                    case PrefUtils.GREEN:
                        return R.style.ThemeLight_Green;

                    case PrefUtils.PINK:
                        return R.style.ThemeLight_Pink;
                    case PrefUtils.RED:
                        return R.style.ThemeLight_Red;
                    case PrefUtils.PURPLE:
                        return R.style.ThemeLight_Purple;
                    case PrefUtils.DEEP_PURPLE:
                        return R.style.ThemeLight_DeepPurple;
                }
            case PrefUtils.DARK:
                switch (accentColor){
                    case PrefUtils.LIGHT_BLUE:
                        return R.style.ThemeDark_LightBlue;
                    case PrefUtils.BLUE:
                        return R.style.ThemeDark_Blue;
                    case PrefUtils.INDIGO:
                        return R.style.ThemeDark_Indigo;
                    case PrefUtils.ORANGE:
                        return R.style.ThemeDark_Orange;

                    case PrefUtils.YELLOW:
                        return R.style.ThemeDark_Yellow;
                    case PrefUtils.AMBER:
                        return R.style.ThemeDark_Amber;
                    case PrefUtils.GREY:
                        return R.style.ThemeDark_Grey;
                    case PrefUtils.BROWN:
                        return R.style.ThemeDark_Brown;

                    case PrefUtils.CYAN:
                        return R.style.ThemeDark_Cyan;
                    case PrefUtils.TEAL:
                        return R.style.ThemeDark_Teal;
                    case PrefUtils.LIME:
                        return R.style.ThemeDark_Lime;
                    case PrefUtils.GREEN:
                        return R.style.ThemeDark_Green;

                    case PrefUtils.PINK:
                        return R.style.ThemeDark_Pink;
                    case PrefUtils.RED:
                        return R.style.ThemeDark_Red;
                    case PrefUtils.PURPLE:
                        return R.style.ThemeDark_Purple;
                    case PrefUtils.DEEP_PURPLE:
                        return R.style.ThemeDark_DeepPurple;
                }
        }
        return R.style.ThemeLight_Indigo;
    }

    private static boolean ignorePage(BaseActivity activity){
        return activity instanceof SplashActivity;
    }

}
