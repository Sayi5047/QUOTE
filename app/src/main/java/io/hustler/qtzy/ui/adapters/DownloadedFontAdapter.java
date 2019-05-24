package io.hustler.qtzy.ui.adapters;

import android.app.Activity;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import io.hustler.qtzy.R;

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
public class DownloadedFontAdapter extends RecyclerView.Adapter<DownloadedFontAdapter.FontItemViewHolder> {
    Activity activity;
    String[] items;
    onFontClickListner onFontClickListner;
    boolean isDownlodedFonts;

    public DownloadedFontAdapter(Boolean isDownlodedFonts, Activity activity, String[] items, onFontClickListner onFontClickListner) {
        this.activity = activity;
        this.items = items;
        this.onFontClickListner = onFontClickListner;
        this.isDownlodedFonts = isDownlodedFonts;
    }

    public interface onFontClickListner {
        void onFontClicked(String fontName_Path, int isDownlodedFonts);
    }

    @NonNull
    @Override
    public DownloadedFontAdapter.FontItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DownloadedFontAdapter.FontItemViewHolder(activity.getLayoutInflater().inflate(R.layout.font_item, parent, false));

    }


    @Override
    public void onBindViewHolder(@NonNull DownloadedFontAdapter.FontItemViewHolder holder, final int position) {
        holder.tv.setTypeface(Typeface.createFromFile(items[position]));
        holder.tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onFontClickListner != null) {
                    onFontClickListner.onFontClicked(items[position], 2);
                }
            }
        });

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

        public FontItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.tv);
        }
    }
}
