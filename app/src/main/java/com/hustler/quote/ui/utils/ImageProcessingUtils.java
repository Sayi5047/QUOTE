package com.hustler.quote.ui.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.renderscript.Type;
import android.support.annotation.RequiresApi;

/**
 * Created by anvaya5 on 21/12/2017.
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
public class ImageProcessingUtils {

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static Bitmap create_blur(Bitmap src_Bitmap, float radius, Activity activity) {
        /*Handle radius*/
        if (radius <= 0) {
            radius = 0.0f;
        } else if (radius >= 25) {
            radius = 25.0f;
        }

        /*Create a bitmap with the sousrce*/
//        Bitmap bitmap = Bitmap.createBitmap(src.getWidth(), src.getHeight(), Bitmap.Config.ARGB_8888);
        /*Create a renderscript object*/
        RenderScript renderScript = RenderScript.create(activity);

        Allocation blurr_Input_Allocation = Allocation.createFromBitmap(renderScript, src_Bitmap);
        Type type = blurr_Input_Allocation.getType();
        Allocation blurr_Output_Allocation = Allocation.createTyped(renderScript, type);


//        Create ScriptIntrensicBlur object (Hero of the story)

        ScriptIntrinsicBlur intrinsicBlur = ScriptIntrinsicBlur.create(renderScript, blurr_Input_Allocation.getElement());

        /* SET INPUT
        * SET RADIUS
        * SET FOR EACH PIXEL IN OUTPUT ALLOCATION
        * COPY THE DATA TO BITMAP
        * DESTORY IT
        * RETURN IT*/
        intrinsicBlur.setRadius(radius);
        intrinsicBlur.setInput(blurr_Input_Allocation);
        intrinsicBlur.forEach(blurr_Output_Allocation);
//        Copy to bitmap
//        destroy all to free memory
        blurr_Output_Allocation.copyTo(src_Bitmap);
        blurr_Input_Allocation.destroy();
        intrinsicBlur.destroy();

        return src_Bitmap;


    }
}
