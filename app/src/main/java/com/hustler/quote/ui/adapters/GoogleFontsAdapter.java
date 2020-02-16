package com.hustler.quote.ui.adapters;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.HandlerThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.provider.FontRequest;
import androidx.core.provider.FontsContractCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.Objects;

import com.hustler.quote.R;
import com.hustler.quote.ui.pojo.QueryBuilder;

/**
 * Created by anvaya5 on 15/12/2017.
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
public class GoogleFontsAdapter extends RecyclerView.Adapter<GoogleFontsAdapter.FontItemViewHolder> {
    Activity activity;
    private String[] items;
    private onFontClickListner onFontClickListner;
    @Nullable
    private static Handler mHandler = null;

    public GoogleFontsAdapter(Activity activity, String[] items, GoogleFontsAdapter.onFontClickListner onFontClickListner) {
        this.activity = activity;
        this.items = items;
        this.onFontClickListner = onFontClickListner;
    }

    public interface onFontClickListner {
        void onFontClicked(String fontName_Path, int isDownlodedFonts);
    }

    @NonNull
    @Override
    public GoogleFontsAdapter.FontItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GoogleFontsAdapter.FontItemViewHolder(activity.getLayoutInflater().inflate(R.layout.font_item, parent, false));

    }


    @Override
    public void onBindViewHolder(@NonNull final GoogleFontsAdapter.FontItemViewHolder holder, int position) {
        final String item = items[position];

        holder.tv.setText(item);


        QueryBuilder queryBuilder = new QueryBuilder(item)
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
                        Objects.requireNonNull(getHandlerThreadHandler()));


        holder.tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onFontClickListner != null) {
                    onFontClickListner.onFontClicked(item, 3);
                }
            }
        });

    }

    @Nullable
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

    class FontItemViewHolder extends RecyclerView.ViewHolder {
        Button tv;

        FontItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.tv);
        }
    }
}
