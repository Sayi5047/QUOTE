package io.hustler.qtzy.ui.customviews;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

public class DoodleView extends View {

    private ArrayList<Path> paths = new ArrayList<>();
    private ArrayList<Integer> colors = new ArrayList<>();
    private ArrayList<Boolean> shadowEnabledArray = new ArrayList<>();
    private ArrayList<Integer> brushWidthArray = new ArrayList<>();

    private ArrayList<Path> backupPaths = new ArrayList<>();
    private ArrayList<Integer> backupColors = new ArrayList<>();
    private ArrayList<Boolean> backupShadowEnabledArray = new ArrayList<>();
    private ArrayList<Integer> backupbrushWidthArray = new ArrayList<>();

    private int color;
    private Integer brushSize;
    private Paint paint;


    private EmbossMaskFilter embossMaskFilter;
    private BlurMaskFilter blurMaskFilter;
    private boolean blur;
    private boolean emboss;
    private int backgroundColor;
    LinearGradient gradientDrawable = null;


    int DEFAULT_BG_COLOR = Color.WHITE;
    Context context;
    private boolean isShadowEnabled = false;


    public DoodleView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public DoodleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DoodleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public DoodleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setDither(true);
        paint.setAntiAlias(true);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStyle(Paint.Style.STROKE);

        embossMaskFilter = new EmbossMaskFilter(new float[]{1, 1, 1}, 0.4f, 6, 3.5f);
        blurMaskFilter = new BlurMaskFilter(5, BlurMaskFilter.Blur.NORMAL);
        color = Color.LTGRAY;
        isShadowEnabled = false;
        brushSize = 15;
    }

    public void addPath(Path path) {
        paths.add(path);
        colors.add(color);
        shadowEnabledArray.add(isShadowEnabled);
        brushWidthArray.add(brushSize);


    }

    public void setSetColor(int color) {
        this.color = color;
    }

    public void setBrushSize(Integer brushSize) {
        this.brushSize = brushSize;
    }

    public void EnableShadow(boolean isShadowEnabled) {
        this.isShadowEnabled = isShadowEnabled;
    }

    public Path getLastPath() {
        if (paths.size() > 0) {
            return paths.get(paths.size() - 1);
        }
        return null;
    }

    public Integer getLastColor() {
        if (colors.size() > 0) {
            return colors.get(colors.size() - 1);
        }
        return null;
    }

    public Integer getLastBrushWidth() {
        if (brushWidthArray.size() > 0) {
            return brushWidthArray.get(brushWidthArray.size() - 1);
        }
        return null;
    }

    public Boolean getLastShadowVal() {
        if (shadowEnabledArray.size() > 0) {
            return shadowEnabledArray.get(shadowEnabledArray.size() - 1);
        }
        return null;
    }


    public Path getLastBackupPath() {
        if (backupPaths.size() > 0) {
            return backupPaths.get(backupPaths.size() - 1);
        }
        return null;
    }

    public Integer getLastBackupColor() {
        if (backupColors.size() > 0) {
            return backupColors.get(backupColors.size() - 1);
        }
        return null;
    }

    public Integer getLastBackupBrushWidth() {
        if (backupbrushWidthArray.size() > 0) {
            return backupbrushWidthArray.get(backupbrushWidthArray.size() - 1);
        }
        return null;
    }

    public Boolean getLastBackupShadowVal() {
        if (backupShadowEnabledArray.size() > 0) {
            return backupShadowEnabledArray.get(backupShadowEnabledArray.size() - 1);
        }
        return null;
    }


    public void normal() {
        emboss = false;
        blur = false;
        isShadowEnabled = false;
        color = Color.LTGRAY;
        brushSize = 15;


    }

    public void emboss() {
        emboss = true;
        blur = false;
    }

    public void blur() {
        emboss = false;
        blur = true;
    }

    public void clear() {
        backgroundColor = DEFAULT_BG_COLOR;

        paths.clear();
        colors.clear();
        shadowEnabledArray.clear();
        brushWidthArray.clear();

        backupColors.clear();
        backupPaths.clear();
        backupShadowEnabledArray.clear();
        backupbrushWidthArray.clear();

        color = Color.LTGRAY;
        normal();
        invalidate();
    }


    public void removeLastPath() {
        if (paths.contains(getLastPath())) {

            backupPaths.add(getLastPath());
            backupColors.add(getLastColor());
            backupShadowEnabledArray.add(getLastShadowVal());
            backupbrushWidthArray.add(getLastBrushWidth());

            paths.remove(getLastPath());
            colors.remove(getLastColor());
            shadowEnabledArray.remove(getLastShadowVal());
            brushWidthArray.remove(getLastBrushWidth());
        }
        invalidate();
    }


    public void restoreLastRemovedPath() {
        if (backupPaths.contains(getLastBackupPath())) {

            paths.add(getLastBackupPath());
            colors.add(getLastBackupColor());
            shadowEnabledArray.add(getLastBackupShadowVal());
            brushWidthArray.add(getLastBackupBrushWidth());

            backupPaths.remove(getLastBackupPath());
            backupColors.remove(getLastBackupColor());
            backupShadowEnabledArray.remove(getLastBackupShadowVal());
            backupbrushWidthArray.remove(getLastBackupBrushWidth());

        }
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        assert null != paths;
        int i = 0;
        for (Path path : paths) {

            paint.setColor(colors.get(i));
            if ((0 < shadowEnabledArray.size())) {
                if (shadowEnabledArray.get(i)) {
                    paint.setShadowLayer(5, 5, 15, colors.get(i));
                } else {
                    paint.setShadowLayer(0, 0, 0, Color.TRANSPARENT);
                }
            }
            if (brushWidthArray.size() > 0) {
                paint.setStrokeWidth(brushWidthArray.get(i));

            }
            canvas.drawPath(path, paint);
            i++;

//            paint.setShader(gradientDrawable);

//            paint.setTextSize(20f);
//            canvas.drawTextOnPath("SomethingSomethingSomethingSomethingSomethingSomethingSomethingSomethingSomethingSomethingSomethingSomethingSomethingSomethingSomethingSomethingSomethingSomethingSomethingSomethingSomethingSomethingSomethingSomethingSomethingSomethingSomethingSomethingSomethingSomethingSomethingSomethingSomethingSomethingSomethingSomethingSomethingSomethingSomethingSomethingSomethingSomethingSomethingSomethingSomethingSomethingSomethingSomethingSomethingSomethingSomething",
//                    path, 0, 20, paint);
//        }

        }
    }
}
