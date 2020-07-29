package com.fjrapp.bigmovies.TvShow;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

import static com.fjrapp.bigmovies.db.DatabaseContract.TvShowColumns.BACKGROUND_TV;
import static com.fjrapp.bigmovies.db.DatabaseContract.TvShowColumns.DATE_RELEASE_TV;
import static com.fjrapp.bigmovies.db.DatabaseContract.TvShowColumns.IMAGE_TV;
import static com.fjrapp.bigmovies.db.DatabaseContract.TvShowColumns.OVERVIEW_TV;
import static com.fjrapp.bigmovies.db.DatabaseContract.TvShowColumns.RATING_TV;
import static com.fjrapp.bigmovies.db.DatabaseContract.TvShowColumns.TITLE_TV;
import static com.fjrapp.bigmovies.db.DatabaseContract.TvShowColumns._ID;
import static com.fjrapp.bigmovies.db.DatabaseContract.getColumnInt;
import static com.fjrapp.bigmovies.db.DatabaseContract.getColumnString;

public class TvShow implements Parcelable {

    private int id;
    private String tvShowName;
    private String tvShowOverview;
    private String tvShowImage;
    private String tvShowRelease;
    private float tvShowRating;
    private String rating;
    private String tvShowImageBackground;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTvShowName() {
        return tvShowName;
    }

    public void setTvShowName(String tvShowName) {
        this.tvShowName = tvShowName;
    }

    public String getTvShowOverview() {
        return tvShowOverview;
    }

    public void setTvShowOverview(String tvShowOverview) {
        this.tvShowOverview = tvShowOverview;
    }

    public String getTvShowImage() {
        return tvShowImage;
    }

    public void setTvShowImage(String tvShowImage) {
        this.tvShowImage = tvShowImage;
    }

    public String getTvShowRelease() {
        return tvShowRelease;
    }

    public void setTvShowRelease(String tvShowRelease) {
        this.tvShowRelease = tvShowRelease;
    }

    public float getTvShowRating() {
        return tvShowRating;
    }

    public void setTvShowRating(float tvShowRating) {
        this.tvShowRating = tvShowRating;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getTvShowImageBackground() {
        return tvShowImageBackground;
    }

    public void setTvShowImageBackground(String tvShowImageBackground) {
        this.tvShowImageBackground = tvShowImageBackground;
    }

    TvShow(JSONObject object) {
        try {

            String URL = "https://image.tmdb.org/t/p/w185";
            String URL_BACKGROUND ="https://image.tmdb.org/t/p/w500";

            this.id = object.getInt("id");
            this.tvShowName = object.getString("original_name");
            this.tvShowOverview = object.getString("overview");
            this.tvShowImage = (URL + object.getString("poster_path"));
            this.tvShowRelease = (object.getString("first_air_date"));
            this.tvShowRating = (float) object.getDouble("vote_average");
            this.rating = object.getString("vote_average");
            this.tvShowImageBackground = (URL_BACKGROUND + object.get("backdrop_path"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public TvShow() {
    }

    public TvShow(int id, String tvShowName, String tvShowOverview, String tvShowImage, String tvShowRelease, String Rating, String tvShowImageBackground) {
        this.id = id;
        this.tvShowName = tvShowName;
        this.tvShowOverview = tvShowOverview;
        this.tvShowImage = tvShowImage;
        this.tvShowRelease = tvShowRelease;
        this.rating = Rating;
        this.tvShowImageBackground = tvShowImageBackground;
    }


    public TvShow(Cursor cursor) {
        this.id = getColumnInt(cursor, _ID);
        this.tvShowName = getColumnString(cursor, TITLE_TV);
        this.tvShowOverview = getColumnString(cursor, OVERVIEW_TV);
        this.tvShowImage = getColumnString(cursor, IMAGE_TV);
        this.tvShowRelease = getColumnString(cursor, DATE_RELEASE_TV);
        this.rating = getColumnString(cursor, RATING_TV);
        this.tvShowImageBackground = getColumnString(cursor, BACKGROUND_TV);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.tvShowName);
        dest.writeString(this.tvShowOverview);
        dest.writeString(this.tvShowImage);
        dest.writeString(this.tvShowRelease);
        dest.writeFloat(this.tvShowRating);
        dest.writeString(this.rating);
        dest.writeString(this.tvShowImageBackground);
    }

    protected TvShow(Parcel in) {
        this.id = in.readInt();
        this.tvShowName = in.readString();
        this.tvShowOverview = in.readString();
        this.tvShowImage = in.readString();
        this.tvShowRelease = in.readString();
        this.tvShowRating = in.readFloat();
        this.rating = in.readString();
        this.tvShowImageBackground = in.readString();
    }

    public static final Creator<TvShow> CREATOR = new Creator<TvShow>() {
        @Override
        public TvShow createFromParcel(Parcel source) {
            return new TvShow(source);
        }

        @Override
        public TvShow[] newArray(int size) {
            return new TvShow[size];
        }
    };
}