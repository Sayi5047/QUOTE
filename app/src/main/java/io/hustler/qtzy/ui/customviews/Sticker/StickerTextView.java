package io.hustler.qtzy.ui.customviews.Sticker;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class StickerTextView extends StickerView {
    private AutoResizeTextView tv_main;

    public StickerTextView(@NonNull Context context) {
        super(context);
    }

    public StickerTextView(@NonNull Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StickerTextView(@NonNull Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public View getMainView() {
        if (tv_main != null)
            return tv_main;

        tv_main = new AutoResizeTextView(getContext());
        //tv_main.setTextSize(22);
        tv_main.setTextColor(Color.WHITE);
        tv_main.setGravity(Gravity.CENTER);
        tv_main.setTextSize(400);
        tv_main.setShadowLayer(4, 0, 0, Color.BLACK);
        tv_main.setMaxLines(1);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        params.gravity = Gravity.CENTER;
        tv_main.setLayoutParams(params);
        if (getImageViewFlip() != null)
            getImageViewFlip().setVisibility(View.GONE);
        return tv_main;
    }

    public void setText(String text) {
        if (tv_main != null)
            tv_main.setText(text);
    }


    @Nullable
    public String getText() {
        if (tv_main != null)
            return tv_main.getText().toString();

        return null;
    }

    public static float pixelsToSp(Context context, float px) {
        float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        return px / scaledDensity;
    }

    @Override
    protected void onScaling(boolean scaleUp) {
        super.onScaling(scaleUp);
    }


    public void setTextColor(int color) {
        tv_main.setTextColor(color);
    }

    public void setTextSize(float v) {
        tv_main.setTextSize(v);
    }

    public void setMaxWidth(int width) {
        tv_main.setMaxWidth(width);
    }

    public void setTypeface(Typeface fromAsset) {
        tv_main.setTypeface(fromAsset);
    }

    public void setGravity(int center) {
        tv_main.setGravity(center);
    }

    public void setText(SpannableString spannableString) {
        tv_main.setText(spannableString);
    }
}

