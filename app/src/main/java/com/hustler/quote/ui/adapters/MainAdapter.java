package com.hustler.quote.ui.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.hustler.quote.R;
import com.hustler.quote.ui.pojo.QuotesFromFC;
import com.hustler.quote.ui.superclasses.App;

import java.util.List;

/**
 * Created by Sayi on 07-10-2017.
 */

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainViewHolder> {

    Activity activity;
    List<QuotesFromFC> dataFromNet;

    public MainAdapter(Activity activity, List<QuotesFromFC> dataFromNet) {
        this.activity = activity;
        this.dataFromNet = dataFromNet;
    }


    public MainAdapter(Activity activity) {
        this.activity = activity;
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new MainViewHolder(activity.getLayoutInflater().inflate(R.layout.rv_item, parent, false));
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, int position) {
        QuotesFromFC quote = dataFromNet.get(position);
        holder.itemView.setAnimation(AnimationUtils.loadAnimation(activity,R.anim.slideup));
        holder.tv.setText(quote.getBody());


    }

    @Override
    public int getItemCount() {
        if (dataFromNet.size() < 0) {
            return 0;
        } else {
            return dataFromNet.size();
        }
    }


    public void updateQuotes(List<QuotesFromFC> quotesFromFCList) {
        this.dataFromNet = quotesFromFCList;
        notifyDataSetChanged();
    }

    public class MainViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv;

        public MainViewHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.text1_check);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            App.showToast(activity,"clicked");
        }
    }
}
