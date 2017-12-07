package com.hustler.quote.ui.adapters;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.hustler.quote.R;
import com.hustler.quote.ui.apiRequestLauncher.Constants;
import com.hustler.quote.ui.superclasses.App;
import com.hustler.quote.ui.utils.TextUtils;
import com.hustler.quote.ui.utils.ToastSnackDialogUtils;

import java.util.ArrayList;

/**
 * Created by anvaya5 on 07/12/2017.
 */

public class Features_adapter extends RecyclerView.Adapter<Features_adapter.FeaturesViewholder> {
    Activity activity;
    OnFeature_ItemClickListner listner;
    String[] itemsfromActivity;String arrayName;
    ArrayList<String> itemstobe_used = new ArrayList<>();
     String current_item = null;


    public Features_adapter(Activity activity, String arrayName,String[] itemsfromActivity, OnFeature_ItemClickListner listner) {
        this.activity = activity;
        this.itemsfromActivity = itemsfromActivity;
        this.listner = listner;
        this.arrayName = arrayName;
        prepare_text_features();
    }

    private void prepare_text_features() {
        for(int i=0;i<itemsfromActivity.length;i++){
            itemstobe_used.add(i, TextUtils.getArrayItem_of_String(activity,arrayName,i));
        }
    }

    @Override
    public FeaturesViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new  FeaturesViewholder(activity.getLayoutInflater().inflate((R.layout.features_item),parent,false));
    }

    @Override
    public void onBindViewHolder(final FeaturesViewholder holder, int position) {
        holder.feature_item.setText(itemstobe_used.get(position));
        current_item = itemstobe_used.get(position);
        holder.itemView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:{
                        AnimatorSet set=(AnimatorSet) AnimatorInflater.loadAnimator(activity,R.animator.squeeze_in);
                        set.setTarget(v);
                        set.start();
                        if(listner!=null){
                            listner.onItemClick(current_item);
                            ToastSnackDialogUtils.show_ShortToast(activity,current_item);
                        }
                    }
                    break;
                    case MotionEvent.ACTION_UP:{
                        AnimatorSet set=(AnimatorSet) AnimatorInflater.loadAnimator(activity,R.animator.squeeze_out);
                        set.setTarget(v);
                        set.start();
                        if(listner!=null){
                            listner.onItemClick(current_item);
                            ToastSnackDialogUtils.show_ShortToast(activity,current_item);
                        }
                    }
                }
                return true;

            }
        });

    }

    @Override
    public int getItemCount() {
        if(itemstobe_used.size()<=0){
            return 0;
        }else {
            return itemstobe_used.size();
        }
    }

    public class FeaturesViewholder extends RecyclerView.ViewHolder {
        TextView feature_item;
        public FeaturesViewholder(View itemView) {
            super(itemView);
            feature_item = (TextView) itemView.findViewById(R.id.feature_item);
            TextUtils.setFont(activity,feature_item, Constants.FONT_Sans_Bold);
        }
    }


    public interface OnFeature_ItemClickListner {
        void onItemClick(String clickedItem);
    }
}
