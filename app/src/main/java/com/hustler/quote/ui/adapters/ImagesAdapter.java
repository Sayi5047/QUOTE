package com.hustler.quote.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hustler.quote.R;
import com.hustler.quote.ui.activities.EditorActivity;
import com.hustler.quote.ui.pojo.ImagesFromPixaBay;

import java.util.ArrayList;

/**
 * Created by Sayi on 17-01-2018.
 */

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ImageviewHolder> {

    EditorActivity activity;
    ArrayList<ImagesFromPixaBay> imagesFromPixaBays = new ArrayList<>();
    ImagesOnClickListner listner;

    public ImagesAdapter(EditorActivity activity, ArrayList<ImagesFromPixaBay> imagesFromPixaBays, ImagesOnClickListner listner) {
        this.activity = activity;
        this.imagesFromPixaBays = imagesFromPixaBays;
        this.listner = listner;
    }


    public interface ImagesOnClickListner {
        void onImageClicked(String previreLink, String Biglink);
    }

    @Override
    public ImageviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ImageviewHolder(activity.getLayoutInflater().inflate(R.layout.pixabay_image_layout, parent, false));

    }

    @Override
    public void onBindViewHolder(ImageviewHolder holder, int position) {
        final ImagesFromPixaBay imagesFromPixaBay = imagesFromPixaBays.get(position);
        Glide.with(activity)
                .load(imagesFromPixaBay.getPreviewURL())
                .asBitmap()
                .centerCrop()
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listner != null) {
                    listner.onImageClicked(imagesFromPixaBay.getPreviewURL(), imagesFromPixaBay.getWebformatURL());
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return imagesFromPixaBays.size() <= 0 ? 0 : imagesFromPixaBays.size();
    }

    public class ImageviewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ImageviewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
        }
    }
}
