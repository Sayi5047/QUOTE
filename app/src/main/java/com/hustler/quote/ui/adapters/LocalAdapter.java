package com.hustler.quote.ui.adapters;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hustler.quote.R;
import com.hustler.quote.ui.apiRequestLauncher.Constants;
import com.hustler.quote.ui.pojo.Quote;
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
public class LocalAdapter extends RecyclerView.Adapter<LocalAdapter.LocalViewholder> {
    Activity activity;
    ArrayList<Quote> dataFromNet;
    OnQuoteClickListener onQuoteClickListener;

    public LocalAdapter(Activity activity, ArrayList<Quote> dataFromNet, OnQuoteClickListener onQuoteClickListener) {
        this.activity = activity;
        this.dataFromNet = dataFromNet;
        this.onQuoteClickListener = onQuoteClickListener;
    }

    public LocalAdapter(Activity activity, ArrayList<Quote> dataFromNet) {
        this.activity = activity;
        this.dataFromNet = dataFromNet;
        this.dataFromNet = new ArrayList<>();
    }

    public LocalAdapter(Activity activity, OnQuoteClickListener onQuoteClickListener) {
        this.activity = activity;
        this.onQuoteClickListener = onQuoteClickListener;
        dataFromNet = new ArrayList<Quote>();
    }

    public interface OnQuoteClickListener {
        void onQuoteClicked(int position, int color, Quote quote, View view);
    }

    @Override
    public LocalViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new LocalViewholder(activity.getLayoutInflater().inflate(R.layout.rv_item, parent, false));
    }

    public void addData(ArrayList<Quote> extraData) {
        dataFromNet = new ArrayList<>();
        for (int i = 0; i < extraData.size(); i++) {
            dataFromNet.add(extraData.get(i));
        }
        notifyItemRangeInserted(0, dataFromNet.size());
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(LocalViewholder holder, final int position) {
        final int color = TextUtils.getMainMatColor("mdcolor_400", activity);
        holder.itemView.setBackgroundColor(color);
        final Quote quote = dataFromNet.get(position);
        quote.setColor(color);
        String genre;

        genre = quote.getQuote_category();

        holder.tv.setText(quote.getQuote_body());
        holder.tv.setTextColor(Color.WHITE);
        holder.tv2.setTextColor(Color.WHITE);
        holder.tv3.setTextColor(Color.WHITE);
        holder.tv2.setText(quote.getQuote_author());
        holder.tv3.setText(genre);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onQuoteClickListener != null) {
                    onQuoteClickListener.onQuoteClicked(position, color, quote, v);
                }
            }
        });
        int visibility = quote.getIsLiked() == 1 ? View.VISIBLE : View.GONE;
        holder.imageView.setVisibility(visibility);
    }

    @Override
    public int getItemCount() {
        if (dataFromNet.size() < 0) {
            return 0;
        } else {
            return dataFromNet.size();
        }
    }


    public class LocalViewholder extends RecyclerView.ViewHolder {
        TextView tv, tv2, tv3;
        ImageView imageView;
        LinearLayout rootView;
        CardView cardView;

        public LocalViewholder(View itemView) {
            super(itemView);
            rootView = itemView.findViewById(R.id.root_layout);
            cardView = itemView.findViewById(R.id.iv_quote_card_root);
            imageView = itemView.findViewById(R.id.fav_iv);

            cardView.setClipToPadding(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                cardView.setClipToOutline(true);
            }
            tv = itemView.findViewById(R.id.main_quote);
            tv2 = itemView.findViewById(R.id.quote_author);
            tv3 = itemView.findViewById(R.id.quote_genre_end);
            TextUtils.setFont(activity, tv, Constants.FONT_CIRCULAR);
            TextUtils.setFont(activity, tv2, Constants.FONT_CIRCULAR);
            TextUtils.setFont(activity, tv3, Constants.FONT_CIRCULAR);

        }
    }
}
