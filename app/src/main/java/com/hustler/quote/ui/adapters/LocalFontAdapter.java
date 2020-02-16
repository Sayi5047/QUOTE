package com.hustler.quote.ui.adapters;

import android.app.Activity;
import android.graphics.Typeface;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.hustler.quote.R;

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
public class LocalFontAdapter extends RecyclerView.Adapter<LocalFontAdapter.FontItemViewHolder> {
    Activity activity;
    private String[] fontsArray;
    private onFontClickListener onFontClickListener;

    public LocalFontAdapter(Activity activity, String[] fontsArray, onFontClickListener onFontClickListener) {
        this.activity = activity;
        this.fontsArray = fontsArray;
        this.onFontClickListener = onFontClickListener;
    }

    public interface onFontClickListener {
        void onFontClicked(String fontName_Path, int isDownloadedFonts);
    }

    @NonNull
    @Override
    public FontItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FontItemViewHolder(activity.getLayoutInflater().inflate(R.layout.font_item, parent, false));

    }


    @Override
    public void onBindViewHolder(@NonNull FontItemViewHolder holder, int position) {
        final String font = fontsArray[position];
        holder.tv.setTypeface(Typeface.createFromAsset(holder.tv.getContext().getAssets(), font));

        holder.tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onFontClickListener != null) {
                    onFontClickListener.onFontClicked(font, 1);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        if (fontsArray.length <= 0) {
            return 0;
        } else {
            return fontsArray.length;
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
