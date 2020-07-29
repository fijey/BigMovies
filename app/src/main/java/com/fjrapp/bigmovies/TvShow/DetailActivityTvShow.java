package com.fjrapp.bigmovies.TvShow;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.fjrapp.bigmovies.R;
import com.fjrapp.bigmovies.db.TvShowHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.BlurTransformation;

import static com.fjrapp.bigmovies.db.DatabaseContract.TvShowColumns.BACKGROUND_TV;
import static com.fjrapp.bigmovies.db.DatabaseContract.TvShowColumns.CONTENT_URI2;
import static com.fjrapp.bigmovies.db.DatabaseContract.TvShowColumns.DATE_RELEASE_TV;
import static com.fjrapp.bigmovies.db.DatabaseContract.TvShowColumns.IMAGE_TV;
import static com.fjrapp.bigmovies.db.DatabaseContract.TvShowColumns.OVERVIEW_TV;
import static com.fjrapp.bigmovies.db.DatabaseContract.TvShowColumns.RATING_DOUBLE_TV;
import static com.fjrapp.bigmovies.db.DatabaseContract.TvShowColumns.RATING_TV;
import static com.fjrapp.bigmovies.db.DatabaseContract.TvShowColumns.TITLE_TV;


public class DetailActivityTvShow extends AppCompatActivity {
    public static final String EXTRA_NAME = "name";

    @BindView(R.id.movie_name_detail)
    TextView tvShowName;

    @BindView(R.id.movie_desc_detail)
    TextView tvShowDesc;

    @BindView(R.id.tvshow_release_detail)
    TextView tvShowRelease;

    @BindView(R.id.image_poster_detail)
    ImageView tvShowImage;

    @BindView(R.id.imagesampul)
    ImageView tvShowImageSampul;

    @BindView(R.id.ratingBar)
    RatingBar tvShoRating;

    @BindView(R.id.tv_tvrating)
    TextView ratingtext;


    @BindView(R.id.fab_favorite_tvshow)
    FloatingActionButton fabFavorite;

    boolean itemFavoriteTv = false;
    TvShowHelper tvShowHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tv_show);

        ButterKnife.bind(this);

        TvShow tvShow = getIntent().getParcelableExtra(EXTRA_NAME);
        String tvName = tvShow.getTvShowName();
        tvShowName.setText(tvName);
        String tvDesc = tvShow.getTvShowOverview();
        tvShowDesc.setText(tvDesc);
        String tvRelease = tvShow.getTvShowRelease();
        tvShowRelease.setText(tvRelease);
        String ratingBar = tvShow.getRating();
        tvShoRating.setRating(Float.parseFloat(ratingBar) / 2);
        String tvRating = (tvShow.getRating()+"/10");
        ratingtext.setText(tvRating);

        Glide.with(this).load(tvShow.getTvShowImage())
                .apply(new RequestOptions().override(350, 350))
                .into(tvShowImage);
        Glide.with(this).load(tvShow.getTvShowImageBackground())
                .apply(RequestOptions.bitmapTransform(new BlurTransformation(2, 3)))
                .into(tvShowImageSampul);

        loadData();

        fabFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (itemFavoriteTv) {
                    removeFavorite();
                    Snackbar.make(v, "Dihapus dari Favorit", Snackbar.LENGTH_SHORT).show();
                } else {
                    saveFavorite();
                    Snackbar.make(v, "Ditambahkan ke Favorit", Snackbar.LENGTH_SHORT).show();
                }
                itemFavoriteTv = !itemFavoriteTv;
                setFavorite();

            }
        });
    }

    private void loadData() {
        TvShow data = getIntent().getParcelableExtra("name");
        tvShowHelper = new TvShowHelper(this);
        tvShowHelper.open();
        Cursor cursor = getContentResolver().query(Uri.parse(CONTENT_URI2 + "/" + data.getId()), null,
                null,
                null,
                null);

        if (cursor != null) {
            if (cursor.moveToFirst()) itemFavoriteTv = true;
            cursor.close();
        }
        setFavorite();
    }

    private void setFavorite() {
        if (itemFavoriteTv) {
            fabFavorite.setImageResource(R.drawable.ic_favorite_true);
        } else {
            fabFavorite.setImageResource(R.drawable.ic_favorite_false);
        }
    }

    private void saveFavorite() {
        TvShow data = getIntent().getParcelableExtra("name");
        ContentValues values = new ContentValues();
        values.put(BaseColumns._ID, data.getId());
        values.put(TITLE_TV, data.getTvShowName());
        values.put(OVERVIEW_TV, data.getTvShowOverview());
        values.put(IMAGE_TV, data.getTvShowImage());
        values.put(DATE_RELEASE_TV, data.getTvShowRelease());
        values.put(RATING_TV, data.getRating());
        values.put(BACKGROUND_TV, data.getTvShowImageBackground());
        values.put(RATING_DOUBLE_TV, data.getTvShowRating());
        getContentResolver().insert(CONTENT_URI2, values);
    }

    private void removeFavorite() {
        TvShow data = getIntent().getParcelableExtra("name");
        getContentResolver().delete(Uri.parse(CONTENT_URI2 + "/" + data.getId()), null,
                null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (tvShowHelper != null) {
            tvShowHelper.close();
        }
    }
}
