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
/*   Copyright [2018] [Sayi Manoj Sugavasi]

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.*/
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
