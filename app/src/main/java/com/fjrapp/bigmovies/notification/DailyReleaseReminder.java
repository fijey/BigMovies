package com.fjrapp.bigmovies.notification;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.arch.lifecycle.MutableLiveData;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.fjrapp.bigmovies.MainActivity;
import com.fjrapp.bigmovies.Movie.DetailActivity;
import com.fjrapp.bigmovies.Movie.Movie;
import com.fjrapp.bigmovies.R;
import com.fjrapp.bigmovies.Tool;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class DailyReleaseReminder extends BroadcastReceiver {
    private final int NOTIF_ID_RELEASE = 20;
    private List<Movie> listMovie = new ArrayList<>(), tmpMovies;


    public DailyReleaseReminder () {

    }
    @Override
    public void onReceive(Context context, Intent intent) {
        getMovie(context);
        showNotification(context);
    }

    public void setReminder(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, DailyReleaseReminder.class);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        int requestCode = NOTIF_ID_RELEASE;
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, NOTIF_ID_RELEASE,intent,0);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY,pendingIntent);
        Toast.makeText(context, "Pengingat telah dinyalakan ", Toast.LENGTH_SHORT).show();


    }
    public void cancelReminder(Context context) {
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, DailyReleaseReminder.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, NOTIF_ID_RELEASE,intent,0);
        alarmManager.cancel(pendingIntent);
        Toast.makeText(context, "Pengingat telah dimatikan", Toast.LENGTH_SHORT).show();
    }
         private void getMovie(final Context context) {

            Date date = Calendar.getInstance().getTime();
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String TODAYDATE = dateFormat.format(date);

            AsyncHttpClient client = new AsyncHttpClient();
            String url = "https://api.themoviedb.org/3/discover/movie?api_key="+Tool.API_KEY+"&primary_release_date.gte="+TODAYDATE+"&primary_release_date.lte="+TODAYDATE;
            client.get(url, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String result = new String(responseBody);
                    try {
                        JSONObject responseObject = new JSONObject(result);
                        JSONArray list = responseObject.getJSONArray("results");

                        for (int i = 0; i < list.length(); i++) {
                            JSONObject movie = list.getJSONObject(i);
                            Movie mData = new Movie(movie);
                            listMovie.add(mData);
                            showNotification(context);
                        }

                    } catch (Exception e) {
                        Log.d("Exception", e.getMessage());

                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                }
            });
    }

    private void showNotification(Context context) {
        int REQUEST_CODE = 321;
        String CHANNEL_ID = "Channel_2";
        String CHANNEL_NAME = "Reminder Movie Date Channel";
        Intent intent;
        PendingIntent pendingIntent;

        NotificationManager notificationManager
                = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT);

            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{1000, 1000, 1000, 1000, 1000});

            builder.setChannelId(CHANNEL_ID);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        int numMovies = 0;
        try {
            numMovies = ((listMovie.size() > 0) ? listMovie.size() : 0);
        } catch (Exception e) {
            Log.w("ERROR", e.getMessage());
        }

        String message = "";
        String title = "";

        if (numMovies == 0) {
            message = "Tidak Ada Film yang Rilis Hari ini";
            intent = new Intent(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

            pendingIntent = TaskStackBuilder.create(context)
                    .addNextIntent(intent)
                    .getPendingIntent(REQUEST_CODE, PendingIntent.FLAG_UPDATE_CURRENT);

            builder.setSmallIcon(R.drawable.movie_white)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                    .setSound(alarmSound)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);

            if (notificationManager != null) {
                notificationManager.notify(0, builder.build());
            }
        } else {
            intent = new Intent(context, DetailActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

           for (int i = 0; i< listMovie.size(); i++) {
               title = listMovie.get(i).getMovieName();
               message = context.getString(R.string.release_movie_today);
               intent.putExtra(DetailActivity.EXTRA_NAME, listMovie.get(i));

               pendingIntent = TaskStackBuilder.create(context)
                       .addNextIntent(intent)
                       .getPendingIntent(i, PendingIntent.FLAG_UPDATE_CURRENT);

               builder.setSmallIcon(R.drawable.movie_white)
                       .setContentTitle(title)
                       .setContentIntent(pendingIntent)
                       .setContentText(message)
                       .setVibrate(new long[]{1000,1000,1000,1000,1000})
                       .setSound(alarmSound)
                       .setAutoCancel(true);
               if (notificationManager!= null) {
                   notificationManager.notify(i, builder.build());
               }
           }
        }

    }
}
