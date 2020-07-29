package com.fjrapp.bigmovies.TvShow;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.fjrapp.bigmovies.R;

public class FavoritAdapterTv extends RecyclerView.Adapter<FavoritAdapterTv.FavoriteViewHolder> {

    private Cursor listFavoriteTv;
    private OnItemClickCallback onItemClickCallback;

    public FavoritAdapterTv(Cursor items) {
        replaceAll(items);
    }


    public void replaceAll(Cursor items) {
        listFavoriteTv = items;
        notifyDataSetChanged();
    }

    public void setOnItemClickCallback(FavoritAdapterTv.OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    public interface OnItemClickCallback {
        void onItemClicked(TvShow tvShow);
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FavoriteViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorit_tvshow, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final FavoriteViewHolder holder, final int position) {
        holder.bind(getItem(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickCallback.onItemClicked(getItem(position));
            }
        });


    }

    private TvShow getItem(int position) {
        if (!listFavoriteTv.moveToPosition(position)) {
            throw new IllegalStateException("Invalid Position!");
        }
        return new TvShow(listFavoriteTv);
    }

    @Override
    public int getItemCount() {
        if (listFavoriteTv == null) return 0;
        return listFavoriteTv.getCount();
    }

    public class FavoriteViewHolder extends RecyclerView.ViewHolder {

        TextView favoriteName, favoriteOverview, favoriteDateRelease;
        ImageView favoriteImage;
        RatingBar favoriteRating;


        public FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);

            favoriteName = itemView.findViewById(R.id.favorit_title);
            favoriteOverview = itemView.findViewById(R.id.favorit_description);
            favoriteDateRelease = itemView.findViewById(R.id.favorit_tv_release);
            favoriteRating = itemView.findViewById(R.id.favorit_ratingBar);
            favoriteImage = itemView.findViewById(R.id.favorit_image_poster);

        }

        public void bind(final TvShow item) {
            favoriteName.setText(item.getTvShowName());
            favoriteOverview.setText(item.getTvShowOverview());
            favoriteDateRelease.setText(item.getTvShowRelease());
            favoriteRating.setRating(Float.parseFloat(item.getRating()) / 2);


            Glide.with(itemView.getContext()).load(item.getTvShowImage())
                    .apply(new RequestOptions().override(350, 350))
                    .into(favoriteImage);
        }
    }
}

