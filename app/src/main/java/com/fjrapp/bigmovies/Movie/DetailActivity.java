package com.fjrapp.bigmovies.Movie;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.fjrapp.bigmovies.R;
import com.fjrapp.bigmovies.db.MovieHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.BlurTransformation;

import static com.fjrapp.bigmovies.db.DatabaseContract.MovieColumns.BACKGROUND;
import static com.fjrapp.bigmovies.db.DatabaseContract.MovieColumns.CONTENT_URI;
import static com.fjrapp.bigmovies.db.DatabaseContract.MovieColumns.DATE_RELEASE;
import static com.fjrapp.bigmovies.db.DatabaseContract.MovieColumns.IMAGE;
import static com.fjrapp.bigmovies.db.DatabaseContract.MovieColumns.OVERVIEW;
import static com.fjrapp.bigmovies.db.DatabaseContract.MovieColumns.RATING_DOUBLE;
import static com.fjrapp.bigmovies.db.DatabaseContract.MovieColumns.TITLE;

public class DetailActivity extends AppCompatActivity {
    public static final String EXTRA_NAME = "name";


    @BindView(R.id.movie_name_detail)
    TextView movieName;


    @BindView(R.id.movie_desc_detail)
    TextView movieDesc;


    @BindView(R.id.tv_date)
    TextView movieDate;


    @BindView(R.id.tvrating)
    TextView movieRating;


    @BindView(R.id.image_poster_detail)
    ImageView moviePoster;


    @BindView(R.id.imagesampul)
    ImageView moviesampul;


    @BindView(R.id.scrollview)
    ScrollView scroll;

    @BindView(R.id.ratingBar)
    RatingBar ratingbar;


    @BindView(R.id.fab_favorite)
    FloatingActionButton fabFavorite;

    boolean itemFavorite = false;
    MovieHelper movieHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);


        Movie movie = getIntent().getParcelableExtra(EXTRA_NAME);
        String name = movie.getMovieName();
        movieName.setText(name);
        String desc = movie.getMovieOverview();
        movieDesc.setText(desc);
        String date = movie.getMovieRelease();
        movieDate.setText(date);
        double rating = movie.getMovieRating();
        ratingbar.setRating((float) rating /2);
        String tvRat = (movie.getMovieRating()+ "/10");
        movieRating.setText(tvRat);

        Glide.with(this).load(movie.getMovieImage())
                .apply(new RequestOptions().override(350, 350))
                .into(moviePoster);
        Glide.with(this).load(movie.getMovieImageBackground())
                .apply(RequestOptions.bitmapTransform(new BlurTransformation(2, 3)))
                .into(moviesampul);

        loadData();

        fabFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemFavorite) {
                    removeFavorite();
                    Snackbar.make(v, "Dihapus dari Favorit", Snackbar.LENGTH_SHORT).show();
                } else {
                    saveFavorite();
                    Snackbar.make(v, "Ditambahkan ke Favorit", Snackbar.LENGTH_SHORT).show();
                }
                itemFavorite = !itemFavorite;
                setFavorite();
            }
        });

    }

    private void loadData() {
        Movie data = getIntent().getParcelableExtra("name");
        movieHelper = new MovieHelper(this);
        movieHelper.open();
        Cursor cursor = getContentResolver().query(Uri.parse(CONTENT_URI + "/" + data.getId()), null,
                null,
                null,
                null);

        if (cursor != null) {
            if (cursor.moveToFirst()) itemFavorite = true;
            cursor.close();
        }
        setFavorite();
    }

    private void setFavorite() {
        if (itemFavorite) {
            fabFavorite.setImageResource(R.drawable.ic_favorite_true);
//
        } else {
            fabFavorite.setImageResource(R.drawable.ic_favorite_false);
        }
    }

    private void saveFavorite() {
        Movie data = getIntent().getParcelableExtra("name");
        ContentValues values = new ContentValues();
        values.put(BaseColumns._ID, data.getId());
        values.put(TITLE, data.getMovieName());
        values.put(OVERVIEW, data.getMovieOverview());
        values.put(IMAGE, data.getMovieImage());
        values.put(DATE_RELEASE, data.getMovieRelease());
        values.put(BACKGROUND, data.getMovieImageBackground());
        values.put(RATING_DOUBLE, data.getMovieRating());
        getContentResolver().insert(CONTENT_URI, values);

    }

    private void removeFavorite() {
        Movie data = getIntent().getParcelableExtra("name");
        getContentResolver().delete(Uri.parse(CONTENT_URI + "/" + data.getId()), null,
                null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (movieHelper != null) {
            movieHelper.close();
        }
    }
}
