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

package com.thirtydegreesray.openhub.mvp.model;

import android.net.Uri;
import android.support.annotation.Nullable;

import com.thirtydegreesray.openhub.util.StringUtils;

import java.util.List;

/**
 * Created by Kosh on 11 Feb 2017, 11:03 PM
 */

public class NameParser {

    public String name;
    public String username;
    public boolean isEnterprise;

    public NameParser(@Nullable String url) {
        if (!StringUtils.isBlank(url)) {
//            boolean isEnterprise = LinkParserHelper.isEnterprise(url);
//            if (isEnterprise) {
//                url = url.replace("api/v3/", "");
//            }
            Uri uri = Uri.parse(url);
            List<String> segments = uri.getPathSegments();
            if (segments == null || segments.size() < 2) {
                return;
            }
            boolean isFirstPathIsRepo = (segments.get(0).equalsIgnoreCase("repos") || segments.get(0).equalsIgnoreCase("repo"));
            this.username = isFirstPathIsRepo ? segments.get(1) : segments.get(0);
            this.name = isFirstPathIsRepo ? segments.get(2) : segments.get(1);
            this.isEnterprise = isEnterprise;
        }
    }

    @Override public String toString() {
        return "NameParser{" +
                "name='" + name + '\'' +
                ", username='" + username + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public boolean isEnterprise() {
        return isEnterprise;
    }
}
