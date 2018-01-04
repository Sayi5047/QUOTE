package com.hustler.quote.ui.adapters;

import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hustler.quote.R;
import com.hustler.quote.ui.apiRequestLauncher.Constants;
import com.hustler.quote.ui.utils.TextUtils;

import java.util.ArrayList;

/**
 * Created by anvaya5 on 20/12/2017.
 */

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
        categoriesListArray = categoriesString.split("\n");
        for (int i = 0; i < categoriesListArray.length; i++) {
            categoryArrayList.add(i, categoriesListArray[i]);
        }

    }

    public interface OnCategoryClickListener {
        void onCategoryClicked(String category, int position);
    }


    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CategoryViewHolder(activity.getLayoutInflater().inflate(R.layout.category_item, parent, false));
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, final int position) {
        createDrawable(holder);
        holder.textView.setText(categoryArrayList.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (categoryClickListener != null) {
                    categoryClickListener.onCategoryClicked(categoryArrayList.get(position), position);
                }
            }
        });

    }

    private void createDrawable(CategoryViewHolder holder) {
        int color1 = TextUtils.getMatColor(activity);
        int color2 = TextUtils.getMatColor(activity);
        int[] colors = {color1, color2};
        GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.BR_TL, colors);
        gradientDrawable.setGradientRadius(135);
        gradientDrawable.setCornerRadius(0f);
        holder.imageView.setBackground(gradientDrawable);
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
            textView = (TextView) itemView.findViewById(R.id.bt_category);
            imageView = (ImageView) itemView.findViewById(R.id.back_image);
            TextUtils.setFont(activity, textView, Constants.FONT_Google_sans_regular);
        }
    }
}
