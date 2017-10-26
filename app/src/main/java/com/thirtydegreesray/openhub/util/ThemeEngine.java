/*
 *   Copyright (C) 2017 Kosh.
 *   Licensed under the GPL-3.0 license.
 *   (See the LICENSE(https://github.com/k0shk0sh/FastHub/blob/master/LICENSE) file for the whole license text.)
 */

package com.thirtydegreesray.openhub.util;

import android.app.Activity;
import android.support.annotation.StyleRes;

import com.thirtydegreesray.openhub.R;
import com.thirtydegreesray.openhub.ui.activity.SplashActivity;
import com.thirtydegreesray.openhub.ui.activity.base.BaseActivity;

/**
 * Created on 2017/8/8.
 */

public class ThemeEngine {

    public static void apply(BaseActivity activity){
        if(ignorePage(activity)){
            return ;
        }
        int theme = PrefHelper.getTheme();
        int accentColor = PrefHelper.getAccentColor();
        activity.setTheme(getTheme(theme, accentColor));
    }

    public static void applyForAboutActivity(Activity activity){
        int theme = PrefHelper.getTheme();
        activity.setTheme(getAboutTheme(theme));
    }

    @StyleRes
    public static int getAboutTheme(int theme){
        if(theme == PrefHelper.LIGHT){
            return R.style.ThemeLight_AboutActivity;
        }else{
            return R.style.ThemeDark_AboutActivity;
        }
    }

    @StyleRes
    public static int getTheme(int theme, int accentColor){
        switch (theme){
            case PrefHelper.LIGHT:
                switch (accentColor){
                    case PrefHelper.LIGHT_BLUE:
                        return R.style.ThemeLight_LightBlue;
                    case PrefHelper.BLUE:
                        return R.style.ThemeLight_Blue;
                    case PrefHelper.INDIGO:
                        return R.style.ThemeLight_Indigo;
                    case PrefHelper.ORANGE:
                        return R.style.ThemeLight_Orange;

                    case PrefHelper.YELLOW:
                        return R.style.ThemeLight_Yellow;
                    case PrefHelper.AMBER:
                        return R.style.ThemeLight_Amber;
                    case PrefHelper.GREY:
                        return R.style.ThemeLight_Grey;
                    case PrefHelper.BROWN:
                        return R.style.ThemeLight_Brown;

                    case PrefHelper.CYAN:
                        return R.style.ThemeLight_Cyan;
                    case PrefHelper.TEAL:
                        return R.style.ThemeLight_Teal;
                    case PrefHelper.LIME:
                        return R.style.ThemeLight_Lime;
                    case PrefHelper.GREEN:
                        return R.style.ThemeLight_Green;

                    case PrefHelper.PINK:
                        return R.style.ThemeLight_Pink;
                    case PrefHelper.RED:
                        return R.style.ThemeLight_Red;
                    case PrefHelper.PURPLE:
                        return R.style.ThemeLight_Purple;
                    case PrefHelper.DEEP_PURPLE:
                        return R.style.ThemeLight_DeepPurple;
                }
            case PrefHelper.DARK:
                switch (accentColor){
                    case PrefHelper.LIGHT_BLUE:
                        return R.style.ThemeDark_LightBlue;
                    case PrefHelper.BLUE:
                        return R.style.ThemeDark_Blue;
                    case PrefHelper.INDIGO:
                        return R.style.ThemeDark_Indigo;
                    case PrefHelper.ORANGE:
                        return R.style.ThemeDark_Orange;

                    case PrefHelper.YELLOW:
                        return R.style.ThemeDark_Yellow;
                    case PrefHelper.AMBER:
                        return R.style.ThemeDark_Amber;
                    case PrefHelper.GREY:
                        return R.style.ThemeDark_Grey;
                    case PrefHelper.BROWN:
                        return R.style.ThemeDark_Brown;

                    case PrefHelper.CYAN:
                        return R.style.ThemeDark_Cyan;
                    case PrefHelper.TEAL:
                        return R.style.ThemeDark_Teal;
                    case PrefHelper.LIME:
                        return R.style.ThemeDark_Lime;
                    case PrefHelper.GREEN:
                        return R.style.ThemeDark_Green;

                    case PrefHelper.PINK:
                        return R.style.ThemeDark_Pink;
                    case PrefHelper.RED:
                        return R.style.ThemeDark_Red;
                    case PrefHelper.PURPLE:
                        return R.style.ThemeDark_Purple;
                    case PrefHelper.DEEP_PURPLE:
                        return R.style.ThemeDark_DeepPurple;
                }
        }
        return R.style.ThemeLight_Indigo;
    }

    private static boolean ignorePage(BaseActivity activity){
        return activity instanceof SplashActivity;
    }

}
