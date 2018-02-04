package com.hustler.quote.ui.listeners;

import android.support.v7.widget.RecyclerView;

/**
 * Created by Sayi on 04-02-2018.
 */

public class ParrallaxScrollListener extends RecyclerView.OnScrollListener {

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        for(int i=0;i<recyclerView.getChildCount();i++){

        }
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        this.onScrollStateChanged(recyclerView,newState);
    }


}
