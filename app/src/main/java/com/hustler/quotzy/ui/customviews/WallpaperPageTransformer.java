package com.hustler.quotzy.ui.customviews;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Created by Sayi on 27-01-2018.
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
public class WallpaperPageTransformer implements ViewPager.PageTransformer {
    private int border = 0;
    private float speed = 0.8f;
    int imageId;

    public WallpaperPageTransformer(int imageId) {
        this.imageId = imageId;
    }




    public void setBorder(int px) {
        border = px;
    }

    @Override
    public void transformPage(@NonNull View page, float position) {
        View parralexView = page.findViewById(imageId);
        if (page == null) {

        } else {
            if (parralexView != null && position > -1 && position < 1) {
                float width = parralexView.getWidth();

                parralexView.setTranslationX(-(position * width * speed));
                float scaleFactor = ((float) page.getWidth() - border) / page.getWidth();
                if (position == 0) {
                    page.setScaleX(1);
                    page.setScaleY(1);

                } else {
                    page.setScaleX(scaleFactor);
                    page.setScaleY(scaleFactor);
                }
            }
        }
    }
}
