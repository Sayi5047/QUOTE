package com.hustler.quote.ui.CustomSpan;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.style.ReplacementSpan;

/**
 * Created by anvaya5 on 22/01/2018.
 */

public class HollowSpan extends ReplacementSpan {
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Path path = new Path();
    private int width ;

    public HollowSpan(int strokeWidth) {
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(strokeWidth);
    }

    @Override
    public int getSize(@NonNull Paint paint, CharSequence text,
                       @IntRange(from = 0) int start,
                       @IntRange(from = 0) int end,
                       @Nullable Paint.FontMetricsInt fontMetricsInt) {
        this.paint.setColor(paint.getColor());
        width = (int) (paint.measureText(text, start, end) + this.paint.getStrokeWidth());
        return width;
    }

    @Override
    public void draw(@NonNull Canvas canvas,
                     CharSequence text,
                     @IntRange(from = 0) int start,
                     @IntRange(from = 0) int end,
                     float x,
                     int top,
                     int y,
                     int bottom,
                     @NonNull Paint paint) {
        path.reset();
        paint.getTextPath(text.toString(), start, end, x, y, path);
        path.close();
        canvas.translate(this.paint.getStrokeWidth() / 2, 0);
        canvas.drawPath(path, this.paint);
        canvas.translate(-this.paint.getStrokeWidth() / 2, 0);
    }

    public void setPathEffect(PathEffect effect) {
        paint.setPathEffect(effect);
    }
}
