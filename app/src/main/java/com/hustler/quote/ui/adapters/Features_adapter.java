package com.hustler.quote.ui.adapters;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
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
 * Created by anvaya5 on 07/12/2017.
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
public class Features_adapter extends RecyclerView.Adapter<Features_adapter.FeaturesViewholder> {
    Activity activity;
    OnFeature_ItemClickListner listner;
    int itemsfromActivity_Count;
    String arrayName;
    ArrayList<String> itemstobe_used = new ArrayList<>();
    ArrayList<Drawable> itemstobe_used_images = new ArrayList<>();
    String current_item = null;
    boolean isText;


    public Features_adapter(Activity activity, String arrayName, int itemsfromActivity, OnFeature_ItemClickListner listner, boolean isText) {
        this.activity = activity;
        this.itemsfromActivity_Count = itemsfromActivity;
        this.listner = listner;
        this.arrayName = arrayName;
        this.isText = isText;

        prepare_text_features();
        addImages();
    }

    private void addImages() {
        if (isText) {
            itemstobe_used_images.add(getDrawable(R.drawable.ic_text_format_black_24dp));
            itemstobe_used_images.add(getDrawable(R.drawable.ic_edit_black));
            itemstobe_used_images.add(getDrawable(R.drawable.ic_font_sizes));
            itemstobe_used_images.add(getDrawable(R.drawable.ic_rotate_right_black_24dp));
            itemstobe_used_images.add(getDrawable(R.drawable.ic_spacing));
            itemstobe_used_images.add(getDrawable(R.drawable.ic_text_color));
            itemstobe_used_images.add(getDrawable(R.drawable.ic_format_line_spacing_black_24dp));
            itemstobe_used_images.add(getDrawable(R.drawable.ic_full_width));
            itemstobe_used_images.add(getDrawable(R.drawable.ic_shadow));
            itemstobe_used_images.add(getDrawable(R.drawable.ic_font_bt));
            itemstobe_used_images.add(getDrawable(R.drawable.ic_gradient));
            itemstobe_used_images.add(getDrawable(R.drawable.ic_canvas_size));
            itemstobe_used_images.add(getDrawable(R.drawable.ic_text_fx));

        } else {
            itemstobe_used_images.add(getDrawable(R.drawable.ic_gallery_bg));
            itemstobe_used_images.add(getDrawable(R.drawable.ic_colors_bg));
            itemstobe_used_images.add(getDrawable(R.drawable.ic_blur_on_black_24dp));
            itemstobe_used_images.add(getDrawable(R.drawable.ic_filter_b_and_w_black_24dp));
            itemstobe_used_images.add(getDrawable(R.drawable.ic_filter_white));
            itemstobe_used_images.add(getDrawable(R.drawable.ic_gradient));
            itemstobe_used_images.add(getDrawable(R.drawable.ic_search_black_24dp));

        }
    }

    private Drawable getDrawable(int drawable) {
        return ContextCompat.getDrawable(activity, drawable);
    }

    private void prepare_text_features() {
        for (int i = 0; i < itemsfromActivity_Count; i++) {
            itemstobe_used.add(i, TextUtils.getArrayItem_of_String(activity, arrayName, i));
        }
    }

    @Override
    public FeaturesViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (isText) {
            return new FeaturesViewholder(activity.getLayoutInflater().inflate((R.layout.features_item), parent, false));

        } else {
            return new FeaturesViewholder(activity.getLayoutInflater().inflate((R.layout.feature_item_2), parent, false));

        }
    }

    @Override
    public void onBindViewHolder(final FeaturesViewholder holder, final int position) {
        holder.feature_name.setText(itemstobe_used.get(position));
        holder.feature_item.setImageDrawable(itemstobe_used_images.get(position));
        current_item = itemstobe_used.get(position);
//        Toast_Snack_Dialog_Utils.show_ShortToast(activity,"TOAST 1"+position+"");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listner.onItemClick(itemstobe_used.get(position));


            }
        });
//        button animation for
//        holder.itemView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN: {
//                        AnimatorSet set = (AnimatorSet) AnimatorInflater.loadAnimator(activity, R.animator.squeeze_in);
//                        set.setTarget(v);
//                        set.start();
//
//                    }
//                    break;
//                    case MotionEvent.ACTION_UP: {
//                        AnimatorSet set = (AnimatorSet) AnimatorInflater.loadAnimator(activity, R.animator.squeeze_out);
//                        set.setTarget(v);
//                        set.start();
//
//                    }
//                    case MotionEvent.ACTION_MOVE:
//                    {
//
//                    }
//
//                }
//                return true;
//
//            }
//        });

    }

    @Override
    public int getItemCount() {
        if (itemstobe_used.size() <= 0) {
            return 0;
        } else {
            return itemstobe_used.size();
        }
    }

    public interface OnFeature_ItemClickListner {
        void onItemClick(String clickedItem);
    }

    public class FeaturesViewholder extends RecyclerView.ViewHolder {
        ImageView feature_item;
        TextView feature_name;

        public FeaturesViewholder(View itemView) {
            super(itemView);
            feature_item = itemView.findViewById(R.id.feature_item);
            feature_name = itemView.findViewById(R.id.feature_name);
            TextUtils.setFont(activity, feature_name, Constants.FONT_CIRCULAR);
        }
    }
}
