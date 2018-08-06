package com.hustler.quote.ui.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.drawable.GradientDrawable;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.ViewAnimationUtils;

import com.hustler.quote.R;

/**
 * Created by Sayi on 02-12-2017.
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
public class AnimUtils {

    public static void revealCircular(FloatingActionButton floatingActionButton, final DialogInterface dialog, View dialog_layout, boolean isDialog_Starting) {

        final View view = dialog_layout.findViewById(R.id.dialog);
        int width = view.getWidth();
        int height = view.getHeight();

        int endradius = (int) Math.hypot(width, height);

        int cx = (int) floatingActionButton.getX() + floatingActionButton.getWidth() / 2;
        int cy = (int) floatingActionButton.getY() + floatingActionButton.getHeight() + 56;

        if (isDialog_Starting) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                Animator animator = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, endradius);
                view.setVisibility(View.VISIBLE);
                animator.setDuration(500);
                animator.start();
            } else {
            }
        } else {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                Animator animator = ViewAnimationUtils.createCircularReveal(view, cx, cy, endradius, 0);
                animator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        dialog.dismiss();
                        view.setVisibility(View.INVISIBLE);
                    }
                });
                animator.setDuration(500);
                animator.start();
            } else {

            }

        }
    }

    public static GradientDrawable createDrawable(int color1, int color2, Activity activity){
        int defaultcolor=activity.getResources().getColor(android.R.color.transparent);
        color1=color1==0?defaultcolor:color1;
        color2=color1==0?defaultcolor:color2;
        int[] colors = {color1, color2};
        GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.BR_TL, colors);
        gradientDrawable.setGradientRadius(135);
        gradientDrawable.setCornerRadius(0f);
        return gradientDrawable;
    }
}
