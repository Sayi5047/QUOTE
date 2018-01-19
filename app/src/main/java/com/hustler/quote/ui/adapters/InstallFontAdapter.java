package com.hustler.quote.ui.adapters;

import android.app.Activity;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hustler.quote.R;
import com.hustler.quote.ui.textFeatures.TextFeatures;

import java.io.File;
import java.util.List;

/**
 * Created by anvaya5 on 19/01/2018.
 */

public class InstallFontAdapter extends RecyclerView.Adapter<InstallFontAdapter.FontViewHolder> {
    Activity activity;
    List<String> zipContents;
    String[] fontPAths;
    String fontsUnZipPath;

    public InstallFontAdapter(Activity activity, List<String> zipContents, String fontsUnZipPath) {
        this.activity = activity;
        this.zipContents = zipContents;
        this.fontsUnZipPath = fontsUnZipPath;
        fontPAths = TextFeatures.getDownloadedFonts(activity, new File(fontsUnZipPath));
    }


    @Override
    public FontViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FontViewHolder(activity.getLayoutInflater().inflate(R.layout.install_font_item, parent, false));
    }

    @Override
    public void onBindViewHolder(FontViewHolder holder, int position) {
        holder.textView.setText(zipContents.get(position));
        holder.textView.setTypeface(Typeface.createFromFile(fontPAths[position]));
    }

    @Override
    public int getItemCount() {
        return zipContents.size() <= 0 ? 0 : zipContents.size();
    }

    public class FontViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public FontViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.tv);
        }
    }
}
