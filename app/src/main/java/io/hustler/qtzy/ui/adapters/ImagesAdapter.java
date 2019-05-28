package io.hustler.qtzy.ui.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import io.hustler.qtzy.R;
import io.hustler.qtzy.ui.activities.EditorActivity;
import io.hustler.qtzy.ui.apiRequestLauncher.response.DataItem;

/**
 * Created by Sayi on 17-01-2018.
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
public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ImageviewHolder> {

    EditorActivity activity;
    List<DataItem> giphyStickerItems = new ArrayList<>();
    ImagesOnClickListner listner;

    public ImagesAdapter(EditorActivity activity, List<DataItem> giphyStickerItems, ImagesOnClickListner listner) {
        this.activity = activity;
        this.giphyStickerItems = giphyStickerItems;
        this.listner = listner;
    }


    public interface ImagesOnClickListner {
        void onImageClicked(String stillImage, String gifImage, int frameCount);
    }

    @NonNull
    @Override
    public ImageviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ImageviewHolder(activity.getLayoutInflater().inflate(R.layout.pixabay_image_layout, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull ImageviewHolder holder, int position) {
        final DataItem giphySticker = giphyStickerItems.get(position);
        Glide.with(activity)
                .load(giphySticker.getImages().getOriginalStill().getUrl())
                .asBitmap()
                .centerCrop()
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listner != null) {
                    listner.onImageClicked(giphySticker.getImages().getOriginalStill().getUrl(),
                            giphySticker.getImages().getOriginal().getUrl(), Integer.parseInt(giphySticker.getImages().getOriginal().getFrames()));
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return giphyStickerItems.size() <= 0 ? 0 : giphyStickerItems.size();
    }

    public class ImageviewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ImageviewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
        }
    }
}
