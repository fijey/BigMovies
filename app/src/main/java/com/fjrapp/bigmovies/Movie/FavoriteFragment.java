package com.fjrapp.bigmovies.Movie;

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

import com.fjrapp.bigmovies.Movie.adapter.FavoritAdapter;
import com.fjrapp.bigmovies.R;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.fjrapp.bigmovies.db.DatabaseContract.MovieColumns.CONTENT_URI;


public class FavoriteFragment extends Fragment {
    @Nullable
    @BindView(R.id.favorit_title)
    TextView favoriteName;

    @Nullable
    @BindView(R.id.favorit_description)
    TextView favoriteOverview;

    @Nullable
    @BindView(R.id.favorit_tv_release)
    TextView favoriteDateRelease;

    @Nullable
    @BindView(R.id.favorit_ratingBar)
    RatingBar favoriteRating;

    @Nullable
    @BindView(R.id.favorit_image_poster)
    ImageView favoriteImage;

    @Nullable
    @BindView(R.id.swloading)
    SwipeRefreshLayout swLayout;

    @Nullable
    @BindView(R.id.list_favorit)
    RecyclerView rvFavorite;


    @BindView(R.id.no_list)
    ImageView noList;

    private Context context;
    private Cursor list;
    private FavoritAdapter adapter;

//    public static int REQUEST_CODE = 121;
//
//    public static int ID =121;
//    public static String CODE ="code";


    public FavoriteFragment() {
    }

    public static FavoriteFragment newInstance(FragmentManager fragmentManager) {
        FavoriteFragment fragment = new FavoriteFragment();
        fragment.setFragmentManager(fragmentManager);
        return fragment;
    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        FragmentManager fragmentManager1 = fragmentManager;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);
        context = view.getContext();
        ButterKnife.bind(this,view);

        adapter = new FavoritAdapter(list);
        rvFavorite.setLayoutManager(new LinearLayoutManager(context));
        rvFavorite.setHasFixedSize(true);
        rvFavorite.setAdapter(adapter);
//        checkView();
        new loadMoviesAsync().execute();

        adapter.setOnItemClickCallback(new FavoritAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(Movie movie) {
                showSelectedFavorit(movie);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        new loadMoviesAsync().execute();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        FavoriteFragment.this.onActivityResult(requestCode, resultCode, data);
//        if ((requestCode == REQUEST_CODE) && (resultCode == Activity.RESULT_OK)) {
//            Fragment frag;
//            frag=  getActivity().getSupportFragmentManager().findFragmentByTag(CODE);
//            FragmentTransaction ft = getFragmentManager().beginTransaction();
//            ft.detach(frag).attach(frag).commit();
//        }
//    }


    private class loadMoviesAsync extends AsyncTask<Void, Void, Cursor> {
        @Override
        protected Cursor doInBackground(Void... voids) {
            return context.getContentResolver().query(CONTENT_URI, null, null, null, null);
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            super.onPostExecute(cursor);

            list = cursor;
            adapter.replaceAll(list);
        }
    }

    private void showSelectedFavorit(Movie movie) {
        Intent intentfavorit = new Intent(getActivity(), DetailActivity.class);
        intentfavorit.putExtra(DetailActivity.EXTRA_NAME, movie);
        getActivity().startActivity(intentfavorit);
    }

//    public void checkView() {
//
//        if (adapter.getItemCount()== 0) {
//            rvFavorite.setVisibility(View.INVISIBLE);
//            noList.setVisibility(View.VISIBLE);
//            adapter.notifyDataSetChanged();
//        } else {
//            rvFavorite.setVisibility(View.VISIBLE);
//            noList.setVisibility(View.INVISIBLE);
//            adapter.notifyDataSetChanged();
//        }
//
//    }
}

