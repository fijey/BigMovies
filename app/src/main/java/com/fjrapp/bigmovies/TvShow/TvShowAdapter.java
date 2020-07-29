package com.fjrapp.bigmovies.TvShow;

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
import com.fjrapp.bigmovies.R;

import java.util.ArrayList;

public class TvShowAdapter extends RecyclerView.Adapter<TvShowAdapter.CardViewViewHolder> {
    private Context context;


    private ArrayList<TvShow> mData = new ArrayList<>();
    private OnItemClickCallback onItemClickCallback;

    public TvShowAdapter(ArrayList<TvShow> list) {
        this.mData = list;
    }


    public TvShowAdapter(Context context) {
    }

    public TvShowAdapter() {

    }

    public void setData(ArrayList<TvShow> items) {
        mData.clear();
        mData.addAll(items);
        notifyDataSetChanged();
    }

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    @NonNull
    @Override
    public CardViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_tv_show, parent, false);
        return new CardViewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CardViewViewHolder holder, final int position) {

        holder.tvShowName.setText(mData.get(position).getTvShowName());
        holder.tvShowDesc.setText(mData.get(position).getTvShowOverview());
        holder.tvShowRelease.setText(mData.get(position).getTvShowRelease());
        holder.tvShowRating.setRating(mData.get(position).getTvShowRating() / 2);

        if (holder.itemView.getContext() != null) {

            Glide.with(holder.itemView.getContext()).load(mData.get(position).getTvShowImage())
                    .apply(new RequestOptions().override(350, 350))
                    .into(holder.tvShowImage);

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
        return mData.size();
    }

    public interface OnItemClickCallback {
        void onItemClicked(TvShow tvShow);
    }

    public class CardViewViewHolder extends RecyclerView.ViewHolder {
        ImageView tvShowImage;
        TextView tvShowName, tvShowDesc, tvShowRelease;
        RatingBar tvShowRating;

        public CardViewViewHolder(@NonNull View itemView) {
            super(itemView);
            tvShowImage = itemView.findViewById(R.id.tvshow_image_poster);
            tvShowName = itemView.findViewById(R.id.tvshow_title);
            tvShowDesc = itemView.findViewById(R.id.tvshow_description);
            tvShowRating = itemView.findViewById(R.id.tvshow_ratingBar);
            tvShowRelease = itemView.findViewById(R.id.tvshow_release);

        }
    }
}
