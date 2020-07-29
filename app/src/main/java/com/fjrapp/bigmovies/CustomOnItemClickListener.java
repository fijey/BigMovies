package com.fjrapp.bigmovies;

import android.view.View;

public class CustomOnItemClickListener implements View.OnClickListener {

    private int position;
    private OnItemFavoritClickCallback onItemFavoritClickCallback;

    public CustomOnItemClickListener(int position, OnItemFavoritClickCallback onItemFavoritClickCallback) {
        this.position = position;
        this.onItemFavoritClickCallback = onItemFavoritClickCallback;
    }

    @Override
    public void onClick(View v) {
        onItemFavoritClickCallback.onItemClicked(v, position);

    }

    public interface OnItemFavoritClickCallback {
        void onItemClicked(View view, int position);
    }
}
