package com.hustler.quote.ui.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.hustler.quote.R;
import com.hustler.quote.ui.utils.TextUtils;

/**
 * Created by anvaya5 on 15/12/2017.
 */

public class SymbolFontAdapter extends RecyclerView.Adapter<SymbolFontAdapter.FontItemViewHolder> {
    Activity activity;
    String[] items;
    onFontClickListner onFontClickListner;
    boolean isDownlodedFonts;

    public SymbolFontAdapter(Boolean isDownlodedFonts, Activity activity, String[] items, onFontClickListner onFontClickListner) {
        this.activity = activity;
        this.items = items;
        this.onFontClickListner = onFontClickListner;
        this.isDownlodedFonts = isDownlodedFonts;
    }

    public interface onFontClickListner {
        void onFontClicked(String fontName_Path, boolean isDownlodedFonts);
    }

    @Override
    public SymbolFontAdapter.FontItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SymbolFontAdapter.FontItemViewHolder(activity.getLayoutInflater().inflate(R.layout.font_item, parent, false));

    }


    @Override
    public void onBindViewHolder(SymbolFontAdapter.FontItemViewHolder holder, final int position) {
        TextUtils.setFont(activity, holder.tv, items[position]);
        holder.tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onFontClickListner != null) {
                    onFontClickListner.onFontClicked(items[position], false);
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
