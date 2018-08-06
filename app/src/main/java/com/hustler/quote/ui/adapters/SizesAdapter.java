package com.hustler.quote.ui.adapters;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hustler.quote.R;
import com.hustler.quote.ui.activities.EditorActivity;

import java.util.ArrayList;

/**
 * Created by anvaya5 on 18/01/2018.
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
public class SizesAdapter extends RecyclerView.Adapter<SizesAdapter.SizeViewHolder> {
    EditorActivity editorActivity;
    ArrayList<Integer> imagesList;
    OnSizeClickListner onSizeClickListner;

    public SizesAdapter(EditorActivity editorActivity, ArrayList<Integer> imagesList, OnSizeClickListner onSizeClickListner) {
        this.editorActivity = editorActivity;
        this.imagesList = imagesList;
        this.onSizeClickListner = onSizeClickListner;
    }


    @Override
    public SizeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SizeViewHolder(editorActivity.getLayoutInflater().inflate(R.layout.size_image, parent,false));

    }

    @Override
    public void onBindViewHolder(SizeViewHolder holder, final int position) {
        holder.imageView.setImageDrawable(ContextCompat.getDrawable(editorActivity,imagesList.get(position)));
//        Glide.with(editorActivity).load(ContextCompat.getDrawable(editorActivity,imagesList.get(position))).asBitmap().into(holder.imageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onSizeClickListner==null){
                }else {
                    onSizeClickListner.onSizeClicked(position);

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return imagesList.size() <= 0 ? 0 : imagesList.size();
    }

    public interface OnSizeClickListner {
        void onSizeClicked(int position);
    }

    public class SizeViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public SizeViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
        }
    }
}
