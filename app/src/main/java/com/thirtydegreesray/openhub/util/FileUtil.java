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
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Base64;

import java.io.File;
import java.io.FileInputStream;

/**
 * 文件工具类
 * Created by ThirtyDegreesRay on 2016/8/4 13:30
 */
public class FileUtil {

    private final static String AUDIO_CACHE_DIR_NAME = "audio";

    private final static String SIGN_IMAGE_CACHE_DIR_NAME = "sign_image";

    private final static String HTTP_CACHE_DIR_NAME = "http_response";

    /**
     * 获取缓存文件夹
     *
     * @param context 上下文
     * @param dirName 文件夹名称
     * @return 缓存文件夹
     */
    @Nullable
    public static File getCacheDir(@NonNull Context context, @NonNull String dirName) {
        File rootDir = context.getExternalCacheDir();
        File cacheFile = new File(rootDir, dirName);
        if (!cacheFile.exists()) {
            cacheFile.mkdir();
        }
        return cacheFile;
    }

    /**
     * 获取音频缓存文件夹
     *
     * @param context 上下文
     * @return 音频缓存文件夹
     */
    @Nullable
    public static File getAudioCacheDir(@NonNull Context context) {
        return getCacheDir(context, AUDIO_CACHE_DIR_NAME);
    }

    /**
     * 获取图片缓存文件夹
     *
     * @param context 上下文
     * @return 图片缓存文件夹
     */
    @Nullable
    public static File getSignImageCacheDir(@NonNull Context context) {
        return getCacheDir(context, SIGN_IMAGE_CACHE_DIR_NAME);
    }

    /**
     * 获取网络请求缓存文件夹
     * @param context 上下文
     * @return 网络请求缓存文件夹
     */
    @Nullable
    public static File getHttpImageCacheDir(@NonNull Context context) {
        return getCacheDir(context, HTTP_CACHE_DIR_NAME);
    }

    /**
     * 检查内部存储是否可用
     * @return
     */
    public static boolean isExternalStorageEnable() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    /**
     * 将文件转化为字节数组字符串，并对其进行Base64编码处理
     * @return
     */
    public static String encodeBase64File(@NonNull String path) throws Exception {
        File file = new File(path);
        FileInputStream inputFile = new FileInputStream(file);
        byte[] buffer = new byte[(int)file.length()];
        inputFile.read(buffer);
        inputFile.close();
        return Base64.encodeToString(buffer, Base64.DEFAULT);
    }

}
