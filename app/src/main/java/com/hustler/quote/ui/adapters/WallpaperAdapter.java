package com.hustler.quote.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Sayi on 26-01-2018.
 */

public class WallpaperAdapter extends android.support.v7.widget.RecyclerView.Adapter<WallpaperAdapter.WallpaperViewholder> {
    Context context;
    String[] images;
    OnWallpaperClickListener onimageClickListener;

    @Override
    public WallpaperAdapter.WallpaperViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(WallpaperAdapter.WallpaperViewholder holder, int position) {

    }

    interface OnWallpaperClickListener {
        void onWallpaperClicked(int position, String wallpaper);

    }

    @Override
    public int getItemCount() {
        return images.length <= 0 ? 0 : images.length;
    }

    public class WallpaperViewholder extends RecyclerView.ViewHolder {
        public WallpaperViewholder(View itemView) {
            super(itemView);
        }
    }
}
