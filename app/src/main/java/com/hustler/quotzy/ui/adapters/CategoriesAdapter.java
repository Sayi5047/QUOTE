package com.hustler.quotzy.ui.adapters;

import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hustler.quotzy.R;
import com.hustler.quotzy.ui.apiRequestLauncher.Constants;
import com.hustler.quotzy.ui.utils.TextUtils;

import java.util.ArrayList;

/**
 * Created by anvaya5 on 20/12/2017.
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
public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.CategoryViewHolder> {
    Activity activity;
    String categoriesString = Constants.CATEGORIES_STRING;
    String[] categoriesListArray;
    ArrayList<String> categoryArrayList = new ArrayList<>();
    OnCategoryClickListener categoryClickListener;


    public CategoriesAdapter(Activity activity, OnCategoryClickListener categoryClickListener) {
        this.activity = activity;
        this.categoryClickListener = categoryClickListener;
        convertvals();

    }

    public CategoriesAdapter(Activity activity) {
        this.activity = activity;
        convertvals();
    }

    private void convertvals() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                categoriesListArray = categoriesString.split("\n");
                for (int i = 0; i < categoriesListArray.length; i++) {
                    categoryArrayList.add(i, categoriesListArray[i]);
                }
            }
        }).run();


    }

    public interface OnCategoryClickListener {
        void onCategoryClicked(String category, String cate2, int position, GradientDrawable drawable);
    }


    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CategoryViewHolder(activity.getLayoutInflater().inflate(R.layout.category_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final CategoryViewHolder holder, final int position) {
        final GradientDrawable gradientDrawable = createDrawable(holder);
        holder.textView.setText(categoryArrayList.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (categoryClickListener != null) {
                    categoryClickListener.onCategoryClicked(categoryArrayList.get(position), position == 0 ? " " :
                            categoryArrayList.get(position - 1), position, gradientDrawable);
                }
            }
        });

    }

    private GradientDrawable createDrawable(CategoryViewHolder holder) {
        int color1 = TextUtils.getMatColor(activity, "mdcolor_500");
        int color2 = TextUtils.getMatColor(activity, "mdcolor_500");
        int[] colors = {color1, color2};
        GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.BR_TL, colors);
        gradientDrawable.setGradientRadius(135);
        gradientDrawable.setCornerRadius(20f);
        holder.imageView.setBackground(gradientDrawable);
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
    public int getItemCount() {
        if (categoryArrayList.size() <= 0) {
            return 0;
        } else {
            return categoryArrayList.size();
        }
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;

        public CategoryViewHolder(View itemView) {

            super(itemView);
            textView = itemView.findViewById(R.id.bt_category);
            imageView = itemView.findViewById(R.id.back_image);
            TextUtils.setFont(activity, textView, Constants.FONT_CIRCULAR);
//            textView.setShadowLayer(1f, 2, 2, activity.getResources().getColor(android.R.color.black));

        }
    }
}
