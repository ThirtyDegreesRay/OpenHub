

package com.thirtydegreesray.openhub.ui.widget;

import androidx.annotation.NonNull;
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
