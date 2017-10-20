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

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.NonNull;
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