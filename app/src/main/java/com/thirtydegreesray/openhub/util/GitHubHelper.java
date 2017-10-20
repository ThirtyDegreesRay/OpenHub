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

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.webkit.MimeTypeMap;

import java.util.regex.Pattern;

/**
 * Created by ThirtyDegreesRay on 2017/8/19 17:24:29
 */

public class GitHubHelper {

    private static final String[] IMAGE_EXTENSIONS = {".png", ".jpg", ".jpeg", ".gif", ".svg"};

    private static final String[] MARKDOWN_EXTENSIONS = {
            ".md", ".mkdn", ".mdwn", ".mdown", ".markdown", ".mkd", ".mkdown", ".ron", ".rst", "adoc"
    };

        private static final String[] ARCHIVE_EXTENSIONS = {
            ".zip", ".zipx", ".7z", "s7z", "zz", ".rar", ".tar.gz", ".tgz", ".tar.Z", ".tar.bz2", ".tbz2", ".tar.lzma",
            ".tlz", ".apk", ".jar", ".dmg", "ipa", "war", "cab", "dar"
    };

    private static final String GITHUB_BASE_URL_PATTERN_STR = "(https://)?(http://)?(www.)?github.com";

    public static final Pattern REPO_FULL_NAME_PATTERN =
            Pattern.compile("([a-z]|[A-Z]|\\d|-)*/([a-z]|[A-Z]|\\d|-|\\.|_)*");
    private static final Pattern USER_PATTERN = Pattern.compile(GITHUB_BASE_URL_PATTERN_STR
            + "/([a-z]|[A-Z]|\\d|-)*(/)?");
    private static final Pattern REPO_PATTERN = Pattern.compile(GITHUB_BASE_URL_PATTERN_STR
            + "/([a-z]|[A-Z]|\\d|-)*/([a-z]|[A-Z]|\\d|-|\\.|_)*(/)?");
    private static final Pattern ISSUE_PATTERN = Pattern.compile(GITHUB_BASE_URL_PATTERN_STR
            + "/([a-z]|[A-Z]|\\d|-)*/([a-z]|[A-Z]|\\d|-|\\.|_)*/issues/(\\d)*(/)?");
    private static final Pattern GITHUB_URL_PATTERN = Pattern.compile(GITHUB_BASE_URL_PATTERN_STR
            + "(.)*");

    public static boolean isImage(@Nullable String name) {
        if (StringUtils.isBlank(name)) return false;
        name = name.toLowerCase();
        for (String value : IMAGE_EXTENSIONS) {
            String extension = MimeTypeMap.getFileExtensionFromUrl(name);
            if ((extension != null && value.replace(".", "").equals(extension))
                    || name.endsWith(value))
                return true;
        }
        return false;
    }

    public static boolean isMarkdown(@Nullable String name) {
        if (StringUtils.isBlank(name)) return false;
        name = name.toLowerCase();
        for (String value : MARKDOWN_EXTENSIONS) {
            String extension = MimeTypeMap.getFileExtensionFromUrl(name);
            if ((extension != null && value.replace(".", "").equals(extension)) ||
                    name.equalsIgnoreCase("README") || name.endsWith(value))
                return true;
        }
        return false;
    }

    public static boolean isArchive(@Nullable String name) {
        if (StringUtils.isBlank(name)) return false;
        name = name.toLowerCase();
        for (String value : ARCHIVE_EXTENSIONS) {
            String extension = MimeTypeMap.getFileExtensionFromUrl(name);
            if ((extension != null && value.replace(".", "").equals(extension))
                    || name.endsWith(value))
                return true;
        }

        return false;
    }

    public static String getExtension(@Nullable String name) {
        if (StringUtils.isBlank(name)) return null;
        return MimeTypeMap.getFileExtensionFromUrl(name);
    }

    public static boolean isUserUrl(@NonNull String url){
        return USER_PATTERN.matcher(url).matches();
    }

    public static boolean isRepoUrl(@NonNull String url){
        return REPO_PATTERN.matcher(url).matches();
    }

    public static boolean isIssueUrl(@NonNull String url){
        return ISSUE_PATTERN.matcher(url).matches();
    }

    public static boolean isGitHubUrl(@NonNull String url){
        return GITHUB_URL_PATTERN.matcher(url).matches();
    }

    @Nullable
    public static String getUserFromUrl(@NonNull String url){
        if(!isUserUrl(url)) return null;
        if(url.endsWith("/")) url = url.substring(0, url.length() - 1);
        return url.substring(url.lastIndexOf("/") + 1);
    }

    @Nullable
    public static String getRepoFullNameFromUrl(@NonNull String url){
        if(!isRepoUrl(url)) return null;
        if(url.endsWith("/")) url = url.substring(0, url.length() - 1);
        return url.substring(url.indexOf("com/") + 4);
    }

}
