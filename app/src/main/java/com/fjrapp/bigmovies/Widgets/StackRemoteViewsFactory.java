package com.fjrapp.bigmovies.Widgets;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.fjrapp.bigmovies.Movie.Movie;
import com.fjrapp.bigmovies.R;

import java.util.concurrent.ExecutionException;

import static com.fjrapp.bigmovies.db.DatabaseContract.MovieColumns.CONTENT_URI;

public class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Cursor list;
    private Context mContext;

    StackRemoteViewsFactory(Context context, Intent intent) {
        mContext = context;
    }


    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {

        if (list != null) {
            list.close();
        }

        final long identityToken = Binder.clearCallingIdentity();

        list = mContext.getContentResolver().query(CONTENT_URI,
                null,
                null,
                null,
                null);

        Binder.restoreCallingIdentity(identityToken);

    }

    @Override
    public void onDestroy() {
        if (list != null) {
            list.close();
        }

    }

    @Override
    public int getCount() {
        return list.getCount();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        Movie movieFavorite = getItem(position);
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);

        try {
            Bitmap bmp = Glide.with(mContext)
                    .asBitmap()
                    .load(movieFavorite.getMovieImage())
                    .apply(new RequestOptions().fitCenter())
                    .submit()
                    .get();
            rv.setImageViewBitmap(R.id.imageView, bmp);
        } catch (InterruptedException | ExecutionException e) {
            Log.d("Widget Load Error", "error");
        }
        Bundle extras = new Bundle();
        extras.putInt(FavoriteWidget.EXTRA_ITEM, position);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);

        rv.setOnClickFillInIntent(R.id.imageView, fillInIntent);
        return rv;

    }


    private Movie getItem(int position) {
        if (!list.moveToPosition(position)) {
            throw new IllegalStateException("position invalid");
        }
        return new Movie(list);
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
