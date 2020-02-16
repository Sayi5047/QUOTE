package com.hustler.quote.ui.adapters;

import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hustler.quote.ui.apiRequestLauncher.Constants;

import com.hustler.quote.R;

import com.hustler.quote.ui.utils.TextUtils;

/**
 * Created by Sayi on 22-04-2018.
 */

public class MainFeatureADapter extends RecyclerView.Adapter<MainFeatureADapter.FeatureViewholder> {
    Activity activity;
    String[] features;

    public MainFeatureADapter(Activity activity, FeatureClick_Listener listener) {
        this.activity = activity;
        this.listener = listener;
        getallFeatures();
    }

    private void getallFeatures() {
        features = activity.getResources().getStringArray(R.array.allfeatures);
    }

    FeatureClick_Listener listener;

    public interface FeatureClick_Listener {
        void onFeatureClciked(int position, String name);
    }


    @NonNull
    @Override
    public FeatureViewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new FeatureViewholder(activity.getLayoutInflater().inflate(R.layout.new_feature_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final FeatureViewholder holder, int i) {
        createDrawable(holder);
        holder.textView.setText(features[holder.getAdapterPosition()]);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onFeatureClciked(holder.getAdapterPosition(), features[holder.getAdapterPosition()]);
                }
            }
        });
    }

    @NonNull
    private GradientDrawable createDrawable(FeatureViewholder holder) {
        int color1 = TextUtils.getMatColor(activity, "mdcolor_500");
        int color2 = TextUtils.getMatColor(activity, "mdcolor_500");
        int[] colors = {color1, color2};
        GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.BR_TL, colors);
        gradientDrawable.setGradientRadius(135);
        gradientDrawable.setCornerRadius(20f);
        holder.itemView.setBackground(gradientDrawable);
        return gradientDrawable;
//        holder.imageView.layout(0,0,100,100);
//
//        holder.imageView.setDrawingCacheEnabled(true);
//        holder.imageView.buildDrawingCache();
//        Bitmap bitmap=holder.imageView.getDrawingCache();
//        Bitmap finalbitmap =ImageProcessingUtils.create_blur(bitmap,5.0f,activity);
//        holder.imageView.setImageBitmap(finalbitmap);
    }

    @Override
    public int getItemCount() {
        return 6;
    }

    public class FeatureViewholder extends RecyclerView.ViewHolder {
        TextView textView;
        CardView cardView;

        public FeatureViewholder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text);
            TextUtils.setFont(activity,textView, Constants.FONT_CIRCULAR);
            cardView = itemView.findViewById(R.id.card);
            cardView.setClipToPadding(true);
            cardView.setClipToOutline(true);
        }
    }
}
