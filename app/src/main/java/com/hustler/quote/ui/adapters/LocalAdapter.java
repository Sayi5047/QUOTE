package com.hustler.quote.ui.adapters;

import android.app.Activity;
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
import com.hustler.quote.ui.superclasses.App;
import com.hustler.quote.ui.utils.TextUtils;

import java.util.ArrayList;

/**
 * Created by anvaya5 on 27/12/2017.
 */

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
    }

    public interface OnQuoteClickListener {
        void onQuoteClicked(int position, int color, Quote quote, View view);
    }

    @Override
    public LocalViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new LocalViewholder(activity.getLayoutInflater().inflate(R.layout.rv_item, parent, false));
    }

    @Override
    public void onBindViewHolder(LocalViewholder holder, final int position) {
        final int color = TextUtils.getMainMatColor("mdcolor_100", activity);
        holder.itemView.setBackgroundColor(color);
        final Quote quote = dataFromNet.get(position);
        quote.setColor(color);
        String genre;

        genre = quote.getQuote_category();

        holder.tv.setText(quote.getQuote_body());
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
            rootView = (LinearLayout) itemView.findViewById(R.id.root_ll);
            cardView = (CardView) itemView.findViewById(R.id.iv_quote_card_root);
            imageView = (ImageView) itemView.findViewById(R.id.fav_iv);

            cardView.setClipToPadding(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                cardView.setClipToOutline(true);
            }
            tv = (TextView) itemView.findViewById(R.id.main_quote);
            tv2 = (TextView) itemView.findViewById(R.id.quote_author);
            tv3 = (TextView) itemView.findViewById(R.id.quote_genre_end);
            tv.setTypeface(App.getZingCursive(activity, Constants.FONT_ZINGSANS));
            tv2.setTypeface(App.getZingCursive(activity, Constants.FONT_ZINGCURSIVE));
            tv3.setTypeface(App.getZingCursive(activity, Constants.FONT_NEVIS));

        }
    }
}
