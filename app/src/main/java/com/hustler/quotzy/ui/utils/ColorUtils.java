package com.hustler.quotzy.ui.utils;

import android.animation.ArgbEvaluator;
import android.app.Activity;

/**
 * Created by anvaya5 on 04/01/2018.
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
public class ColorUtils {
    public static int getHEaderColor(int[] colors, int position, float positionOffset, Activity activity) {


        if (position == colors.length - 1) {
            return colors[position];
        } else {
            int start = colors[position];
            int end = colors[position + 1];
            int color = (int) new ArgbEvaluator().evaluate(positionOffset, start, end);
            return color;
        }
    }

}
