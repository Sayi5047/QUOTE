package com.hustler.quote.ui.adapters;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.palette.graphics.Palette;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hustler.quote.ui.apiRequestLauncher.Constants;

import com.hustler.quote.R;

import com.hustler.quote.ui.utils.TextUtils;

import java.util.ArrayList;

/**
 * Created by Sayi on 17-12-2017.
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
public class UserWorkAdapter extends RecyclerView.Adapter<UserWorkAdapter.UserWorkViewHolder> {


    Activity activity;
    String[] paths;
    String[] imageNames;
    OnImageClickListner onImageClickListner;
    @Nullable
    Palette.Swatch swatch;


    @NonNull
    ArrayList<String> pathsList = new ArrayList<>();
    @NonNull
    ArrayList<String> namespathsList = new ArrayList<>();

    public UserWorkAdapter(Activity activity, @NonNull String[] paths, @NonNull String[] imageNames, OnImageClickListner onImageClickListner) {
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
        void onImageClickListneer( int position, String imageName, String imagepath);
    }

    @NonNull
    @Override
    public UserWorkViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new UserWorkViewHolder(activity.getLayoutInflater().inflate(R.layout.user_work_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UserWorkViewHolder holder, final int position) {
        Glide.with(activity).load(paths[position]).centerCrop().diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).into(holder.iv);
//        updateBgColors(holder, position);
        holder.tv.setText(imageNames[position]);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onImageClickListner != null) {
                    onImageClickListner.onImageClickListneer( position, namespathsList.get(position), pathsList.get(position));
                }
            }
        });
    }

    @Nullable
    private Palette.Swatch updateBgColors(@NonNull final UserWorkViewHolder holder, final int position) {
        swatch = new Palette.Swatch(0, 0);

        Palette.from(BitmapFactory.decodeFile(pathsList.get(position))).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(@NonNull Palette palette) {
                swatch = palette.getVibrantSwatch();
                if (swatch == null) {
                    swatch = palette.getDominantSwatch();
                }
                try
                {
                    holder.tv.setBackgroundColor(swatch.getRgb());
                    holder.iv.setBackgroundColor(swatch.getRgb());

                 }catch (Exception e){
                    e.printStackTrace();
                }

            }

        });
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

        public UserWorkViewHolder(@NonNull View itemView) {
            super(itemView);
            iv = itemView.findViewById(R.id.user_work_iv);
            tv = itemView.findViewById(R.id.user_work_name_tv);
            rootCard = itemView.findViewById(R.id.root_cl);
            rootCard.setClipToPadding(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                tv.setClipToOutline(true);
            }
            TextUtils.setFont(activity, tv, Constants.FONT_CIRCULAR);

        }
    }
}
