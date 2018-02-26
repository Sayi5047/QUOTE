package com.hustler.quote.ui.adapters;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

import java.util.ArrayList;

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
public class WallpaperAdapter extends android.support.v7.widget.RecyclerView.Adapter<WallpaperAdapter.WallpaperViewholder> {
    Activity context;
    private boolean isLoadingAdded = false;
    static ArrayList<Unsplash_Image> m_AL_Images = new ArrayList<>();
    Unsplash_Image[] images;
    OnWallpaperClickListener onimageClickListener;

    public WallpaperAdapter(Activity context, Unsplash_Image[] images, OnWallpaperClickListener onimageClickListener, int from_which_activity) {
        this.context = context;
        this.images = images;
        this.onimageClickListener = onimageClickListener;
        this.from_which_activity = from_which_activity;
        convertToArraYList(images);
    }

    int from_which_activity;

    public WallpaperAdapter(Activity context, Unsplash_Image[] images, OnWallpaperClickListener onimageClickListener) {
        this.context = context;
        this.images = images;
        this.onimageClickListener = onimageClickListener;
        from_which_activity=1;
        convertToArraYList(images);
    }

    public void addItems(Unsplash_Image[] images) {
        for (int i = 0; i < images.length; i++) {
            m_AL_Images.add(images[i]);
        }
        notifyDataSetChanged();
        notifyItemRangeInserted(this.images.length, images.length);
    }

    private void convertToArraYList(Unsplash_Image[] images) {
        m_AL_Images.clear();
        notifyDataSetChanged();
        for (int i = 0; i < images.length; i++) {

            m_AL_Images.add(i, images[i]);


        }
        Log.d("ADAPTER SIZE", images.length + "");
    }

    public interface OnWallpaperClickListener {
        void onWallpaperClicked(int position, ArrayList<Unsplash_Image> m_AL_Images);

    }

    @Override
    public WallpaperAdapter.WallpaperViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(from_which_activity==3){
            return new WallpaperViewholder(context.getLayoutInflater().inflate(R.layout.wallpaper_rv_item_layout_search, parent, false));

        }else {
            return new WallpaperViewholder(context.getLayoutInflater().inflate(R.layout.wallpaper_rv_item_layout, parent, false));

        }
    }

    @Override
    public void onBindViewHolder(WallpaperAdapter.WallpaperViewholder holder, final int position) {
        final Unsplash_Image image = m_AL_Images.get(position);
        Glide.with(context).load(image.getUrls().getSmall()).centerCrop().crossFade().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(holder.wallpaper);
        Glide.with(context).load(image.getUser().getProfile_image().getSmall()).centerCrop().crossFade().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(holder.photographer);
        holder.photoGrapher_name.setText(image.getUser().getFirst_name());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onimageClickListener != null) {
                    onimageClickListener.onWallpaperClicked(position, m_AL_Images);
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
//
//    //    @Override
////    public int getItemViewType(int position) {
//////        return (position == images.length - 1 && isLoadingAdded) ? LOADING : ITEM;
////    }
//    public Unsplash_Image getItem(int position) {
//        return m_AL_Images.get(position);
//    }
//
//    public void add(Unsplash_Image image) {
//        m_AL_Images.add(image);
//        notifyItemInserted(m_AL_Images.size() - 1);
//    }
//
//    public void add_All(ArrayList<Unsplash_Image> images) {
//        for (Unsplash_Image image : images) {
//            m_AL_Images.add(image);
//        }
//    }
//
//    public void remove(Unsplash_Image city) {
//        int position = m_AL_Images.indexOf(city);
//        if (position > -1) {
//            m_AL_Images.remove(position);
//            notifyItemRemoved(position);
//        }
//    }
//
//    public void clear() {
//        isLoadingAdded = false;
//        while (getItemCount() > 0) {
//            remove(getItem(0));
//        }
//    }
//
//    public boolean isEmpty() {
//        return getItemCount() == 0;
//    }
//
//    public void addLoadingFooter() {
//        isLoadingAdded = true;
//        add(new Unsplash_Image());
//    }
//
//    public void removeLoadingFooter() {
//        isLoadingAdded = false;
//
//        int position = m_AL_Images.size() - 1;
//        Unsplash_Image item = getItem(position);
//
//        if (item != null) {
//            m_AL_Images.remove(position);
//            notifyItemRemoved(position);
//        }
//    }


    public class WallpaperViewholder extends RecyclerView.ViewHolder {
        ImageView wallpaper, photographer;
        TextView photoGrapher_name;

        public WallpaperViewholder(View itemView) {
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
