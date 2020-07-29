package com.fjrapp.bigmovies.Movie;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.fjrapp.bigmovies.db.DatabaseContract;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.provider.BaseColumns._ID;
import static com.fjrapp.bigmovies.Tool.URL;
import static com.fjrapp.bigmovies.db.DatabaseContract.getColumnInt;
import static com.fjrapp.bigmovies.db.DatabaseContract.getColumnString;

public class Movie implements Parcelable {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("title")
    @Expose
    private String movieName;

    @SerializedName("overview")
    @Expose
    private String movieOverview;

    @SerializedName("poster_path")
    @Expose
    private String movieImage;

    @SerializedName("release_date")
    @Expose
    private String movieRelease;

    @SerializedName("vote_average")
    @Expose
    private Double movieRating;

    @SerializedName("backdrop_path")
    @Expose
    private String movieImageBackground;

    public Movie(int anInt, String string) {

    }

    public Movie(int id, String movieName, String movieOverview, String movieImage, String movieRelease, Double movieRating, String movieImageBackground) {
        this.id = id;
        this.movieName = movieName;
        this.movieOverview = movieOverview;
        this.movieImage = movieImage;
        this.movieRelease = movieRelease;
        this.movieImageBackground = movieImageBackground;
        this.movieRating = movieRating;
    }

    public Movie(Cursor cursor) {
        this.id = getColumnInt(cursor, _ID);
        this.movieName = getColumnString(cursor, DatabaseContract.MovieColumns.TITLE);
        this.movieOverview = getColumnString(cursor, DatabaseContract.MovieColumns.OVERVIEW);
        this.movieImage = getColumnString(cursor, DatabaseContract.MovieColumns.IMAGE);
        this.movieRelease = getColumnString(cursor, DatabaseContract.MovieColumns.DATE_RELEASE);
        this.movieImageBackground = getColumnString(cursor, DatabaseContract.MovieColumns.BACKGROUND);
        this.movieRating = Double.valueOf(getColumnString(cursor, DatabaseContract.MovieColumns.RATING_DOUBLE));
    }

    public Movie(JSONObject object) {
        try {

            this.id = object.getInt("id");
            this.movieName = object.getString("title");
            this.movieOverview = object.getString("overview");
            this.movieImage = (URL + object.getString("poster_path"));
            this.movieRelease = (object.getString("release_date"));
            this.movieRating = object.getDouble("vote_average");
            this.movieImageBackground = (URL + object.getString("backdrop_path"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public String getMovieImageBackground() {
        return movieImageBackground;
    }

    public void setMovieImageBackground(String movieImageBackground) {
        this.movieImageBackground = movieImageBackground;
    }

    public double getMovieRating() {
        return movieRating;
    }

    public void setMovieRating(Double movieRating) {
        this.movieRating = movieRating;
    }

    public String getMovieRelease() {
        return movieRelease;
    }

    public void setMovieRelease(String movieRelease) {
        this.movieRelease = movieRelease;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getMovieOverview() {
        return movieOverview;
    }

    public void setMovieOverview(String movieOverview) {
        this.movieOverview = movieOverview;
    }

    public String getMovieImage() {
        return movieImage;
    }

    public void setMovieImage(String movieImage) {
        movieImage = movieImage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.movieName);
        dest.writeString(this.movieOverview);
        dest.writeString(this.movieImage);
        dest.writeString(this.movieRelease);
        dest.writeValue(this.movieRating);
        dest.writeString(this.movieImageBackground);
    }

    protected Movie(Parcel in) {
        this.id = in.readInt();
        this.movieName = in.readString();
        this.movieOverview = in.readString();
        this.movieImage = in.readString();
        this.movieRelease = in.readString();
        this.movieRating = (Double) in.readValue(Double.class.getClassLoader());
        this.movieImageBackground = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}

