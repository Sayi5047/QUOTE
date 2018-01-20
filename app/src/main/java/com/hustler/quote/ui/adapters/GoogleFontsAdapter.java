package com.hustler.quote.ui.adapters;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v4.provider.FontRequest;
import android.support.v4.provider.FontsContractCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.hustler.quote.R;
import com.hustler.quote.ui.pojo.QueryBuilder;

/**
 * Created by anvaya5 on 15/12/2017.
 */

public class GoogleFontsAdapter extends RecyclerView.Adapter<GoogleFontsAdapter.FontItemViewHolder> {
    Activity activity;
    String[] items;
    onFontClickListner onFontClickListner;
    boolean isDownlodedFonts;
    private static Handler mHandler = null;

    public GoogleFontsAdapter(Boolean isDownlodedFonts, Activity activity, String[] items, GoogleFontsAdapter.onFontClickListner onFontClickListner) {
        this.activity = activity;
        this.items = items;
        this.onFontClickListner = onFontClickListner;
        this.isDownlodedFonts = isDownlodedFonts;
    }

    public interface onFontClickListner {
        void onFontClicked(String fontName_Path, int isDownlodedFonts);
    }

    @Override
    public GoogleFontsAdapter.FontItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GoogleFontsAdapter.FontItemViewHolder(activity.getLayoutInflater().inflate(R.layout.font_item, parent, false));

    }


    @Override
    public void onBindViewHolder(final GoogleFontsAdapter.FontItemViewHolder holder, final int position) {

//        holder.tv.setTypeface(Typeface.createFromFile(items[position]));
        holder.tv.setText(items[position]);


        QueryBuilder queryBuilder = new QueryBuilder(items[position])
                .withWidth(25)
                .withWeight(500)
                .withItalic(0)
                .withBestEffort(true);

        String query = queryBuilder.build();

        FontRequest request = new FontRequest(
                "com.google.android.gms.fonts",
                "com.google.android.gms",
                query,
                R.array.com_google_android_gms_fonts_certs);
        FontsContractCompat.FontRequestCallback callback = new FontsContractCompat.FontRequestCallback() {
            @Override
            public void onTypefaceRequestFailed(int reason) {
                super.onTypefaceRequestFailed(reason);
            }

            @Override
            public void onTypefaceRetrieved(Typeface typeface) {
                holder.tv.setTypeface(typeface);


            }

        };
        FontsContractCompat
                .requestFont(activity, request, callback,
                        getHandlerThreadHandler());


        holder.tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onFontClickListner != null) {


                    onFontClickListner.onFontClicked(items[position], 3);
                }
            }
        });

    }

    private static Handler getHandlerThreadHandler() {
        if (mHandler == null) {
            HandlerThread handlerThread = new HandlerThread("fonts");
            handlerThread.start();
            mHandler = new Handler(handlerThread.getLooper());
        }
        return mHandler;
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
