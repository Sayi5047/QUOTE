package com.hustler.quote.ui.customviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

import com.hustler.quote.ui.apiRequestLauncher.Constants;

/**
 * Created by Sayi on 12-10-2017.
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
public class CustomView extends View {

    Paint paint;
    private int circleColor= Constants.DEFAULT_COLOR;

    public CustomView(Context context) {
        super(context);
        init(context,null);

    }

    public CustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);

    }

    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(Context context,AttributeSet attributeSet)
    {
        paint = new Paint();
        paint.setAntiAlias(true);
    }

    public void setCircleColor(int color)
    {
        this.circleColor=circleColor;
        invalidate();
    }

    public int getCircleColor()
    {
        return this.circleColor;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int w=getWidth();
        int h=getHeight();

        int pl=getPaddingLeft();
        int pr=getPaddingRight();
        int pt=getPaddingTop();
        int pb=getPaddingBottom();


        int usableWidth=(w-(pl+pr));
        int usableHeight=(h-(pt+pb));

        int radius=Math.min(usableWidth,usableHeight)/2;
        int cx=pl+(usableWidth/2);
        int cy=pt+(usableHeight/2);

        paint.setColor(circleColor);
        canvas.drawCircle(cx,cy,radius,paint);
    }

}
