package com.fjrapp.bigmovies.Movie;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class MovieViewModel extends ViewModel {

    private MutableLiveData<ArrayList<Movie>> listmovie = new MutableLiveData<>();
    void setMovie() {
        String API_KEY = "2eef7f621d0b892c273a8a26785b783d";
        AsyncHttpClient client = new AsyncHttpClient();
        final ArrayList<Movie> listitems = new ArrayList<>();
        String url = "https://api.themoviedb.org/3/discover/movie?api_key=" + API_KEY + "&language=en-US";


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
                        listitems.add(mData);
                    }
                    listmovie.postValue(listitems);

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

    LiveData<ArrayList<Movie>> getMovie() {
        return listmovie;
    }

    private MutableLiveData<ArrayList<Movie>> listSearch = new MutableLiveData<>();
    void setSearch() {
        String API_KEY = "2eef7f621d0b892c273a8a26785b783d";
        AsyncHttpClient client = new AsyncHttpClient();
        final ArrayList<Movie> searchItems = new ArrayList<>();
        String url = "https://api.themoviedb.org/3/search/movie?api_key="+API_KEY+"&language=en-US&query=";


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
                        searchItems.add(mData);
                    }
                    listSearch.postValue(searchItems);

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
}
