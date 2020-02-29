package com.hustler.quote.ui.adapters;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.hustler.quote.R;
import com.hustler.quote.ui.ORM.Tables.QuotesTable;
import com.hustler.quote.ui.apiRequestLauncher.Constants;
import com.hustler.quote.ui.utils.TextUtils;

import java.util.ArrayList;

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
public class QuotesAdapter extends RecyclerView.Adapter<QuotesAdapter.LocalViewholder> {
    Activity activity;
    private ArrayList<QuotesTable> dataFromTables = new ArrayList<>();
    private OnQuoteClickListener onQuoteClickListener;

    public QuotesAdapter(Activity activity, OnQuoteClickListener onQuoteClickListener) {
        this.activity = activity;
        this.onQuoteClickListener = onQuoteClickListener;
    }

    public QuotesAdapter(Activity activity, ArrayList<QuotesTable> dataFromTables, OnQuoteClickListener onQuoteClickListener) {
        this.activity = activity;
        this.dataFromTables = dataFromTables;
        this.onQuoteClickListener = onQuoteClickListener;
    }

    public interface OnQuoteClickListener {
        void onQuoteClicked(int position, int color, QuotesTable quote, View view);
    }

    @NonNull
    @Override
    public LocalViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LocalViewholder(activity.getLayoutInflater().inflate(R.layout.rv_item, parent, false));
    }

    public void addData(@NonNull ArrayList<QuotesTable> newData) {
        dataFromTables.clear();
        dataFromTables.addAll(newData);
        notifyDataSetChanged();
    }

    @NonNull
    private int createDrawable(LocalViewholder holder) {
        int color1 = TextUtils.getMatColor(activity, "mdcolor_100");
//        int color2 = TextUtils.getMatColor(activity, "mdcolor_500");
        int[] colors = {color1, color1};
        GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.BR_TL, colors);
        gradientDrawable.setGradientRadius(135);
        gradientDrawable.setCornerRadius(8f);
        holder.itemView.setBackground(gradientDrawable);
        return color1;

    }

    @Override
    public void onBindViewHolder(@NonNull final LocalViewholder holder, final int position) {
        final int gradientDrawable = createDrawable(holder);
        final QuotesTable quote = dataFromTables.get(position);
        String genre;

        genre = quote.getCategory();

        holder.tv.setText(quote.getQuotes());
        holder.tv.setTextColor(ContextCompat.getColor(activity,R.color.textColor));
        holder.tv2.setTextColor(ContextCompat.getColor(activity,R.color.textColor));
        holder.tv3.setTextColor(ContextCompat.getColor(activity,R.color.textColor));
        holder.tv2.setText(quote.getAuthor());
        holder.tv3.setText(genre);
        holder.itemView.setOnClickListener(v -> {
            if (onQuoteClickListener != null) {
                onQuoteClickListener.onQuoteClicked(position, gradientDrawable, quote, holder.cardView);
            }
        });
        int visibility = quote.isIsliked() ? View.VISIBLE : View.GONE;
        holder.imageView.setVisibility(visibility);
    }

    @Override
    public int getItemCount() {
        dataFromTables.size();
        return dataFromTables.size();
    }


    class LocalViewholder extends RecyclerView.ViewHolder {
        TextView tv, tv2, tv3;
        ImageView imageView;
        LinearLayout rootView;
        CardView cardView;

        LocalViewholder(@NonNull View itemView) {
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
