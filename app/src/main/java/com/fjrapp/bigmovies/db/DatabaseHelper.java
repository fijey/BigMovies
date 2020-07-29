package com.fjrapp.bigmovies.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import static android.provider.BaseColumns._ID;
import static com.fjrapp.bigmovies.db.DatabaseContract.MovieColumns.BACKGROUND;
import static com.fjrapp.bigmovies.db.DatabaseContract.MovieColumns.DATE_RELEASE;
import static com.fjrapp.bigmovies.db.DatabaseContract.MovieColumns.IMAGE;
import static com.fjrapp.bigmovies.db.DatabaseContract.MovieColumns.OVERVIEW;
import static com.fjrapp.bigmovies.db.DatabaseContract.MovieColumns.RATING_DOUBLE;
import static com.fjrapp.bigmovies.db.DatabaseContract.MovieColumns.TABLE_NAME;
import static com.fjrapp.bigmovies.db.DatabaseContract.MovieColumns.TITLE;
import static com.fjrapp.bigmovies.db.DatabaseContract.TvShowColumns.BACKGROUND_TV;
import static com.fjrapp.bigmovies.db.DatabaseContract.TvShowColumns.DATE_RELEASE_TV;
import static com.fjrapp.bigmovies.db.DatabaseContract.TvShowColumns.IMAGE_TV;
import static com.fjrapp.bigmovies.db.DatabaseContract.TvShowColumns.OVERVIEW_TV;
import static com.fjrapp.bigmovies.db.DatabaseContract.TvShowColumns.RATING_DOUBLE_TV;
import static com.fjrapp.bigmovies.db.DatabaseContract.TvShowColumns.RATING_TV;
import static com.fjrapp.bigmovies.db.DatabaseContract.TvShowColumns.TABLE_NAME_TV;
import static com.fjrapp.bigmovies.db.DatabaseContract.TvShowColumns.TITLE_TV;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static String DATABASE_NAME = "dbmovie.db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String SQL_CREATE_TABLE_MOVIE = "create table " + TABLE_NAME + " ( " +
                BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TITLE + " TEXT NOT NULL, " +
                OVERVIEW + " TEXT NOT NULL, " +
                IMAGE + " TEXT NOT NULL, " +
                DATE_RELEASE + " TEXT NOT NULL, " +
                BACKGROUND + " TEXT NOT NULL, " +
                RATING_DOUBLE + " REAL NOT NULL " +
                ");";

        db.execSQL(SQL_CREATE_TABLE_MOVIE);

        String SQL_CREATE_TABLE_TVSHOW = "create table " + TABLE_NAME_TV + " ( " +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TITLE_TV + " TEXT NOT NULL, " +
                OVERVIEW_TV + " TEXT NOT NULL, " +
                IMAGE_TV + " TEXT NOT NULL, " +
                DATE_RELEASE_TV + " TEXT NOT NULL, " +
                RATING_TV + " TEXT NOT NULL, " +
                BACKGROUND_TV + " TEXT NOT NULL, " +
                RATING_DOUBLE_TV + " REAL NOT NULL " +
                ");";

        db.execSQL(SQL_CREATE_TABLE_TVSHOW);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_TV);
        onCreate(db);

    }
}
