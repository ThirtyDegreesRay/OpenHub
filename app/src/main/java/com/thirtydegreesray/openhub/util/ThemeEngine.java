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

package com.thirtydegreesray.openhub.util;

import android.app.Activity;
import android.support.annotation.StyleRes;

import com.thirtydegreesray.openhub.R;
import com.thirtydegreesray.openhub.ui.activity.SplashActivity;
import com.thirtydegreesray.openhub.ui.activity.base.BaseActivity;

/**
 * Created on 2017/8/8.
 *
 * @author ThirtyDegreesRay
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
