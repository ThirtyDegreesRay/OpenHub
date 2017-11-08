

package com.thirtydegreesray.openhub.common;

import android.content.Context;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.module.AppGlideModule;
import com.thirtydegreesray.openhub.AppConfig;

/**
 * Created by ThirtyDegreesRay on 2017/8/26 22:47:20
 */
@GlideModule
public class MyAppGlideModel extends AppGlideModule {

    @Override
    public boolean isManifestParsingEnabled() {
        return super.isManifestParsingEnabled();
    }

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        super.applyOptions(context, builder);
        builder.setDiskCache(new InternalCacheDiskCacheFactory(context, AppConfig.IMAGE_MAX_CACHE_SIZE));
    }
}
