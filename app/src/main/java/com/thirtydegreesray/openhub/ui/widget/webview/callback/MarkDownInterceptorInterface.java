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

package com.thirtydegreesray.openhub.ui.widget.webview.callback;

import android.webkit.JavascriptInterface;

import com.thirtydegreesray.openhub.ui.widget.webview.PrettifyWebView;

/**
 * Created by Kosh on 13 Dec 2016, 3:01 PM
 */

public class MarkDownInterceptorInterface {
    private PrettifyWebView prettifyWebView;
    private boolean toggleNestScrolling;

    public MarkDownInterceptorInterface(PrettifyWebView prettifyWebView) {
        this(prettifyWebView, false);
    }

    public MarkDownInterceptorInterface(PrettifyWebView prettifyWebView, boolean toggleNestScrolling) {
        this.prettifyWebView = prettifyWebView;
        this.toggleNestScrolling = toggleNestScrolling;
    }

    @JavascriptInterface public void startIntercept() {
        if (prettifyWebView != null) {
            prettifyWebView.setInterceptTouch(true);
            if (toggleNestScrolling) prettifyWebView.setEnableNestedScrolling(false);
        }
    }

    @JavascriptInterface public void stopIntercept() {
        if (prettifyWebView != null) {
            prettifyWebView.setInterceptTouch(false);
            if (toggleNestScrolling) prettifyWebView.setEnableNestedScrolling(true);
        }
    }
}
