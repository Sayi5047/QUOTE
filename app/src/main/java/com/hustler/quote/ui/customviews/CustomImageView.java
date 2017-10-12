package com.hustler.quote.ui.customviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.hustler.quote.ui.apiRequestLauncher.Constants;

/**
 * Created by Sayi on 12-10-2017.
 */

public class CustomImageView extends android.support.v7.widget.AppCompatImageView {

    Paint paint;
    private int circleColor= Constants.DEFAULT_COLOR;
    public CustomImageView(Context context) {
        super(context);
        init(context,null);
    }

    public CustomImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public CustomImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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
