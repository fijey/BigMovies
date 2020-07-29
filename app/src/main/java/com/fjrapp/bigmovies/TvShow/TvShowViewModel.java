package com.fjrapp.bigmovies.TvShow;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class TvShowViewModel extends ViewModel {
    private MutableLiveData<ArrayList<TvShow>> listTvShow = new MutableLiveData<>();

    void setTvShow() {
        String API_KEY = "2eef7f621d0b892c273a8a26785b783d";
        AsyncHttpClient client = new AsyncHttpClient();
        final ArrayList<TvShow> listitems = new ArrayList<>();
        String url = "https://api.themoviedb.org/3/discover/tv?api_key=" + API_KEY + "&language=en-US";

        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                try {
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray list = responseObject.getJSONArray("results");

                    for (int i = 0; i < list.length(); i++) {
                        JSONObject tv = list.getJSONObject(i);
                        TvShow mData = new TvShow(tv);
                        listitems.add(mData);
                    }
                    listTvShow.postValue(listitems);

                } catch (Exception e) {
                    Log.d("Exception", e.getMessage());

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("Onfailure", error.getMessage());

            }
        });
    }

    LiveData<ArrayList<TvShow>> getTvShow() {
        return listTvShow;
    }
}