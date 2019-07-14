package io.hustler.qtzy.ui.listeners;

import android.graphics.Path;
import android.view.MotionEvent;
import android.view.View;

import io.hustler.qtzy.ui.customviews.DoodleView;

public class DoodleViewTouchListener implements View.OnTouchListener {
    private float mX, mY;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        DoodleView doodleView = (DoodleView) v;
        Path path;
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                path = new Path();
                doodleView.addPath(path);
                path.reset();
                path.moveTo(x, y);
                mX = x;
                mY = y;

                break;
            case MotionEvent.ACTION_MOVE:
                float dX = Math.abs(x - mX);
                float dY = Math.abs(y - mY);
                path = doodleView.getLastPath();
                if (null != path) {
                    path.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
                    mX = x;
                    mY = y;
//                    path.lineTo(x, y);
                }
                break;

        }
        doodleView.invalidate();

        return true;
    }
}
