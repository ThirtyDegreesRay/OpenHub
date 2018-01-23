package com.thirtydegreesray.openhub.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.style.ReplacementSpan;

import com.thirtydegreesray.openhub.R;
import com.thirtydegreesray.openhub.mvp.model.Label;
import com.thirtydegreesray.openhub.util.ViewUtils;

/**
 * Created by ThirtyDegreesRay on 2018/1/21 15:18:55
 */

public class IssueLabelSpan extends ReplacementSpan {

    private int textColor;
    private int bgColor;
    private float padding;
    private float margin;
    private float bgRadius;

    private int ascent;
    private int descent;

    public IssueLabelSpan(@NonNull Context context, @NonNull Label label) {
        super();
        bgColor = label.getColorValue();
        textColor = ViewUtils.getLabelTextColor(context, bgColor);
        padding = context.getResources().getDimension(R.dimen.spacing_micro);
        margin = context.getResources().getDimension(R.dimen.spacing_micro);
        bgRadius = context.getResources().getDimension(R.dimen.spacing_micro);
    }

    @Override
    public int getSize(@NonNull Paint paint, CharSequence text, int start, int end,
                       @Nullable Paint.FontMetricsInt fm) {
        if(fm != null){
            paint.getFontMetricsInt(fm);
            ascent = -fm.ascent;
            descent = fm.descent;
            fm.top -= (padding + margin);
            fm.ascent -= (padding + margin);
            fm.bottom += (padding + margin);
            fm.descent += (padding + margin);
        }

        int textWidth = (int) Math.ceil(paint.measureText(text, start, end));
        return (int) (textWidth + 2 * (padding + margin));
    }

    @Override
    public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x,
                     int top, int y, int bottom, @NonNull Paint paint) {
        float textSize = paint.measureText(text, start, end);

        float bgLeft = x + margin;
        float bgRight = bgLeft + textSize + 2 * padding;
        float bgTop = y - ascent - padding;
        float bgBottom = y + descent + padding;
        RectF rectF = new RectF(bgLeft, bgTop, bgRight, bgBottom);

        paint.setColor(bgColor);
        canvas.drawRoundRect(rectF, bgRadius, bgRadius, paint);

        paint.setColor(textColor);
        canvas.drawText(text, start, end, x + padding + margin, y, paint);

    }

}
