package com.hustler.quotzy.ui.listeners;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by Sayi on 04-02-2018.
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
public abstract class InfiniteScrolListener extends RecyclerView.OnScrollListener {
    public InfiniteScrolListener(GridLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    GridLayoutManager layoutManager;

    // The minimum number of items to have below your current scroll position
    // before loading more.
    private int visibleThreshold = 5;
    // The current offset index of data you have loaded
    private int currentPage = 0;
    // The total number of items in the dataset after the last load
    private int previousTotalItemCount = 0;
    // True if we are still waiting for the last set of data to load.
    private boolean loading = true;
    // Sets the starting page index
    private int startingPageIndex = 0;

    public InfiniteScrolListener(int visibleThreshold, int currentPage, int startingPageIndex) {
        this.visibleThreshold = visibleThreshold;
        this.currentPage = currentPage;
        this.startingPageIndex = startingPageIndex;
    }

    public InfiniteScrolListener() {

    }

    public InfiniteScrolListener(int visibleThreshold) {

        this.visibleThreshold = visibleThreshold;
    }


    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        // If the total item count is zero and the previous isn't, assume the
        // list is invalidated and should be reset back to initial state

        int visibleItemCount = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();
        if (totalItemCount < previousTotalItemCount) {
            this.currentPage = this.startingPageIndex;
            this.previousTotalItemCount = totalItemCount;
            if (totalItemCount == 0) {
                this.loading = true;
            }
        }
        // If it's still loading, we check to see if the dataset count has
        // changed, if so we conclude it has finished loading and update the current page
        // number and total item count.
        if (loading && (totalItemCount > previousTotalItemCount)) {
            loading = false;
            previousTotalItemCount = totalItemCount;
            currentPage++;
        }

        // If it isn't currently loading, we check to see if we have breached
        // the visibleThreshold and need to reload more data.
        // If we do need to reload some more data, we execute onLoadMore to fetch the data.
        if ((!loading && (layoutManager.findFirstVisibleItemPosition() + visibleItemCount + visibleThreshold) >= totalItemCount) ) {
            loading = true;
            onLoadMore(currentPage + 1, totalItemCount);
        }
    }

    public abstract void onLoadMore(int i, int totalItemCount);

    public void resetState() {
        loading = false;
        visibleThreshold = 5;
        // The current offset index of data you have loaded
        currentPage = 0;
        // The total number of items in the dataset after the last load
        previousTotalItemCount = 0;
        // True if we are still waiting for the last set of data to load.
        loading = false;
        // Sets the starting page index
        startingPageIndex = 0;

        currentPage=0;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
    }
}
