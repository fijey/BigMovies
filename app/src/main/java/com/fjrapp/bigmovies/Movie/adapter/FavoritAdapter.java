package com.fjrapp.bigmovies.Movie.adapter;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.fjrapp.bigmovies.Movie.Movie;
import com.fjrapp.bigmovies.R;

public class FavoritAdapter extends RecyclerView.Adapter<FavoritAdapter.FavoriteViewHolder> {

    private Cursor listFavorite;
    private OnItemClickCallback onItemClickCallback;

    public FavoritAdapter(Cursor items) {
        replaceAll(items);
    }


    public void replaceAll(Cursor items) {
        listFavorite = items;
        notifyDataSetChanged();
    }

    public void setOnItemClickCallback(FavoritAdapter.OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    public interface OnItemClickCallback {
        void onItemClicked(Movie movie);
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FavoriteViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorit, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final FavoriteViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.bind(getItem(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickCallback.onItemClicked(getItem(position));
            }
        });


    }

    private Movie getItem(int position) {
        if (!listFavorite.moveToPosition(position)) {
            throw new IllegalStateException("Invalid Position!");
        }
        return new Movie(listFavorite);
    }

    @Override
    public int getItemCount() {
        if (listFavorite == null) return 0;
        return listFavorite.getCount();
    }

    class FavoriteViewHolder extends RecyclerView.ViewHolder {

        TextView favoriteName, favoriteOverview, favoriteDateRelease;
        ImageView favoriteImage;
        RatingBar favoriteRating;


        FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);

            favoriteName = itemView.findViewById(R.id.favorit_title);
            favoriteOverview = itemView.findViewById(R.id.favorit_description);
            favoriteDateRelease = itemView.findViewById(R.id.favorit_tv_release);
            favoriteRating = itemView.findViewById(R.id.favorit_ratingBar);
            favoriteImage = itemView.findViewById(R.id.favorit_image_poster);

        }

        void bind(final Movie item) {
            favoriteName.setText(item.getMovieName());
            favoriteOverview.setText(item.getMovieOverview());
            favoriteDateRelease.setText(item.getMovieRelease());
            favoriteRating.setRating((float) item.getMovieRating() /2);


            Glide.with(itemView.getContext()).load(item.getMovieImage())
                    .apply(new RequestOptions().override(350, 350))
                    .into(favoriteImage);
        }
    }
}
