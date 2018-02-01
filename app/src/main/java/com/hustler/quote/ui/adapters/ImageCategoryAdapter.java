package com.hustler.quote.ui.adapters;

import android.app.Activity;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hustler.quote.R;
import com.hustler.quote.ui.apiRequestLauncher.Constants;
import com.hustler.quote.ui.utils.TextUtils;

/**
 * Created by Sayi on 01-02-2018.
 */

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

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, final int position) {

        final int color = TextUtils.getMainMatColor("mdcolor_100", activity);

        holder.category.setText(cats[position]);
        holder.itemView.setBackgroundColor(color);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            holder.itemView.setClipToOutline(true);
        }
        TextUtils.setFont(activity, holder.category, Constants.FONT_NEVIS);
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
        public void onCategoryClicked(int position, String category);
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView category;

        public CategoryViewHolder(View itemView) {
            super(itemView);
            category = (TextView) itemView.findViewById(R.id.category);
        }
    }
}
