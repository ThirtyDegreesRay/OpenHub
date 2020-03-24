package com.thirtydegreesray.openhub.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import android.view.MenuItem;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.thirtydegreesray.openhub.common.GlideApp;

/**
 * Created by ThirtyDegreesRay on 2018/2/5 21:49:24
 */

public class MenuAvatarLoader {

    private Context context;
    private MenuItem menuItem;
    private int placeholderIcon;
    private String url;

    public MenuAvatarLoader(@NonNull Context context, MenuItem menuItem, int placeholderIcon, String url) {
        this.context = context;
        this.menuItem = menuItem;
        this.placeholderIcon = placeholderIcon;
        this.url = url;
    }

    public void load(){
        menuItem.setIcon(placeholderIcon);
        GlideApp.with(context)
                .asDrawable()
                .centerCrop()
                .load(url)
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
//                        menuItem.setIcon(resource);
                        destroy();
                    }
                });

    }

    private void destroy(){
        context = null;
        menuItem = null;
    }

}
