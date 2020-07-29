package com.fjrapp.bigmovies.db;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

import static com.fjrapp.bigmovies.Tool.AUTHORITY;

public class DatabaseContract {
    private static final String SCHEME = "content";

    private DatabaseContract() {
    }

    public static String getColumnString(Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndex(columnName));
    }

    public static int getColumnInt(Cursor cursor, String columnName) {
        return cursor.getInt(cursor.getColumnIndex(columnName));
    }


    public static final class MovieColumns implements BaseColumns {
        public static final String TABLE_NAME = "movie";
        public static final String TITLE = "title";
        public static final String OVERVIEW = "overview";
        public static final String IMAGE = "image";
        public static final String DATE_RELEASE = "date_release";
        public static final String BACKGROUND = "background";
        public static final String RATING_DOUBLE = "rating_double";

        public static final Uri CONTENT_URI = new Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build();
    }

    public static final class TvShowColumns implements BaseColumns {
        public static final String TABLE_NAME_TV = "tvshow";
        public static final String TITLE_TV = "title";
        public static final String OVERVIEW_TV = "overview";
        public static final String IMAGE_TV = "image";
        public static final String DATE_RELEASE_TV = "date_release";
        public static final String RATING_TV = "rating";
        public static final String BACKGROUND_TV = "background";
        public static final String RATING_DOUBLE_TV = "rating_double";

        public static final Uri CONTENT_URI2 = new Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME_TV)
                .build();
    }
}
