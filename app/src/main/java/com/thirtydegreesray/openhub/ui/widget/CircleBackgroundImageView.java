package com.thirtydegreesray.openhub.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.thirtydegreesray.openhub.R;

/**
 * Created by ThirtyDegreesRay on 2018/1/16 15:13:43
 */

public class CircleBackgroundImageView extends AppCompatImageView {

    private int backgroundColor;
    private Paint paint ;

    public CircleBackgroundImageView(Context context) {
        super(context);
        init(null);
    }

    public CircleBackgroundImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CircleBackgroundImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray tp = getContext().obtainStyledAttributes(attrs, R.styleable.CircleBackgroundImageView);
            try {
                backgroundColor = tp.getInt(R.styleable.CircleBackgroundImageView_background_color, 0);
                paint = new Paint();
                paint.setColor(backgroundColor);
                paint.setFlags(Paint.ANTI_ALIAS_FLAG);
            } finally {
                tp.recycle();
            }
        }
    }

    @Override
    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        paint.setColor(backgroundColor);
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(paint != null){
            float size = getWidth() / 2.0f;
            canvas.drawCircle(size, size, size, paint);
        }
        super.onDraw(canvas);
    }
}
