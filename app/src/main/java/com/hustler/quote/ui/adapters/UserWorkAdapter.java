package com.hustler.quote.ui.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hustler.quote.R;
import com.hustler.quote.ui.apiRequestLauncher.Constants;
import com.hustler.quote.ui.utils.TextUtils;

/**
 * Created by Sayi on 17-12-2017.
 */

public class UserWorkAdapter extends RecyclerView.Adapter<UserWorkAdapter.UserWorkViewHolder> {
    Activity activity;
    String[] paths;
    String[] imageNames;
    OnImageClickListner onImageClickListner;

    public UserWorkAdapter(Activity activity, String[] paths, String[] imageNames,OnImageClickListner onImageClickListner) {
        this.activity = activity;
        this.paths = paths;
        this.imageNames = imageNames;
        this.onImageClickListner=onImageClickListner;
    }


    public UserWorkAdapter() {
    }


    public interface OnImageClickListner{
        void onImageClickListneer(String imageName,String imagepath);
    }
    @Override
    public UserWorkViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new UserWorkViewHolder(activity.getLayoutInflater().inflate(R.layout.user_work_item, parent, false));
    }

    @Override
    public void onBindViewHolder(UserWorkViewHolder holder, final int position) {
        Glide.with(activity).load(paths[position]).asBitmap().crossFade().fitCenter().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(holder.iv);
        holder.tv.setText(imageNames[position]);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onImageClickListner!=null){
                    onImageClickListner.onImageClickListneer(imageNames[position],paths[position]);
                }
            }
        });
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

        public UserWorkViewHolder(View itemView) {
            super(itemView);
            iv = (ImageView) itemView.findViewById(R.id.user_work_iv);
            tv = (TextView) itemView.findViewById(R.id.user_work_name_tv);
            TextUtils.setFont(activity, tv, Constants.FONT_Sans_Bold);

        }
    }
}
