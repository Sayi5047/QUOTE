package com.hustler.quote.ui.adapters;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hustler.quote.R;
import com.hustler.quote.ui.apiRequestLauncher.Constants;
import com.hustler.quote.ui.pojo.unspalsh.Unsplash_Image;
import com.hustler.quote.ui.utils.TextUtils;
import com.hustler.quote.ui.utils.Toast_Snack_Dialog_Utils;

/**
 * Created by Sayi on 26-01-2018.
 */

public class WallpaperAdapter extends android.support.v7.widget.RecyclerView.Adapter<WallpaperAdapter.WallpaperViewholder> {
    Activity context;

    public WallpaperAdapter(Activity context, Unsplash_Image[] images, OnWallpaperClickListener onimageClickListener) {
        this.context = context;
        this.images = images;
        this.onimageClickListener = onimageClickListener;
    }

    Unsplash_Image[] images;
    OnWallpaperClickListener onimageClickListener;

    @Override
    public WallpaperAdapter.WallpaperViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new WallpaperViewholder(context.getLayoutInflater().inflate(R.layout.wallpaper_rv_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(WallpaperAdapter.WallpaperViewholder holder, final int position) {
        final Unsplash_Image image = images[position];
        Glide.with(context).load(image.getUrls().getSmall()).centerCrop().crossFade().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(holder.wallpaper);
        Glide.with(context).load(image.getUser().getProfile_image().getSmall()).centerCrop().crossFade().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(holder.photographer);
        holder.photoGrapher_name.setText(image.getUser().getFirst_name());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onimageClickListener != null) {
                    onimageClickListener.onWallpaperClicked(position, image);
                } else {
                    Toast_Snack_Dialog_Utils.show_ShortToast(context, context.getString(R.string.Listener));
                }
            }
        });
    }

    public interface OnWallpaperClickListener {
        void onWallpaperClicked(int position, Unsplash_Image wallpaper);

    }

    @Override
    public int getItemCount() {
        return images.length <= 0 ? 0 : images.length;
    }

    public class WallpaperViewholder extends RecyclerView.ViewHolder {
        ImageView wallpaper, photographer;
        TextView photoGrapher_name;

        public WallpaperViewholder(View itemView) {
            super(itemView);
            wallpaper = (ImageView) itemView.findViewById(R.id.wallpaper_preview);
            photographer = (ImageView) itemView.findViewById(R.id.photographer_image);
            photoGrapher_name = (TextView) itemView.findViewById(R.id.photographer_name);
            photoGrapher_name.setShadowLayer(24,5,5, ContextCompat.getColor(context,android.R.color.black));
            TextUtils.setFont(context, photoGrapher_name, Constants.FONT_Google_sans_regular);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                photographer.setClipToOutline(true);
            }
        }
    }
}
