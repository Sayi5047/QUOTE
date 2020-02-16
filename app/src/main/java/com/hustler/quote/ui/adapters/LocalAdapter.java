package com.hustler.quote.ui.adapters;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hustler.quote.ui.apiRequestLauncher.Constants;

import java.util.ArrayList;

import com.hustler.quote.R;
import com.hustler.quote.ui.ORM.Tables.QuotesTable;
import com.hustler.quote.ui.utils.TextUtils;

/**
 * Created by anvaya5 on 27/12/2017.
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
public class LocalAdapter extends RecyclerView.Adapter<LocalAdapter.LocalViewholder> {
    Activity activity;
    private ArrayList<QuotesTable> dataFromNet;
    private OnQuoteClickListener onQuoteClickListener;

    public LocalAdapter(Activity activity, ArrayList<QuotesTable> dataFromNet, OnQuoteClickListener onQuoteClickListener) {
        this.activity = activity;
        this.dataFromNet = dataFromNet;
        this.onQuoteClickListener = onQuoteClickListener;
    }


    public interface OnQuoteClickListener {
        void onQuoteClicked(int position, GradientDrawable color, QuotesTable quote, View view);
    }

    @NonNull
    @Override
    public LocalViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LocalViewholder(activity.getLayoutInflater().inflate(R.layout.rv_item, parent, false));
    }

    public void addData(@NonNull ArrayList<QuotesTable> extraData) {
        dataFromNet = null;
        notifyDataSetChanged();
        dataFromNet = new ArrayList<>();
        dataFromNet.addAll(extraData);
        notifyItemRangeInserted(0, dataFromNet.size());
        notifyDataSetChanged();
    }

    @NonNull
    private GradientDrawable createDrawable(LocalViewholder holder) {
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
    public void onBindViewHolder(@NonNull final LocalViewholder holder, final int position) {
        final GradientDrawable gradientDrawable = createDrawable(holder);
        final QuotesTable quote = dataFromNet.get(position);
        String genre;

        genre = quote.getCategory();

        holder.tv.setText(quote.getQuotes());
        holder.tv.setTextColor(Color.WHITE);
        holder.tv2.setTextColor(Color.WHITE);
        holder.tv3.setTextColor(Color.WHITE);
        holder.tv2.setText(quote.getAuthor());
        holder.tv3.setText(genre);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onQuoteClickListener != null) {
                    onQuoteClickListener.onQuoteClicked(position, gradientDrawable, quote, holder.cardView);
                }
            }
        });
        int visibility = quote.isIsliked() ? View.VISIBLE : View.GONE;
        holder.imageView.setVisibility(visibility);
    }

    @Override
    public int getItemCount() {
        dataFromNet.size();
        return dataFromNet.size();
    }


    public class LocalViewholder extends RecyclerView.ViewHolder {
        TextView tv, tv2, tv3;
        ImageView imageView;
        LinearLayout rootView;
        CardView cardView;

        public LocalViewholder(@NonNull View itemView) {
            super(itemView);
            rootView = itemView.findViewById(R.id.root_layout);
            cardView = itemView.findViewById(R.id.iv_quote_card_root);
            imageView = itemView.findViewById(R.id.fav_iv);

            cardView.setClipToPadding(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                cardView.setClipToOutline(true);
            }
            tv = itemView.findViewById(R.id.main_quote);
            tv2 = itemView.findViewById(R.id.author);
            tv3 = itemView.findViewById(R.id.quote_genre_end);
            TextUtils.setFont(activity, tv, Constants.FONT_CIRCULAR);
            TextUtils.setFont(activity, tv2, Constants.FONT_CIRCULAR);
            TextUtils.setFont(activity, tv3, Constants.FONT_CIRCULAR);

        }
    }
}
