package com.hustler.quotzy.ui.adapters;

import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hustler.quotzy.R;
import com.hustler.quotzy.ui.apiRequestLauncher.Constants;
import com.hustler.quotzy.ui.utils.TextUtils;

/**
 * Created by Sayi on 01-02-2018.
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
public class ImageCategoryAdapter extends RecyclerView.Adapter<ImageCategoryAdapter.CategoryViewHolder> {
    OnImageClickLitner litner;
    String[] cats;
    Activity activity;

    public ImageCategoryAdapter(Activity activity, OnImageClickLitner litner) {
        this.activity = activity;
        this.litner = litner;
        bringAllTheQuotes();
    }

    private void bringAllTheQuotes() {
        cats = activity.getResources().getStringArray(R.array.images_categories);
    }


    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CategoryViewHolder(activity.getLayoutInflater().inflate(R.layout.image_category_item, parent, false));
    }

    private GradientDrawable createDrawable(CategoryViewHolder holder) {
        int color1 = TextUtils.getMatColor(activity, "mdcolor_500");
        int color2 = TextUtils.getMatColor(activity, "mdcolor_500");
        int[] colors = {color1, color2};
        GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.BR_TL, colors);
        gradientDrawable.setGradientRadius(135);
        gradientDrawable.setCornerRadius(20f);
        holder.itemView.setBackground(gradientDrawable);
        return gradientDrawable;
//        holder.imageView.layout(0,0,100,100);
//
//        holder.imageView.setDrawingCacheEnabled(true);
//        holder.imageView.buildDrawingCache();
//        Bitmap bitmap=holder.imageView.getDrawingCache();
//        Bitmap finalbitmap =ImageProcessingUtils.create_blur(bitmap,5.0f,activity);
//        holder.imageView.setImageBitmap(finalbitmap);
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, final int position) {

        final int color = TextUtils.getMainMatColor("mdcolor_400", activity);
        createDrawable(holder);
        holder.category.setText(cats[position]);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            holder.itemView.setClipToOutline(true);
        }
        TextUtils.setFont(activity, holder.category, Constants.FONT_CIRCULAR);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (litner != null) {
                    litner.onCategoryClicked(position, cats[position]);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return cats.length <= 0 ? 0 : cats.length;
    }

    public interface OnImageClickLitner {
        void onCategoryClicked(int position, String category);
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView category;

        public CategoryViewHolder(View itemView) {
            super(itemView);
            category = itemView.findViewById(R.id.category);
        }
    }
}
