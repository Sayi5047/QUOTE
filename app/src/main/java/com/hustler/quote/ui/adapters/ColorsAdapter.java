package com.hustler.quote.ui.adapters;

import android.app.Activity;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hustler.quote.R;

/**
 * Created by anvaya5 on 14/12/2017.
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
public class ColorsAdapter extends RecyclerView.Adapter<ColorsAdapter.ColorViewHolder> {
    OnColorClickListener onColorClickListener;
    Activity activity;
    int[] colors;

    public ColorsAdapter(Activity activity, OnColorClickListener onColorClickListener) {
        this.onColorClickListener = onColorClickListener;
        this.activity = activity;
        resolveColors();
    }

    private void resolveColors() {
        TypedArray colorsArray = activity.getResources().obtainTypedArray(R.array.allColors);
        colors = new int[colorsArray.length()];
        for (int i = 0; i < colorsArray.length(); i++) {
            colors[i] = colorsArray.getColor(i, 0);
        }
        colorsArray.recycle();
    }

    public interface OnColorClickListener {
        void onColorClick(int color);
    }

    @Override
    public ColorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ColorViewHolder(activity.getLayoutInflater().inflate((R.layout.color_layout), parent, false));
    }

    @Override
    public void onBindViewHolder(ColorViewHolder holder, final int position) {
//        DrawableCompat.setTint(holder.imageView.getDrawable(), ContextCompat.getColor(activity,colors[position]));
        holder.imageView.setBackground(new ColorDrawable(colors[position]));

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            holder.imageView.getDrawable().setTint(colors[position]);
//        } else {
//            holder.imageView.setBackgroundColor(colors[position]);
//        }
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onColorClickListener != null) {
                    onColorClickListener.onColorClick(colors[position]);
                } else {
                    Log.e("COLORS ADAPTER", "onClickListner is NUll");
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (colors.length <= 0) {
            return 0;
        } else {
            return colors.length;
        }
    }

    public class ColorViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ColorViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.bt_color);
        }
    }
}
