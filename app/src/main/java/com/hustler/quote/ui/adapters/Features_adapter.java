package com.hustler.quote.ui.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.hustler.quote.R;
import com.hustler.quote.ui.apiRequestLauncher.Constants;
import com.hustler.quote.ui.utils.TextUtils;

import java.util.ArrayList;

/**
 * Created by anvaya5 on 07/12/2017.
 */

public class Features_adapter extends RecyclerView.Adapter<Features_adapter.FeaturesViewholder> {
    Activity activity;
    OnFeature_ItemClickListner listner;
    int itemsfromActivity_Count;
    String arrayName;
    ArrayList<String> itemstobe_used = new ArrayList<>();
    String current_item = null;
    boolean isText;


    public Features_adapter(Activity activity, String arrayName, int itemsfromActivity, OnFeature_ItemClickListner listner, boolean isText) {
        this.activity = activity;
        this.itemsfromActivity_Count = itemsfromActivity;
        this.listner = listner;
        this.arrayName = arrayName;
        this.isText = isText;

        prepare_text_features();
    }

    private void prepare_text_features() {
        for (int i = 0; i < itemsfromActivity_Count; i++) {
            itemstobe_used.add(i, TextUtils.getArrayItem_of_String(activity, arrayName, i));
        }
    }

    @Override
    public FeaturesViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (isText) {
            return new FeaturesViewholder(activity.getLayoutInflater().inflate((R.layout.features_item), parent, false));

        } else {
            return new FeaturesViewholder(activity.getLayoutInflater().inflate((R.layout.feature_item_2), parent, false));

        }
    }

    @Override
    public void onBindViewHolder(final FeaturesViewholder holder, final int position) {
        holder.feature_item.setText(itemstobe_used.get(position));
        current_item = itemstobe_used.get(position);
//        Toast_Snack_Dialog_Utils.show_ShortToast(activity,"TOAST 1"+position+"");
        holder.feature_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listner.onItemClick(itemstobe_used.get(position));


            }
        });
//        button animation for
//        holder.itemView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN: {
//                        AnimatorSet set = (AnimatorSet) AnimatorInflater.loadAnimator(activity, R.animator.squeeze_in);
//                        set.setTarget(v);
//                        set.start();
//
//                    }
//                    break;
//                    case MotionEvent.ACTION_UP: {
//                        AnimatorSet set = (AnimatorSet) AnimatorInflater.loadAnimator(activity, R.animator.squeeze_out);
//                        set.setTarget(v);
//                        set.start();
//
//                    }
//                    case MotionEvent.ACTION_MOVE:
//                    {
//
//                    }
//
//                }
//                return true;
//
//            }
//        });

    }

    @Override
    public int getItemCount() {
        if (itemstobe_used.size() <= 0) {
            return 0;
        } else {
            return itemstobe_used.size();
        }
    }

    public interface OnFeature_ItemClickListner {
        void onItemClick(String clickedItem);
    }

    public class FeaturesViewholder extends RecyclerView.ViewHolder {
        Button feature_item;

        public FeaturesViewholder(View itemView) {
            super(itemView);
            feature_item = (Button) itemView.findViewById(R.id.feature_item);
            TextUtils.setFont(activity, feature_item, Constants.FONT_Google_sans_regular);
        }
    }
}
