package com.hustler.quote.ui.adapters;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hustler.quote.R;
import com.hustler.quote.ui.superclasses.App;

import java.util.ArrayList;

/**
 * Created by Sayi on 11-10-2017.
 */


public class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.ContentViewholder> {
    Activity activity;
    ArrayList<String> items;
    int color;
    ArrayList<Integer> resolvedColorsList = new ArrayList<>();
    ArrayList<String> resolvedFontList = new ArrayList<>();
    public onItemClickListener onItemClickListener;
    boolean isFontAsked;

    public ContentAdapter(Activity activity, ArrayList<String> items, ContentAdapter.onItemClickListener listener, boolean isFont) {
        this.activity = activity;
        this.items = items;
        onItemClickListener = listener;
        isFontAsked = isFont;
        if (isFont) {
            getFontIds();
        } else {
            getColorids();

        }
    }

    private void getFontIds() {
        for (int i = 0; i < items.size(); i++) {
            resolvedFontList.add(i, App.getArrayItemFont(activity, "allfonts", i, Color.WHITE));

            Log.d("Files Added -->", String.valueOf(resolvedColorsList.size()));

        }
        Log.d("Files Added -->", String.valueOf(resolvedColorsList.size()));
    }

    /*Methos to get the store the id os the colors into an Arraylist*/
    private void getColorids() {
        for (int i = 0; i < items.size(); i++) {
            resolvedColorsList.add(i, App.getArrayItemColor(activity, "allColors", i, Color.WHITE));

            Log.d("Files Added -->", String.valueOf(resolvedFontList.size()));

        }
        Log.d("Files Added -->", String.valueOf(resolvedFontList.size()));
    }

    public ContentAdapter(Activity activity, ArrayList<String> items) {
        this.activity = activity;
        this.items = items;

    }

    public interface onItemClickListener {
        void onItemColorClick(int color);

        void onItemFontClick(String font);
    }


    public ContentAdapter(Activity activity) {
        this.activity = activity;
    }


    @Override
    public ContentAdapter.ContentViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ContentViewholder(activity.getLayoutInflater().inflate(R.layout.content_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ContentAdapter.ContentViewholder holder, final int position) {
        if (isFontAsked) {
            holder.colorsItem.setVisibility(View.GONE);
            holder.fontItem.setTypeface(App.getZingCursive(activity, resolvedFontList.get(position)));
            Log.d("Resolved color", resolvedFontList.get(position).toString());
            if (onItemClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClickListener.onItemFontClick(resolvedFontList.get(position));
                        onItemClickListener.onItemFontClick(null);

                    }
                });
            }
        } else {
            holder.fontItem.setVisibility(View.GONE);
            holder.colorsItem.setBackgroundColor(resolvedColorsList.get(position));
            Log.d("Resolved color", resolvedColorsList.get(position).toString());
            if (onItemClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClickListener.onItemColorClick(resolvedColorsList.get(position));
                        onItemClickListener.onItemFontClick(null);

                    }
                });
            }
        }


    }

    @Override
    public int getItemCount() {

        if (items.size() <= 0) {
            return 0;
        } else {
            return items.size();
        }
    }

    public class ContentViewholder extends RecyclerView.ViewHolder {
        View colorsItem;
        TextView fontItem;

        public ContentViewholder(View itemView) {
            super(itemView);
            colorsItem = (View) itemView.findViewById(R.id.content_color_item);
            fontItem = (TextView) itemView.findViewById(R.id.content_font_item);

        }
    }
}
