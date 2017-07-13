/*
 *    Copyright 2017 ThirtyDegressRay
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

/**
 * StringUtil
 * Created by ThirtyDegreesRay on 2016/7/14 16:18
 */
public class StringUtil {

    /**
     * 如果字符串等于null、空白字符(“”)、空格(“ ”)则返回true,否则返回false
     *
     * @param str
     *            String 要比较的字符串
     * @return boolean
     */
    public static boolean isBlank(String str) {
        boolean b = true;
        if (null == str) {
            return b;
        }

        str = str.trim(); // 去掉空格
        if (!str.equals("")) { // 如果不等于“”空字符则返回值为false
            b = false;
        }
        return b;
    }

}
