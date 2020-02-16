package com.hustler.quote.ui.Widgets;

import androidx.annotation.NonNull;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by Sayi on 12-05-2018.
 */

public class RotationGestureDetector {
    OnRotateGestureListener rotateGestureListener;
    private int pointer_Id_1, pointer_ID_2;
    private float fx;
    private float fy;
    private float sx;
    private float sy;

    public float getmAngle() {
        return mAngle;
    }

    public void setmAngle(float mAngle) {
        this.mAngle = mAngle;
    }

    private float mAngle;
    private static final int INVALID_POINTER_ID = -1;

    public RotationGestureDetector(OnRotateGestureListener onRotateGestureListener) {
        this.rotateGestureListener = onRotateGestureListener;
        pointer_Id_1 = INVALID_POINTER_ID;
        pointer_ID_2 = INVALID_POINTER_ID;
    }

    public boolean onTouchEvent(@NonNull MotionEvent motionEvent) {
        switch (motionEvent.getActionMasked()) {
            case MotionEvent.ACTION_DOWN: {
                pointer_Id_1 = motionEvent.getPointerId(motionEvent.getActionIndex());
            }
            break;
            case MotionEvent.ACTION_POINTER_DOWN: {

                pointer_ID_2 = motionEvent.getPointerId(motionEvent.getActionIndex());
                if (pointer_Id_1 != INVALID_POINTER_ID) {
                    sx = motionEvent.getX(motionEvent.findPointerIndex(pointer_Id_1));
                    sy = motionEvent.getY(motionEvent.findPointerIndex(pointer_Id_1));
                }
                if (pointer_ID_2 != INVALID_POINTER_ID) {
                    fx = motionEvent.getX(motionEvent.findPointerIndex(pointer_ID_2));
                    fy = motionEvent.getY(motionEvent.findPointerIndex(pointer_ID_2));
                }


            }
            break;
            case MotionEvent.ACTION_MOVE: {
                if (pointer_Id_1 != INVALID_POINTER_ID && pointer_ID_2 != INVALID_POINTER_ID) {
                    float nsx, nsy, nfx, nfy;
                    nsx = motionEvent.getX(motionEvent.findPointerIndex(pointer_Id_1));
                    nsy = motionEvent.getY(motionEvent.findPointerIndex(pointer_Id_1));
                    nfx = motionEvent.getX(motionEvent.findPointerIndex(pointer_ID_2));
                    nfy = motionEvent.getY(motionEvent.findPointerIndex(pointer_ID_2));
                    if (rotateGestureListener != null) {
                        rotateGestureListener.onRotate(this);
                    }

                    mAngle = calculateAngle(fx, fy, sx, sy, nfx, nfy, nsx, nsy);
                }
            }
            break;
            case MotionEvent.ACTION_UP: {
                pointer_Id_1 = INVALID_POINTER_ID;
            }
            break;
            case MotionEvent.ACTION_POINTER_UP: {
                pointer_ID_2 = INVALID_POINTER_ID;
            }
            break;
            case MotionEvent.ACTION_CANCEL: {
                pointer_ID_2 = INVALID_POINTER_ID;
                pointer_Id_1 = INVALID_POINTER_ID;
            }
            break;
        }
        return true;
    }

    private float calculateAngle(float fx, float fy, float sx, float sy, float nfx, float nfy, float nsx, float nsy) {
        Log.d("Rotational values", "fx--" + fx + "," + "fy--" + fy + "," + "sx--" + sx + "," + "sy--" + sy + "," + "nfx--" + nfx + "," + "nfy--" + nfy + "," + "nsx--" + nsx + "," + "nsy--" + nsy);
        float angle, angle1, angle2;
        angle1 = ((float) Math.atan2((fy - sy), (fx - sx)));
        Log.d("Angle 1-->", angle1 + "");
        angle2 = ((float) Math.atan2((nfy - nsy), (nfx - nsx)));
        Log.d("Angle 2-->", angle2 + "");

        angle = ((float) Math.toDegrees(angle1 - angle2)) % 360;
        Log.d("Angle-->", angle + "");

        if (angle < -180.f) angle += 360.0f;
        if (angle > 180.f) angle -= 360.0f;
        Log.d("Result Angle-->", angle + "");

        return -angle;
    }


    public interface OnRotateGestureListener {
        void onRotate(RotationGestureDetector rotationGestureDetector);
    }

}
