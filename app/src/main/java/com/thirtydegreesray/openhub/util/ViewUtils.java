

package com.thirtydegreesray.openhub.util;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.support.annotation.ColorInt;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.SpannableStringBuilder;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SubMenu;
import android.view.View;
import android.widget.TextView;

import com.thirtydegreesray.openhub.R;
import com.thirtydegreesray.openhub.mvp.model.Label;
import com.thirtydegreesray.openhub.ui.widget.IssueLabelSpan;

import java.util.ArrayList;

/**
 * Created on 2017/8/1.
 * Copied from Copyright (C) 2017 Kosh.
 * Modified by Copyright (C) 2017 ThirtyDegreesRay.
 */

public class ViewUtils {

    public static void virtualClick(final View view){
        virtualClick(view, 300);
    }

    public static void virtualClick(final View view, int pressTime){
        long downTime = System.currentTimeMillis();
        int width = view.getWidth();
        int height = view.getHeight();
        float x = view.getX() + width / 2;
        float y = view.getY() + height / 2;

        MotionEvent downEvent = MotionEvent.obtain(downTime, downTime, MotionEvent.ACTION_DOWN, x, y, 0);
        long upTime = downTime + pressTime;
        final MotionEvent upEvent = MotionEvent.obtain(upTime, upTime, MotionEvent.ACTION_UP, x, y, 0);

        view.onTouchEvent(downEvent);
        view.onTouchEvent(upEvent);

        downEvent.recycle();
        upEvent.recycle();
    }

    public static void setLongClickCopy(@NonNull TextView textView) {
        textView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                TextView text = (TextView) v;
                AppUtils.copyToClipboard(text.getContext(), text.getText().toString());
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

    public static int[] getRefreshLayoutColors(Context context) {
        return new int[]{
                getAccentColor(context),
                getPrimaryColor(context),
                getPrimaryDarkColor(context)
        };
    }

    @ColorInt
    public static int getPrimaryColor(@NonNull Context context) {
        return getColorAttr(context, R.attr.colorPrimary);
    }

    @ColorInt
    public static int getPrimaryDarkColor(@NonNull Context context) {
        return getColorAttr(context, R.attr.colorPrimaryDark);
    }

    @ColorInt
    public static int getAccentColor(@NonNull Context context) {
        return getColorAttr(context, R.attr.colorAccent);
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
    public static int getWindowBackground(@NonNull Context context) {
        return getColorAttr(context, android.R.attr.windowBackground);
    }

    @ColorInt
    public static int getCardBackground(@NonNull Context context) {
        return getColorAttr(context, R.attr.card_background);
    }

    @ColorInt
    public static int getIconColor(@NonNull Context context) {
        return getColorAttr(context, R.attr.icon_color);
    }

    @ColorInt
    public static int getTitleColor(@NonNull Context context) {
        return getColorAttr(context, R.attr.title_color);
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


    private static Bitmap getBitmapFromResource(VectorDrawable vectorDrawable) {
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawable.draw(canvas);
        return bitmap;
    }

    /**
     * Get bitmap from resource
     */
    public static Bitmap getBitmapFromResource(Context context, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        if (drawable instanceof BitmapDrawable) {
            return BitmapFactory.decodeResource(context.getResources(), drawableId);
        } else if (drawable instanceof VectorDrawable) {
            return getBitmapFromResource((VectorDrawable) drawable);
        } else {
            throw new IllegalArgumentException("unsupported drawable type");
        }
    }

    public static String getRGBColor(int colorValue, boolean withAlpha){
        int r = ((colorValue >> 16) & 0xff);
        int g = ((colorValue >>  8) & 0xff);
        int b = ((colorValue      ) & 0xff);
        int a = ((colorValue >> 24) & 0xff);
        String red = Integer.toHexString(r);
        String green = Integer.toHexString(g);
        String blue = Integer.toHexString(b);
        String alpha = Integer.toHexString(a);
        red = fixColor(red);
        green = fixColor(green);
        blue = fixColor(blue);
        alpha = fixColor(alpha);
        return withAlpha ? alpha + red + green + blue : red + green + blue;
    }

    private static String fixColor(@NonNull String colorStr){
        return colorStr.length() == 1 ? "0" + colorStr : colorStr;
    }

    public static boolean isLightColor(int color) {
        double darkness = 1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255;
        if (darkness < 0.5) {
            return true; // It's a light color
        } else {
            return false; // It's a dark color
        }
    }

    public static int getLabelTextColor(@NonNull Context context, int bgColorValue){
        if(ViewUtils.isLightColor(bgColorValue)){
            return context.getResources().getColor(R.color.light_text_color_primary);
        } else {
            return context.getResources().getColor(R.color.material_light_white);
        }
    }

    @NonNull
    public static SpannableStringBuilder getLabelsSpan(@NonNull Context context,
                                                       @Nullable ArrayList<Label> labels){
        SpannableStringBuilder labelsText  = new SpannableStringBuilder("");
        if(labels == null){
            return labelsText;
        }
        int start;
        for(int i = 0; i < labels.size(); i++){
            Label label = labels.get(i);
            start = labelsText.length();
            labelsText.append(label.getName());
            labelsText.setSpan(new IssueLabelSpan(context, label), start, start + label.getName().length(), 0);
        }
        return labelsText;
    }

}
