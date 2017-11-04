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

package com.thirtydegreesray.openhub.ui.widget;

import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by ThirtyDegreesRay on 2017/11/4 22:15:40
 */

public class DoubleClickHandler {

    private final static float MAX_CLICK_INTERVAL = 500;

    private long firstClickTime = 0;

    public static void setDoubleClickListener(@NonNull final View view,
                                              @NonNull final DoubleClickListener listener){
        new DoubleClickHandler(view, listener);
    }

    private DoubleClickHandler(@NonNull final View view,
                              @NonNull final DoubleClickListener listener) {
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    long curTime = System.currentTimeMillis();
                    if(curTime - firstClickTime <= MAX_CLICK_INTERVAL){
                        listener.onDoubleClick(view);
                        firstClickTime = 0;
                    } else {
                        firstClickTime = curTime;
                    }
                    return true;
                }
                return false;
            }
        });
    }

    public interface DoubleClickListener{
        void onDoubleClick(View view);
    }

}
