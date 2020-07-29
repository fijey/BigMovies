package com.fjrapp.bigmovies.TvShow;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.fjrapp.bigmovies.R;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.fjrapp.bigmovies.db.DatabaseContract.TvShowColumns.CONTENT_URI2;


public class FragmentFavoriteTvShow extends Fragment {
    @Nullable
    @BindView(R.id.favorit_title)
    TextView favoriteName;

    @Nullable
    @BindView(R.id.favorit_description)
    TextView favoriteOverview;

    @Nullable
    @BindView(R.id.tvshow_release)
    TextView favoriteDateRelease;

    @Nullable
    @BindView(R.id.ratingBar)
    RatingBar favoriteRating;

    @Nullable
    @BindView(R.id.favorit_image_poster)
    ImageView favoriteImage;

    @Nullable
    @BindView(R.id.swloading)
    SwipeRefreshLayout swLayout;

    @Nullable
    @BindView(R.id.list_favorit_tvshow)
    RecyclerView rvFavorite;

    @Nullable
    @BindView(R.id.no_list_tv)
    ImageView noList;

    private Context context;
    private Cursor list;
    private FavoritAdapterTv adapterTv;
    private FragmentManager fragmentManager;


    public FragmentFavoriteTvShow() {
    }

    public static FragmentFavoriteTvShow newInstance(FragmentManager fragmentManager) {
        FragmentFavoriteTvShow fragment = new FragmentFavoriteTvShow();
        fragment.setFragmentManager(fragmentManager);
        return fragment;
    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite_tv_show, container, false);
        context = view.getContext();

        ButterKnife.bind(this,view);


        adapterTv = new FavoritAdapterTv(list);
        rvFavorite.setLayoutManager(new LinearLayoutManager(context));
        rvFavorite.setHasFixedSize(true);
        rvFavorite.setAdapter(adapterTv);

        new loadTvAsync().execute();


        adapterTv.setOnItemClickCallback(new FavoritAdapterTv.OnItemClickCallback() {
            @Override
            public void onItemClicked(TvShow tvShow) {
                showSelectedFavorite(tvShow);
            }
        });


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        new loadTvAsync().execute();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    private class loadTvAsync extends AsyncTask<Void, Void, Cursor> {
        @Override
        protected Cursor doInBackground(Void... voids) {
            return context.getContentResolver().query(CONTENT_URI2, null, null, null, null);
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            super.onPostExecute(cursor);

            list = cursor;
            adapterTv.replaceAll(list);
        }
    }

    public void showSelectedFavorite(TvShow tvShow) {
        Intent intentFavorite = new Intent(getActivity(), DetailActivityTvShow.class);
        intentFavorite.putExtra(DetailActivityTvShow.EXTRA_NAME, tvShow);
        getActivity().startActivity(intentFavorite);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

//    public void checkView() {
//
//        if (adapterTv.getItemCount() == 0) {
//            rvFavorite.setVisibility(View.INVISIBLE);
//            noList.setVisibility(View.VISIBLE);
//            adapterTv.notifyDataSetChanged();
//        } else {
//            rvFavorite.setVisibility(View.VISIBLE);
//            noList.setVisibility(View.INVISIBLE);
//            adapterTv.notifyDataSetChanged();
//        }
//
//    }
}