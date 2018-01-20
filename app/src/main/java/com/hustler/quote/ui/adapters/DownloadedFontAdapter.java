package com.hustler.quote.ui.adapters;

import android.app.Activity;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.hustler.quote.R;

/**
 * Created by anvaya5 on 15/12/2017.
 */

public class DownloadedFontAdapter extends RecyclerView.Adapter<DownloadedFontAdapter.FontItemViewHolder> {
    Activity activity;
    String[] items;
    onFontClickListner onFontClickListner;
    boolean isDownlodedFonts;

    public DownloadedFontAdapter(Boolean isDownlodedFonts, Activity activity, String[] items, onFontClickListner onFontClickListner) {
        this.activity = activity;
        this.items = items;
        this.onFontClickListner = onFontClickListner;
        this.isDownlodedFonts = isDownlodedFonts;
    }

    public interface onFontClickListner {
        void onFontClicked(String fontName_Path, int isDownlodedFonts);
    }

    @Override
    public DownloadedFontAdapter.FontItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DownloadedFontAdapter.FontItemViewHolder(activity.getLayoutInflater().inflate(R.layout.font_item, parent, false));

    }


    @Override
    public void onBindViewHolder(DownloadedFontAdapter.FontItemViewHolder holder, final int position) {
        holder.tv.setTypeface(Typeface.createFromFile(items[position]));
        holder.tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onFontClickListner != null) {
                    onFontClickListner.onFontClicked(items[position], 2);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        if (items.length <= 0) {
            return 0;
        } else {
            return items.length;
        }
    }

    public class FontItemViewHolder extends RecyclerView.ViewHolder {
        Button tv;

        public FontItemViewHolder(View itemView) {
            super(itemView);
            tv = (Button) itemView.findViewById(R.id.tv);
        }
    }
}
