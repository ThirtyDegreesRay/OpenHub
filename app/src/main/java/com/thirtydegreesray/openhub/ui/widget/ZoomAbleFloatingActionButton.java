package com.thirtydegreesray.openhub.ui.widget;

import android.animation.AnimatorSet;
import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;

import com.thirtydegreesray.openhub.util.AnimatorHelper;

/**
 * Created by ThirtyDegreesRay on 2017/12/25 11:46:59
 */

public class ZoomAbleFloatingActionButton extends FloatingActionButton {

    private AnimatorSet zoomInAnimatorSet;
    private AnimatorSet zoomOutAnimatorSet;

    private boolean isNormalSize = true;

    public ZoomAbleFloatingActionButton(Context context) {
        super(context);
    }

    public ZoomAbleFloatingActionButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ZoomAbleFloatingActionButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void zoomIn(){
        if(zoomInAnimatorSet == null){
            zoomInAnimatorSet = AnimatorHelper.getZoomAnimatorSet(this, 0);
        }
        if(zoomInAnimatorSet.isRunning() || !isNormalSize){
            return;
        }
        zoomInAnimatorSet.start();
        isNormalSize = false;
    }

    public void zoomOut(){
        if(zoomOutAnimatorSet == null){
            zoomOutAnimatorSet = AnimatorHelper.getZoomAnimatorSet(this, 1);
        }
        if(zoomOutAnimatorSet.isRunning() || isNormalSize){
            return;
        }
        zoomOutAnimatorSet.start();
        isNormalSize = true;
    }

}
