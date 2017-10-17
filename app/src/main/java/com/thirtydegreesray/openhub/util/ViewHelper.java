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

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.support.annotation.ColorInt;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.thirtydegreesray.openhub.R;

/**
 * Created on 2017/8/9.
 *
 * @author ThirtyDegreesRay
 */

public class ViewHelper {

    @ColorInt
    public static int getPrimaryDarkColor(@NonNull Context context) {
        return getColorAttr(context, R.attr.colorPrimaryDark);
    }

    @ColorInt
    public static int getPrimaryColor(@NonNull Context context) {
        return getColorAttr(context, R.attr.colorPrimary);
    }

    @ColorInt
    public static int getPrimaryTextColor(@NonNull Context context) {
        return getColorAttr(context, android.R.attr.textColorPrimary);
    }

    @ColorInt
    public static int getSecondaryTextColor(@NonNull Context context) {
        return getColorAttr(context, android.R.attr.textColorSecondary);
    }

    @ColorInt
    public static int getTertiaryTextColor(@NonNull Context context) {
        return getColorAttr(context, android.R.attr.textColorTertiary);
    }

    @ColorInt
    public static int getAccentColor(@NonNull Context context) {
        return getColorAttr(context, R.attr.colorAccent);
    }

    @ColorInt
    public static int getIconColor(@NonNull Context context) {
        return getColorAttr(context, R.attr.icon_color);
    }

    @ColorInt
    public static int getWindowBackground(@NonNull Context context) {
        return getColorAttr(context, android.R.attr.windowBackground);
    }

    @ColorInt
    public static int getCardBackground(@NonNull Context context) {
        return getColorAttr(context, R.attr.card_background);
    }

    @ColorInt
    public static int getSubTitleColor(@NonNull Context context) {
        return getColorAttr(context, R.attr.subtitle_color);
    }

    @ColorInt
    public static int getSelectedColor(@NonNull Context context) {
        return getColorAttr(context, R.attr.selected_color);
    }

    @ColorInt
    private static int getColorAttr(@NonNull Context context, int attr) {
        Resources.Theme theme = context.getTheme();
        TypedArray typedArray = theme.obtainStyledAttributes(new int[]{attr});
        final int color = typedArray.getColor(0, Color.LTGRAY);
        typedArray.recycle();
        return color;
    }

    private static Drawable getDrawableAttr(@NonNull Context context, int attr) {
        Resources.Theme theme = context.getTheme();
        TypedArray typedArray = theme.obtainStyledAttributes(new int[]{attr});
        final Drawable drawable = typedArray.getDrawable(0);
        typedArray.recycle();
        return drawable;
    }

    public static int[] getRefreshLayoutColors(Context context) {
        return new int[]{
                ViewHelper.getAccentColor(context),
                ViewHelper.getPrimaryColor(context),
                ViewHelper.getPrimaryDarkColor(context)
        };
    }

    public static int toPx(@NonNull Context context, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, dp, context.getResources().getDisplayMetrics());
    }

    @NonNull
    private static StateListDrawable getStateListDrawable(int normalColor, int pressedColor) {
        StateListDrawable states = new StateListDrawable();
        states.addState(new int[]{android.R.attr.state_pressed}, new ColorDrawable(pressedColor));
        states.addState(new int[]{android.R.attr.state_focused}, new ColorDrawable(pressedColor));
        states.addState(new int[]{android.R.attr.state_activated}, new ColorDrawable(pressedColor));
        states.addState(new int[]{android.R.attr.state_selected}, new ColorDrawable(pressedColor));
        states.addState(new int[]{}, new ColorDrawable(normalColor));
        return states;
    }

    public static ColorStateList textSelector(int normalColor, int pressedColor) {
        return new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_pressed},
                        new int[]{android.R.attr.state_focused},
                        new int[]{android.R.attr.state_activated},
                        new int[]{android.R.attr.state_selected},
                        new int[]{}
                },
                new int[]{
                        pressedColor,
                        pressedColor,
                        pressedColor,
                        pressedColor,
                        normalColor
                }
        );
    }

    public static boolean isLandscape(@NonNull Resources resources) {
        return resources.getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    @SuppressWarnings("WeakerAccess")
    public static void showKeyboard(@NonNull View v, @NonNull Context activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(v, 0);
    }

    public static void showKeyboard(@NonNull View v) {
        showKeyboard(v, v.getContext());
    }

    public static void hideKeyboard(@NonNull View view) {
        InputMethodManager inputManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void setLongClickCopy(@NonNull TextView textView) {
        textView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                TextView text = (TextView) v;
                AppHelper.copyToClipboard(text.getContext(), text.getText().toString());
                return true;
            }
        });
    }

    public static void setTextView(@NonNull TextView textView, String text) {
        if (!StringUtils.isBlank(text)) {
            textView.setText(text);
            textView.setVisibility(View.VISIBLE);
        } else {
            textView.setVisibility(View.GONE);
        }
    }

    public static MenuItem getSelectedMenu(@NonNull MenuItem menuItem) {
        if (menuItem.getSubMenu() == null || menuItem.getSubMenu().size() == 0) {
            return null;
        }
        MenuItem selected = null;
        for (int i = 0; i < menuItem.getSubMenu().size(); i++) {
            MenuItem item = menuItem.getSubMenu().getItem(i);
            if (item.isChecked()) {
                selected = item;
                break;
            }
        }
        return selected;
    }

    public static void selectMenuItem(@NonNull Menu menu, @IdRes int itemId, boolean findSub) {
        boolean find = false;
        for (int i = 0; i < menu.size(); i++) {
            if (!findSub) {
                menu.getItem(i).setChecked(menu.getItem(i).getItemId() == itemId);
            } else {
                if (menu.getItem(i).getItemId() == itemId) {
                    find = true;
                }
            }
        }

        if (!findSub) {
            return;
        } else if (find) {
            selectMenuItem(menu, itemId, false);
        } else {
            for (int i = 0; i < menu.size(); i++) {
                SubMenu subMenu = menu.getItem(i).getSubMenu();
                if(subMenu != null)
                    selectMenuItem(subMenu, itemId, true);
            }
        }
    }

}
