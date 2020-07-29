package com.fjrapp.bigmovies.Movie.adapter;

import android.content.Context;
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

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.CardViewViewHolder> {
    Context context;
    private ArrayList<Movie> mData;
    private OnItemClickCallback onItemClickCallback;


    public MovieAdapter(Context context) {
        this.context = context;
    }

    public MovieAdapter() {

    }

    public void setListMovie(ArrayList<Movie> items) {
        this.mData = items;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public CardViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_movie, parent, false);
        return new CardViewViewHolder(view);
    }

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    @Override
    public void onBindViewHolder(@NonNull final CardViewViewHolder holder, final int position) {
        holder.tvMovieName.setText(mData.get(position).getMovieName());
        holder.tvMovieDesc.setText(mData.get(position).getMovieOverview());
        holder.tvMovieRelease.setText(mData.get(position).getMovieRelease());
        holder.tvMovieRating.setRating((float) ((mData.get(position).getMovieRating())/2));

        if (holder.itemView.getContext() != null) {
            Glide.with(holder.itemView.getContext()).load(mData.get(position).getMovieImage())
                    .apply(new RequestOptions().override(350, 350))
                    .into(holder.imgPoster);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickCallback.onItemClicked(mData.get(holder.getAdapterPosition()));
            }
        });

    }

    @Override
    public int getItemCount() {
        if (mData != null) {
            return mData.size();
        } else {
            return 0;
        }
    }

    public interface OnItemClickCallback {
        void onItemClicked(Movie movie);
    }

    public class CardViewViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPoster;
        TextView tvMovieName, tvMovieDesc, tvMovieRelease;
        RatingBar tvMovieRating;

        public CardViewViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMovieName = itemView.findViewById(R.id.movie_title);
            tvMovieDesc = itemView.findViewById(R.id.movie_description);
            imgPoster = itemView.findViewById(R.id.movie_image_poster);
            tvMovieRelease = itemView.findViewById(R.id.tv_release);
            tvMovieRating = itemView.findViewById(R.id.ratingBar);
        }
    }
}
