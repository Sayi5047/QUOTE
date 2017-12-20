package com.hustler.quote.ui.adapters;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hustler.quote.R;
import com.hustler.quote.ui.apiRequestLauncher.Constants;
import com.hustler.quote.ui.utils.TextUtils;

import java.util.ArrayList;

/**
 * Created by Sayi on 17-12-2017.
 */

public class UserWorkAdapter extends RecyclerView.Adapter<UserWorkAdapter.UserWorkViewHolder> {


    Activity activity;
    String[] paths;
    String[] imageNames;
    OnImageClickListner onImageClickListner;
    Palette.Swatch swatch;


    ArrayList<String> pathsList = new ArrayList<>();
    ArrayList<String> namespathsList = new ArrayList<>();
    ArrayList<Palette.Swatch> swatches = new ArrayList<>();

    public UserWorkAdapter(Activity activity, String[] paths, String[] imageNames, OnImageClickListner onImageClickListner) {
        this.activity = activity;
        this.paths = paths;
        this.imageNames = imageNames;
        this.onImageClickListner = onImageClickListner;
        convertToAL(paths, imageNames);
    }

    private void convertToAL(String[] paths, String[] imageNames) {
        int oathscount = paths.length;
        int imagesNamesCount = imageNames.length;

        for (int i = 0; i < oathscount; i++) {
            pathsList.add(paths[i]);
            namespathsList.add(imageNames[i]);
        }

    }


    public UserWorkAdapter() {
    }

    //    String[] paths, String[] imageNames,
    public void updateData(int position) {

        pathsList.remove(position);
        namespathsList.remove(position);

    }

    public interface OnImageClickListner {
        void onImageClickListneer(Palette.Swatch swatch, int position, String imageName, String imagepath);
    }

    @Override
    public UserWorkViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new UserWorkViewHolder(activity.getLayoutInflater().inflate(R.layout.user_work_item, parent, false));
    }

    @Override
    public void onBindViewHolder(UserWorkViewHolder holder, final int position) {
        final Palette.Swatch swarch_from_color;
        Glide.with(activity).load(paths[position]).asBitmap().crossFade().fitCenter().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(holder.iv);
        updateBgColors(holder, position);
        holder.tv.setText(imageNames[position].subSequence(0, 10));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onImageClickListner != null) {
                    onImageClickListner.onImageClickListneer(swatches.get(position), position, namespathsList.get(position), pathsList.get(position));
                }
            }
        });
    }

    private Palette.Swatch updateBgColors(final UserWorkViewHolder holder, final int position) {
        swatch = new Palette.Swatch(0, 0);

        Palette.from(BitmapFactory.decodeFile(pathsList.get(position))).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                swatch = palette.getVibrantSwatch();
                if (swatch == null) {
                    swatch = palette.getDominantSwatch();
                }
                holder.tv.setBackgroundColor(swatch.getRgb());
                holder.iv.setBackgroundColor(swatch.getRgb());
            }

        });
        swatches.add(position, swatch);
        return swatch;
    }

    @Override
    public int getItemCount() {
        if (paths.length <= 0) {
            return 0;
        } else {
            return paths.length;
        }
    }

    public class UserWorkViewHolder extends RecyclerView.ViewHolder {
        ImageView iv;
        TextView tv;
        CardView rootCard;

        public UserWorkViewHolder(View itemView) {
            super(itemView);
            iv = (ImageView) itemView.findViewById(R.id.user_work_iv);
            tv = (TextView) itemView.findViewById(R.id.user_work_name_tv);
            rootCard = (CardView) itemView.findViewById(R.id.root_cl);
            rootCard.setClipToPadding(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                tv.setClipToOutline(true);
            }
            TextUtils.setFont(activity, tv, Constants.FONT_Sans_Bold);

        }
    }
}
