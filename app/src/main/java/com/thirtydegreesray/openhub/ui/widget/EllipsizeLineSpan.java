

package com.thirtydegreesray.openhub.ui.widget;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import androidx.annotation.NonNull;
import android.text.style.LineBackgroundSpan;
import android.text.style.LineHeightSpan;
import android.text.style.ReplacementSpan;

public class EllipsizeLineSpan extends ReplacementSpan implements
        LineBackgroundSpan, LineHeightSpan {
    private final Rect mClipRect = new Rect();
    private final int mBottomMargin;

    public EllipsizeLineSpan(int bottomMargin) {
        mBottomMargin = bottomMargin;
    }

    @Override
    public void drawBackground(Canvas c, Paint p, int left, int right, int top,
            int baseline, int bottom, CharSequence text, int start, int end, int lnum) {
        c.getClipBounds(mClipRect);
    }

    @Override
    public int getSize(@NonNull Paint paint, CharSequence text,
                       int start, int end, Paint.FontMetricsInt fm) {
        if (fm != null) {
            paint.getFontMetricsInt(fm);
        }
        int textWidth = (int) Math.ceil(paint.measureText(text, start, end));
        return Math.min(textWidth, mClipRect.width());
    }

    @Override
    public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end,
                     float x, int top, int y, int bottom, @NonNull Paint paint) {
        float textWidth = paint.measureText(text, start, end);

        if (x + (int) Math.ceil(textWidth) < mClipRect.right) {
            //text fits
            canvas.drawText(text, start, end, x, y, paint);
        } else {
            float ellipsisWidth = paint.measureText("\u2026");
            // move 'end' to the ellipsis point
            end = start + paint.breakText(text, start, end, true,
                    mClipRect.right - x - ellipsisWidth, null);
            canvas.drawText(text, start, end, x, y, paint);
            canvas.drawText("\u2026", x + paint.measureText(text, start, end), y, paint);
        }
    }

    @Override
    public void chooseHeight(CharSequence text, int start, int end, int spanstartv,
            int v, Paint.FontMetricsInt fm) {
        fm.descent += mBottomMargin;
        fm.bottom += mBottomMargin;
    }
}