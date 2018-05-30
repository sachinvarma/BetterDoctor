package com.sachinvarma.betterdoctor.helper;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.sachinvarma.betterdoctor.utils.AppConstants;

/**
 * Endless scroll used for recyclerview scrolling. It helps in pagination.
 */

public abstract class EndlessScrollListener extends RecyclerView.OnScrollListener {

    public static final String TAG = EndlessScrollListener.class.getSimpleName();

    @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        // init
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        RecyclerView.Adapter adapter = recyclerView.getAdapter();

        if (layoutManager.getChildCount() > 0) {
      /*// Calculate
      int indexOfLastItemViewVisible = layoutManager.getChildCount() - 1;
      View lastItemViewVisible = layoutManager.getChildAt(indexOfLastItemViewVisible);
      int adapterPosition = layoutManager.getPosition(lastItemViewVisible);
      boolean isLastItemVisible = (adapterPosition == adapter.getItemCount() - 1);
      Log.d(TAG, "index last visible" + indexOfLastItemViewVisible);
      // check*/
            int totalItemCount = adapter.getItemCount();
            int visibleItemCount = recyclerView.getChildCount();
            int pastVisibleItems =
                    ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
            // Checking if we are at point to load more data and current page <= total pages
            // And if we have atleast items >= PRODUCT_SEARCH_PAGE_SIZE. If not, server doesn't have enough
            // data for pagination
            if ((visibleItemCount + pastVisibleItems) >= totalItemCount && totalItemCount >= AppConstants.PAGINATION_COUNT) {
        /*if (weakReferenceListener.get() != null) {
          weakReferenceListener.get().onPageScrolled(twoWayView);
        }*/
                onLastItemVisible();
            }
        }
        //if (isLastItemVisible) onLastItemVisible(); // callback
    }

    /**
     * Here you should load more items because user is seeing the last item of the list.
     * Advice: you should add a bollean value to the class
     * so that the method will be triggered only once
     * and not every time the user touch the screen ;)
     **/
    public abstract void onLastItemVisible();
}