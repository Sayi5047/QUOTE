package com.hustler.quote.ui.adapters;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hustler.quote.ui.apiRequestLauncher.Constants;

import com.hustler.quote.R;

import com.hustler.quote.ui.pojo.unspalsh.Unsplash_Image;
import com.hustler.quote.ui.utils.TextUtils;
import com.hustler.quote.ui.utils.Toast_Snack_Dialog_Utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Created by Sayi on 26-01-2018.
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
public class FavWallpaperAdapter extends RecyclerView.Adapter<FavWallpaperAdapter.WallpaperViewholder> {
    Activity context;
    private boolean isLoadingAdded = false;
    static List<Unsplash_Image> m_AL_Images = new ArrayList<>();
    Unsplash_Image[] images;
    OnWallpaperClickListener onimageClickListener;

    public FavWallpaperAdapter(Activity context, List<Unsplash_Image> m_AL_Images, OnWallpaperClickListener onimageClickListener) {
        this.context = context;
        FavWallpaperAdapter.m_AL_Images = m_AL_Images;
        this.onimageClickListener = onimageClickListener;
        Set<Unsplash_Image> hs = new HashSet<>();
        hs.addAll(FavWallpaperAdapter.m_AL_Images);
        FavWallpaperAdapter.m_AL_Images.clear();
        FavWallpaperAdapter.m_AL_Images.addAll(hs);
    }

    public static void addItems(Unsplash_Image[] images) {
        for (int i = 0; i < images.length; i++) {
            m_AL_Images.add(images[i]);
        }


    }

    private void convertToArraYList(Unsplash_Image[] images) {
        for (int i = 0; i < images.length; i++) {
            m_AL_Images.add(i, images[i]);
        }
        Log.d("ADAPTER SIZE", images.length + "");
    }

    public interface OnWallpaperClickListener {
        void onWallpaperClicked(int position, Unsplash_Image wallpaper);

    }

    @NonNull
    @Override
    public FavWallpaperAdapter.WallpaperViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new WallpaperViewholder(context.getLayoutInflater().inflate(R.layout.wallpaper_fav_rv_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FavWallpaperAdapter.WallpaperViewholder holder, final int position) {
        final Unsplash_Image image = m_AL_Images.get(position);
        Glide.with(context).load(image.getUrls().getRegular()).centerCrop().diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).into(holder.wallpaper);
        Glide.with(context).load(image.getUser().getProfile_image().getLarge()).centerCrop().diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).into(holder.photographer);
        holder.photoGrapher_name.setText(image.getUser().getUsername());
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

        holder.photoGrapher_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://unsplash.com/" + image.getUser().getUsername() + "?utm_source=Quotzy&utm_medium=referral\""));
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return m_AL_Images.size() <= 0 ? 0 : m_AL_Images.size();
    }


    public class WallpaperViewholder extends RecyclerView.ViewHolder {
        ImageView wallpaper, photographer;
        TextView photoGrapher_name;

        public WallpaperViewholder(@NonNull View itemView) {
            super(itemView);
            wallpaper = itemView.findViewById(R.id.wallpaper_preview);
            photographer = itemView.findViewById(R.id.photographer_image);
            photoGrapher_name = itemView.findViewById(R.id.photographer_name);
            photoGrapher_name.setShadowLayer(24, 5, 5, ContextCompat.getColor(context, android.R.color.black));
            TextUtils.setFont(context, photoGrapher_name, Constants.FONT_CIRCULAR);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                photographer.setClipToOutline(true);
            }
        }
    }
}
